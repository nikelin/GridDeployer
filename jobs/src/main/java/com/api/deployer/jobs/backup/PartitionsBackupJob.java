package com.api.deployer.jobs.backup;

import com.api.deployer.system.devices.storage.IStorageDevice;
import com.api.deployer.system.devices.storage.IStorageDevicePartition;
import com.api.deployer.system.devices.storage.IStorageDriveDevice;
import com.redshape.bindings.annotations.Bindable;
import com.redshape.bindings.annotations.BindableAttributes;
import com.redshape.bindings.annotations.ElementType;
import com.redshape.bindings.types.BindableType;
import com.redshape.bindings.types.CollectionType;

import java.util.Collection;
import java.util.UUID;

public class PartitionsBackupJob extends AbstractBackupJob implements IPartitionsBackupJob {
	private static final long serialVersionUID = 7309530764171540497L;

    @Bindable( name = "Device", type = BindableType.LIST )
    @ElementType( value = IStorageDevice.class )
    private IStorageDriveDevice device;

    @Bindable( name = "Partitions", attributes = {BindableAttributes.MULTICHOICE} )
    @ElementType(
        value = IStorageDevicePartition.class,
        type = CollectionType.CHOICE )
	private Collection<IStorageDevicePartition> partitions;

    public PartitionsBackupJob() {
        this(null);
    }

	public PartitionsBackupJob( UUID agentId ) {
		super(agentId);
	}

    public void setDevice( IStorageDriveDevice device ) {
        this.device = device;
    }

    public IStorageDriveDevice getDevice() {
        return this.device;
    }

	@Override
	public void addPartition( IStorageDevicePartition partition ) {
		this.partitions.add(partition);
	}
	
	@Override
	public void setPartitions( Collection<IStorageDevicePartition> partitions ) {
		this.partitions = partitions;
	}
	
	@Override
	public Collection<IStorageDevicePartition> getPartitions() {
		return this.partitions;
	}
	
}
