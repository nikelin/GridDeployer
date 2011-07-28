package com.api.deployer.backup.storages.generic;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.api.commons.config.IConfig;
import com.api.deployer.backup.AbstractBackupEngine;
import com.api.deployer.backup.BackupException;
import com.api.deployer.backup.result.storages.IPartitionBackupResult;
import com.api.deployer.backup.result.storages.PartitionBackupResult;
import com.api.deployer.system.devices.storage.IStorageDevicePartition;
import com.api.deployer.system.ISystemFacade;
import com.api.deployer.system.scripts.IScriptExecutor;

public class DDCloneEngine extends AbstractBackupEngine<IStorageDevicePartition, IPartitionBackupResult>{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( DDCloneEngine.class );
	
	private static final long GB = 1000000000;
	private static final long MB = 1000000;
	private static final long KB = 1000;
	
	public DDCloneEngine( ISystemFacade system ) {
		super(system);
	}

	@Override
	public IPartitionBackupResult save( IStorageDevicePartition source, String mountPath)
			throws BackupException {
		try {
			IScriptExecutor executor = this.getSystem().getConsole().createExecutor("dd");
			executor.setParameter("if", source.getPath() );
			executor.setParameter(
				"of", 
				String.format(
					"%s/%s", 
					mountPath, 
					this.prepareFinalName( source )
				) 
			);
			
			executor.setParameter("count", this.prepareSize( source.getSize() ) );
			
			String output = executor.execute();
			
			if ( !executor.isSuccess() &&  !this.isSilent() ) {
				throw new BackupException("Cannot process partition clone: " + output );
			}
			
			return this.createBackupResult( source, mountPath + "/" + this.prepareFinalName(source) );
		} catch ( IOException e ) {
			throw new BackupException("I/O exception during backup", e );
		}
	}

	@Override
	public void restore( String source, IStorageDevicePartition device, IConfig meta )
			throws BackupException {
		try {
			IScriptExecutor executor = this.getSystem().getConsole().createExecutor("dd");
			executor.setParameter("of", device.getPath() );
			executor.setParameter("if", source );
			executor.setParameter("count", this.prepareSize( device.getSize() ) );
			
			executor.execute();
		} catch ( IOException e  ) {
			throw new BackupException("I/O exception while trying to restore partition record");
		}
	}
	
	private String prepareSize( Long size ) {
		double value = 0;
		String range = null;
		if ( size < KB ) {
			value = size.doubleValue();
			range = "c";
		} else if ( size >= KB && size < MB ) {
			value = size / KB;
			range = "kB";
		} else if ( size >= MB && size < GB ) {
			value = size / MB;
			range = "MB";
		} else if ( size > GB ) {
			value = size / GB; 
			range = "GB";
		}
		
		if ( range == null ) {
			throw new IllegalArgumentException();
		}
		
		return new StringBuffer()
					.append( (int) Math.ceil( value ) )
					.append( range )
						.toString();
	}

	@Override
	protected IPartitionBackupResult createBackupResult( IStorageDevicePartition source, String finalName) {
		IPartitionBackupResult result = new PartitionBackupResult(finalName);
		result.setEnd( source.getEnd() );
		result.setType( source.getType() );
		result.setStart( source.getStart() );
		result.setFilesystem( source.getFilesystem() );
		result.setSize( source.getSize() );
		result.setUUID( source.getUUID() );
		result.setNumber( source.getNumber() );
		
		return result;
	}

}
