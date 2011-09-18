package com.api.deployer.jobs.backup;

import com.api.deployer.system.devices.storage.IStorageDevicePartition;
import com.api.deployer.system.devices.storage.IStorageDriveDevice;

import java.util.Collection;

public interface IPartitionsBackupJob extends IBackupJob {

    public IStorageDriveDevice getDevice();

	public void addPartition( IStorageDevicePartition partition );

	public void setPartitions( Collection<IStorageDevicePartition> partitions );
	
	public Collection<IStorageDevicePartition> getPartitions();
	
}
