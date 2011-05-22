package com.api.deployer.ui.data.devices.storage;

import com.api.deployer.system.devices.IDevice;
import com.api.deployer.ui.data.devices.AbstractDevice;

public class StorageDevice extends AbstractDevice {

	public StorageDevice() {
		this(null);
	}

	public StorageDevice( IDevice device ) {
		super();

		this.setRelatedObject(device);

	}
	
	public String getPath() {
		return this.get( StorageDeviceModel.PATH );
	}
	
	public void setPath( String path ) {
		this.set( StorageDeviceModel.PATH, path );
	}
	
	@Override
	public String toString() {
		return this.getPath().toString();
	}
	
}
