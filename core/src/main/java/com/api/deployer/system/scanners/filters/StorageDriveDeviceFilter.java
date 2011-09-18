package com.api.deployer.system.scanners.filters;

import com.api.deployer.system.devices.IDevice;
import com.api.deployer.system.devices.storage.IStorageDriveDevice;
import com.redshape.utils.IFilter;

import java.io.Serializable;

public class StorageDriveDeviceFilter implements IFilter<IDevice>, Serializable {
	private static final long serialVersionUID = 138732127738689601L;

	@Override
	public boolean filter( IDevice device ) {
		return device instanceof IStorageDriveDevice;
	}
	
}
