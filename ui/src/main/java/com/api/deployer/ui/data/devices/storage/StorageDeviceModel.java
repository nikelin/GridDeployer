package com.api.deployer.ui.data.devices.storage;

import com.api.deployer.ui.data.devices.AbstractDeviceModel;
import com.redshape.ui.data.IModelData;

public class StorageDeviceModel extends AbstractDeviceModel {
	public final static String PATH = "path";
	public final static String NAME = "name";
	public final static String PARTITIONS = "partitions";
	
	public StorageDeviceModel() {
		super( StorageDevice.class );
		
		this.addField(PATH);
		this.addField(NAME);
		this.addField(PARTITIONS);
	}
	
	@Override
	public IModelData createRecord() {
		return new StorageDevice();
	}
	
}
