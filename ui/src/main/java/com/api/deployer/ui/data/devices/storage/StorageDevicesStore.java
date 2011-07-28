package com.api.deployer.ui.data.devices.storage;

import com.api.deployer.ui.data.devices.AbstractDeviceStorage;
import com.redshape.ui.data.loaders.IDataLoader;

public class StorageDevicesStore extends AbstractDeviceStorage<StorageDevice> {

	public StorageDevicesStore() {
		this( null );
	}

	public StorageDevicesStore( IDataLoader<StorageDevice> loader ) {
		super( new StorageDeviceModel(), loader );
	}

}
