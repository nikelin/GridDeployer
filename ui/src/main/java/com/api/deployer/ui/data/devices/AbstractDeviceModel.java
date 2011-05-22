package com.api.deployer.ui.data.devices;

import com.redshape.ui.data.AbstractModelType;

public abstract class AbstractDeviceModel extends AbstractModelType {
	
	public static final String ID = "id";
	
	public AbstractDeviceModel() {
		super();
		
		this.addField(ID);
	}
	
}
