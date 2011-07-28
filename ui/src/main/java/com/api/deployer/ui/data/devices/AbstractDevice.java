package com.api.deployer.ui.data.devices;

import java.util.UUID;

import com.redshape.ui.data.AbstractModelData;

public abstract class AbstractDevice extends AbstractModelData {

	public AbstractDevice() {
		super();
	}
	
	public void setUUID( UUID id ) {
		this.set( AbstractDeviceModel.ID, id );
	}
	
	public UUID getUUID() {
		return this.get( AbstractDeviceModel.ID );
	}
	
}
