package com.api.deployer.backup.storages.partclone.extfs;

import com.api.commons.config.ConfigException;
import com.api.commons.config.IConfig;
import com.api.deployer.backup.storages.partclone.AbstractPartcloneEngine;
import com.api.deployer.system.devices.storage.IStorageDevicePartition;
import com.api.deployer.system.ISystemFacade;
import com.api.deployer.system.scripts.IScriptExecutor;

public class ExtfsPartcloneEngine extends AbstractPartcloneEngine {
	
	public ExtfsPartcloneEngine( ISystemFacade facade ) {
		super(facade);
	}
	
	@Override
	protected IScriptExecutor prepareExecutor( IStorageDevicePartition partition, 
				String mountPath ) {
		IScriptExecutor executor = this.getSystem()
									   .getConsole()
									   .createExecutor("partclone.extfs");
		
		executor.addUnnamedParameter("-c")
				.addUnnamedParameter("-s")
				.addUnnamedParameter("-F")
				.addUnnamedParameter("-R")
				.addUnnamedParameter( partition.getPath() )
				.addUnnamedParameter("-o")
				.addUnnamedParameter(
					String.format(
						"%s/%s",
						mountPath,
						this.prepareFinalName( partition )
					)
				);
		
		return executor;
	}

	@Override
	protected IScriptExecutor prepareRestoreExecutor(String mountPath, IStorageDevicePartition partition, IConfig meta) throws ConfigException {
		IScriptExecutor executor = this.getSystem().getConsole().createExecutor("partclone.extfs");
		executor.addUnnamedParameter("-r")
				.addUnnamedParameter("-s")
				.addUnnamedParameter( mountPath )
				.addUnnamedParameter("-o")
				.addUnnamedParameter( partition.getPath() );
		
		return executor;
	}

}
