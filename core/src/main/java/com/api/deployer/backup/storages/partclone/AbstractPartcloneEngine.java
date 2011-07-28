package com.api.deployer.backup.storages.partclone;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.api.commons.config.ConfigException;
import com.api.commons.config.IConfig;
import com.api.deployer.backup.AbstractBackupEngine;
import com.api.deployer.backup.BackupException;
import com.api.deployer.backup.result.IBackupResult;
import com.api.deployer.backup.result.storages.IPartitionBackupResult;
import com.api.deployer.backup.result.storages.PartitionBackupResult;
import com.api.deployer.system.devices.storage.IStorageDevicePartition;
import com.api.deployer.system.configurers.IPartitionsEditor;
import com.api.deployer.system.ISystemFacade;
import com.api.deployer.system.scripts.IScriptExecutor;

public abstract class AbstractPartcloneEngine extends AbstractBackupEngine<IStorageDevicePartition, IBackupResult> {
	private static final Logger log = Logger.getLogger( AbstractPartcloneEngine.class );

	public AbstractPartcloneEngine( ISystemFacade system ) {
		super(system);
	}

	@Override
	public IBackupResult save(IStorageDevicePartition source, String mountPath)
			throws BackupException {
		try {
			this.unmountDevice( source );
			
			IScriptExecutor process = this.prepareExecutor(source, mountPath);
			
			String output = process.execute();
			
			if ( !process.isSuccess() && !this.isSilent() ) {
				throw new BackupException("Cannot clone requested device: " + output );
			}
			
			return this.createBackupResult(source, mountPath + "/" + this.prepareFinalName(source) );
		} catch ( IOException e ) {
			throw new BackupException("I/O exception while partition saving", e);
		}
	}
	
	abstract protected IScriptExecutor prepareExecutor( 
			IStorageDevicePartition source, String mountPath );
	
	abstract protected IScriptExecutor prepareRestoreExecutor(
			String mountPath, IStorageDevicePartition partition, IConfig meta ) throws ConfigException;
	
	@Override
	public void restore( String source, IStorageDevicePartition device, IConfig meta )
			throws BackupException {
		try {
			this.unmountDevice( device );
			IScriptExecutor process = this.prepareRestoreExecutor( source, device, meta );
			
			String output = process.execute();
			
			if ( !process.isSuccess() && !this.isSilent() ) {
				throw new BackupException("Partition restoration exception: " + output );
			}
		} catch ( IOException e  ) {
			throw new BackupException("I/O exception while partition saving", e );
		} catch ( ConfigException e ) {
			throw new BackupException("Metadata processing exception", e );
		}
	}

	protected void unmountDevice( IStorageDevicePartition partition ) {
		IPartitionsEditor editor = this.getSystem().createPartitionsEditor( partition.getDevice() );
		
		log.info("Trying to unmount partition...");
		try {
			editor.unmountPartition( partition );
		} catch ( IOException e ) {
			log.error("Cannot unmount partition...", e);
		}
	}
	
	@Override
	protected IPartitionBackupResult createBackupResult( IStorageDevicePartition partition, String finalName) {
		IPartitionBackupResult result = new PartitionBackupResult(finalName);
		result.setEnd( partition.getEnd() );
		result.setStart( partition.getStart() );
		result.setUUID( partition.getUUID() );
		result.setType( partition.getType() );
		result.setSize( partition.getSize() );
		result.setNumber( partition.getNumber() );
		result.setFilesystem( partition.getFilesystem() );
		
		return result;
	}
	
}
