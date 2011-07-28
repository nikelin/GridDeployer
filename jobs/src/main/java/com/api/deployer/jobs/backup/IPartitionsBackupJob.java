package com.api.deployer.jobs.backup;

import java.util.Collection;
import java.util.UUID;

import com.api.commons.bindings.annotations.Bindable;
import com.api.commons.bindings.annotations.ElementType;
import com.api.deployer.system.devices.storage.IStorageDevicePartition;
import com.api.deployer.system.devices.storage.IStorageDriveDevice;

public interface IPartitionsBackupJob extends IBackupJob {

    public IStorageDriveDevice getDevice();

	public void addPartition( IStorageDevicePartition partition );

	public void setPartitions( Collection<IStorageDevicePartition> partitions );
	
	public Collection<IStorageDevicePartition> getPartitions();
	
}
