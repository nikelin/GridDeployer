package com.api.deployer.backup.result.storages;

import java.util.Collection;
import java.util.UUID;

import com.api.deployer.backup.result.IBackupResult;
import com.api.deployer.system.devices.storage.PartitionFlag;
import com.api.deployer.system.devices.storage.PartitionType;
import com.api.deployer.system.devices.storage.StorageFilesystem;

public interface IPartitionBackupResult extends IBackupResult {
	
	public void setUUID( UUID uuid );
	
	public UUID getUUID();
	
	public void addFlag( PartitionFlag flag );
	
	public Collection<PartitionFlag> getFlags();
	
	public void setType( PartitionType type );
	
	public PartitionType getType();
	
	public void setSize( Long size );
	
	public Long getSize();
	
	public void setFilesystem( StorageFilesystem filesystem );
	
	public StorageFilesystem getFilesystem();
	
	public void setNumber( Integer number );
	
	public Integer getNumber();
	
	public Long getStart();
	
	public void setStart( Long start );
	
	public void setEnd( Long end );
	
	public Long getEnd();
	
}
