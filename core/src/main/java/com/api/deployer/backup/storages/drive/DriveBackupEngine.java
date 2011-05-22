package com.api.deployer.backup.storages.drive;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import org.apache.log4j.Logger;

import com.api.commons.config.ConfigException;
import com.api.commons.config.IConfig;
import com.api.deployer.backup.AbstractBackupEngine;
import com.api.deployer.backup.BackupException;
import com.api.deployer.backup.IBackupEngine;
import com.api.deployer.backup.result.storages.DriveBackupResult;
import com.api.deployer.backup.result.storages.IDriveBackupResult;
import com.api.deployer.backup.result.storages.IMBRBackupResult;
import com.api.deployer.backup.result.storages.IPartitionBackupResult;
import com.api.deployer.backup.result.storages.MBRBackupResult;
import com.api.deployer.backup.storages.IStorageBackupFacade;
import com.api.deployer.system.devices.storage.IStorageDevicePartition;
import com.api.deployer.system.devices.storage.IStorageDriveDevice;
import com.api.deployer.system.devices.storage.PartitionFlag;
import com.api.deployer.system.devices.storage.PartitionType;
import com.api.deployer.system.devices.storage.StorageFilesystem;
import com.api.deployer.system.configurers.IPartitionsEditor;
import com.api.deployer.system.ISystemFacade;
import com.api.deployer.system.scripts.IScriptExecutor;

public class DriveBackupEngine extends AbstractBackupEngine<IStorageDriveDevice, IDriveBackupResult> {
	public enum Operations {
		MBR_BACKUP,
		GRUB_BACKUP,
		PARTITIONS_BACKUP
	}
	
	private static Logger log = Logger.getLogger( DriveBackupEngine.class );
	private IStorageBackupFacade facade;
	
	public DriveBackupEngine( IStorageBackupFacade facade, ISystemFacade system) {
		super(system);
		
		this.facade = facade;
	}

	protected IStorageBackupFacade getFacade() {
		return this.facade;
	}
	
	public void setFacade( IStorageBackupFacade facade ) {
		this.facade = facade;
	}
	
	@Override
	public IDriveBackupResult save(IStorageDriveDevice source, String mountPath )
			throws BackupException {
		try {
			IDriveBackupResult result = this.createBackupResult(source, mountPath);
			
			if ( !this.isExcluded( Operations.PARTITIONS_BACKUP ) ) {
				result.setPartitionsResult( this.backupPartitions( source, mountPath ) );
			}
			
			if ( !this.isExcluded( Operations.MBR_BACKUP ) ) {
				result.setMBRResult( this.backupMBR( source, mountPath ) );
			}
			
//			if ( !this.isExcluded( Operations.GRUB_BACKUP ) ) {
//				//...
//			}
			
			log.info("Backup successful!");
			
			return result;
		} catch ( IOException e ) {
			throw new BackupException( "I/O exception during device backup", e );
		}
	}
	
	protected Collection<IPartitionBackupResult> backupPartitions( IStorageDriveDevice source, String path ) throws IOException, BackupException {
		// clone each disk drive partition
		Collection<IPartitionBackupResult> partitions = new HashSet<IPartitionBackupResult>();
		for ( IStorageDevicePartition partition : source.getPartitions() ) {
			if ( !( this.isExcluded( partition ) || this.isExcluded( partition.getUUID() ) || this.isExcluded( partition.getFilesystem() ) ) ) {
				IBackupEngine<IStorageDevicePartition,
					IPartitionBackupResult> engine = this.getFacade().selectEngine(partition);
				engine.makeSilent( this.isSilent() );
				
				partitions.add( engine.save(partition, path) );
			}
		}
		
		return partitions;
	}
	
	protected IMBRBackupResult backupMBR( IStorageDriveDevice source, String path ) throws IOException, BackupException {
		String mbrPath = path + "/mbr.bin";
		
		// Backup MBR( 448 bytes ) + partitions table ( 64 bytes ) 
		IScriptExecutor executor = this.prepareMBRBackupExecutor( source, mbrPath );
		
		executor.execute();
		
		return new MBRBackupResult( mbrPath, 512 );
	}
	
	protected IScriptExecutor prepareMBRBackupExecutor( IStorageDriveDevice device, String mountPath ) {
		IScriptExecutor executor = this.getSystem().getConsole().createExecutor( this, "dd");
		executor.setParameter("if", device.getPath() );
		executor.setParameter("of", mountPath );
		executor.setParameter("bs", 512);
		executor.setParameter("count", 1 );
		
		return executor;
	}

	protected IScriptExecutor prepareMBRRestoreExecutor( IStorageDriveDevice device, String source ) {
		IScriptExecutor executor = this.getSystem().getConsole().createExecutor( this, "dd");
		executor.setParameter("if", source + "/mbr.bin");
		executor.setParameter("of", device.getPath() );
		executor.setParameter("bs", 512);
		executor.setParameter("count", 1);
		
		return executor;
	}
	
	@Override
	public void restore(String source, IStorageDriveDevice device, IConfig meta ) throws BackupException {
		try {
			this.prepareMBRRestoreExecutor( device, source )
				.execute();
						
			IPartitionsEditor editor = this.getSystem()
										   .createPartitionsEditor( 
												   device );
			try {
				editor.deletePartitions();
			} catch ( IOException e ) {
				throw new BackupException("Error while trying to clean " +
					"partitions table of device: " + device.getName(), e );
			}
			
			for ( IConfig partitionNode : meta.get("partitions").childs() ) {
				try {
					IStorageDevicePartition partition = editor.createPartition( 
						Integer.valueOf( partitionNode.get("number").value() ), 
						StorageFilesystem.valueOf( partitionNode.get("filesystem").value() ), 
						PartitionType.valueOf( partitionNode.get("type").value() ), 
						Long.valueOf( partitionNode.get("start").value() ), 
						Long.valueOf( partitionNode.get("end").value() ) );
					
					this.restoreNode( partition, partitionNode, source );
					
					if ( partition.checkFlag( PartitionFlag.BOOT ) ) {
						this.installGrub(partition);
					}
				} catch ( IOException e  ) {
					throw new BackupException("Cannot restore partition with number: " + partitionNode.get("number").value(), e);
				}
			}
		} catch ( ConfigException e ) {
			throw new BackupException("Drive restoration exception", e );
		} catch ( IOException e  ) {
			throw new BackupException("I/O exception while trying to restore backup image", e);
		}
	}
	
	protected void installGrub( IStorageDevicePartition partition ) throws IOException {
		IScriptExecutor executor = this.getSystem().getConsole().createExecutor("grub");
		executor.addUnnamedParameter( partition.getPath() );
		
		executor.execute();
	}
	
	protected void restoreNode( IStorageDevicePartition partition, IConfig meta, String source )
		throws BackupException {
			IBackupEngine<
				IStorageDevicePartition, 
				IPartitionBackupResult> backupEngine = this.getFacade().selectEngine(partition);
			backupEngine.makeSilent( this.isSilent() );
			
			backupEngine.restore( source, partition, meta );
	}

	@Override
	protected IDriveBackupResult createBackupResult( IStorageDriveDevice device, String finalName) {
		IDriveBackupResult result = new DriveBackupResult(finalName);
		result.setUUID( device.getUUID() );
		result.setName( device.getName() );
		result.setPath( device.getPath() );
		result.setModel( device.getModel() );
		result.setPartitionsType( device.getPartitionsType() );
		
		return result;
	}
	
}
