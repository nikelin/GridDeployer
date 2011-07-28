package com.api.deployer.system.devices.storage;

import java.util.Collection;

public interface IStorageDriveDevice extends IStorageDevice {
	
	public void setSectorSize( Integer sectorSize );
	
	public Integer getSectorSize();
	
	public void setModel( String model );
	
	public String getModel();
	
	public void addPartition( IStorageDevicePartition partition );
	
    public Collection<IStorageDevicePartition> getPartitions();
    
    public void setPartitionsType( String type );
    
    public String getPartitionsType();
    
    public IStorageDevicePartition createPartition();
    
}
