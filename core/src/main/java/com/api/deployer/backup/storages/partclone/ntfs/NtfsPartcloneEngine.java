package com.api.deployer.backup.storages.partclone.ntfs;

import com.api.deployer.backup.storages.partclone.AbstractPartcloneEngine;
import com.api.deployer.system.ISystemFacade;
import com.api.deployer.system.devices.storage.IStorageDevicePartition;
import com.api.deployer.system.scripts.IScriptExecutor;
import com.redshape.utils.config.ConfigException;
import com.redshape.utils.config.IConfig;

public class NtfsPartcloneEngine extends AbstractPartcloneEngine {
	
	public NtfsPartcloneEngine( ISystemFacade facade ) {
		super(facade);
	}
	
	@Override
	protected IScriptExecutor prepareExecutor( IStorageDevicePartition partition, 
			String mountPath) {
		IScriptExecutor executor = this.getSystem().getConsole().createExecutor("partclone.ntfs");
		executor.addUnnamedParameter("-c")
				.addUnnamedParameter("-s")
				.addUnnamedParameter( partition.getPath() )
				.addUnnamedParameter("-o")
				.addUnnamedParameter(
					String.format( 
						"%s/%s",
						mountPath,
						this.prepareFinalName(partition)
					)
				);
		
		return executor;
	}

	@Override
	protected IScriptExecutor prepareRestoreExecutor(String mountPath,
			IStorageDevicePartition partition, IConfig meta) throws ConfigException {
		IScriptExecutor executor = this.getSystem().getConsole().createExecutor("partclone.ntfs");
		executor.addUnnamedParameter("-r")
				.addUnnamedParameter("-s")
				.addUnnamedParameter( mountPath )
				.addUnnamedParameter("-o")
				.addUnnamedParameter( partition.getPath() );
		
		return executor;
	}
	
}
