package com.api.deployer.system.devices.storage;

public interface IStorageDevicePartition extends IStorageDevice {
	
	public IStorageDriveDevice getDevice();
	
	public void setFilesystem( StorageFilesystem filesystem );
	
	public StorageFilesystem getFilesystem();
	
	public void setNumber( Integer number );
	
	public Integer getNumber();
	
	public void setType( PartitionType type );
	
	public PartitionType getType();
	
	public void setStart( Long start );
	
	public Long getStart();
	
	public void setEnd( Long end );
	
	public Long getEnd();
	
	public boolean checkFlag( PartitionFlag flag );
	
	public void setFlag( PartitionFlag flag, boolean value );
	
}
