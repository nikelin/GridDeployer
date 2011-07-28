package com.api.deployer.system.devices.storage;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class StorageDiskDrive implements IStorageDriveDevice {
	private static final long serialVersionUID = -7466955397618351303L;
	
	private String name;
	private UUID uuid;
	private Long size;
	private Integer sectorSize;
	private String type;
	private String model;
	private Collection<IStorageDevicePartition> partitions = new HashSet<IStorageDevicePartition>();
	private String path;
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public UUID getUUID() {
		return this.uuid;
	}

	@Override
	public String getPath() {
		return this.path;
	}

	@Override
	public void setSectorSize(Integer sectorSize) {
		this.sectorSize = sectorSize;
	}

	@Override
	public Integer getSectorSize() {
		return this.sectorSize;
	}

	@Override
	public void setModel(String model) {
		this.model = model;
	}

	@Override
	public String getModel() {
		return this.model;
	}

	@Override
	public void addPartition(IStorageDevicePartition partition) {
		this.partitions.add(partition);
	}

	@Override
	public Collection<IStorageDevicePartition> getPartitions() {
		return this.partitions;
	}

	@Override
	public void setSize(Long size) {
		this.size = size;
	}

	@Override
	public Long getSize() {
		return this.size;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public void setPartitionsType(String type) {
		this.type = type;
	}

	@Override
	public String getPartitionsType() {
		return this.type;
	}
	
	@Override
	public IStorageDevicePartition createPartition() {
		return new StorageDiskPartition(this);
	}
	
}
