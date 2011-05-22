package com.api.deployer.ui.data.devices;

import com.redshape.ui.data.loaders.IDataLoader;
import com.redshape.ui.data.stores.ListStore;

public abstract class AbstractDeviceStorage<T extends AbstractDevice> extends ListStore<T> {

	public AbstractDeviceStorage( AbstractDeviceModel type, IDataLoader<T> loader ) {
		super(type, loader);
	}

}
