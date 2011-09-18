package com.api.deployer.backup.storages.generic;

import com.api.deployer.backup.AbstractBackupEngine;
import com.api.deployer.backup.BackupException;
import com.api.deployer.backup.result.storages.IPartitionBackupResult;
import com.api.deployer.backup.result.storages.PartitionBackupResult;
import com.api.deployer.system.ISystemFacade;
import com.api.deployer.system.devices.storage.IStorageDevicePartition;
import com.api.deployer.system.scripts.IScriptExecutor;
import com.redshape.utils.config.ConfigException;
import com.redshape.utils.config.IConfig;

import java.io.IOException;

public class SwapCloneEngine extends AbstractBackupEngine<IStorageDevicePartition, 
														  IPartitionBackupResult> {

	public SwapCloneEngine(ISystemFacade system) {
		super(system);
	}

	@Override
	public IPartitionBackupResult save(IStorageDevicePartition source, String destination)
			throws BackupException {
		IPartitionBackupResult result = this.createBackupResult( source, null);
		result.setSize( source.getSize() );
		result.setStart( source.getStart() );
		result.setEnd( source.getEnd() );
		result.setNumber( source.getNumber() );
		result.setFilesystem( source.getFilesystem() );
		result.setUUID( source.getUUID() );
		
		return result;
	}

	@Override
	public void restore( String source, IStorageDevicePartition device, IConfig meta )
			throws BackupException {
		try {
			IScriptExecutor executor = this.getSystem().getConsole().createExecutor("mkswap");
			executor.addUnnamedParameter("-f");
			executor.addUnnamedParameter( "/dev/" + meta.get("device").value() + meta.get("number").value() );
			
			executor.execute();
		} catch ( ConfigException e ) {
			throw new BackupException("Error processing backup meta", e);
		} catch ( IOException e ) {
			throw new BackupException("I/O exception while trying to recover partition");
		}
	}

	@Override
	protected IPartitionBackupResult createBackupResult( IStorageDevicePartition partition, String finalName) {
		IPartitionBackupResult result = new PartitionBackupResult(finalName);
		result.setEnd( partition.getEnd() );
		result.setStart( partition.getStart() );
		result.setUUID( partition.getUUID() );
		result.setSize( partition.getSize() );
		result.setNumber( partition.getNumber() );
		result.setFilesystem( partition.getFilesystem() );
		
		return result;
	}

}
