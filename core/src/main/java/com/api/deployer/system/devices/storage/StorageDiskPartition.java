package com.api.deployer.system.devices.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StorageDiskPartition implements IStorageDevicePartition {
	private static final long serialVersionUID = -7709509962111145992L;
	
	private Long size;
	private String name;
	private String path;
	private UUID uuid;
	private StorageFilesystem filesystem;
	private IStorageDriveDevice device;
	private PartitionType type;
	private Long end;
	private Long start;
	private Integer number;
	private Map<PartitionFlag, Boolean> flags = new HashMap<PartitionFlag, Boolean>();
	
	public StorageDiskPartition( IStorageDriveDevice device ) {
		this.device = device;
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
	public String getName() {
		return this.name;
	}

	@Override
	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public UUID getUUID() {
		return this.uuid;
	}

	@Override
	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String getPath() {
		return this.path;
	}

	@Override
	public IStorageDriveDevice getDevice() {
		return this.device;
	}

	@Override
	public void setFilesystem(StorageFilesystem filesystem) {
		this.filesystem = filesystem;
	}

	@Override
	public StorageFilesystem getFilesystem() {
		return this.filesystem;
	}

	@Override
	public void setNumber(Integer number) {
		this.number = number;
	}

	@Override
	public Integer getNumber() {
		return this.number;
	}

	@Override
	public void setType(PartitionType type) {
		this.type = type;
	}

	@Override
	public PartitionType getType() {
		return this.type;
	}

	@Override
	public void setStart(Long start) {
		this.start = start;
	}

	@Override
	public Long getStart() {
		return this.start;
	}

	@Override
	public void setEnd(Long end) {
		this.end = end;
	}

	@Override
	public Long getEnd() {
		return this.end;
	}

	@Override
	public boolean checkFlag(PartitionFlag flag) {
		return this.flags.get(flag) != null && this.flags.get(flag);
	}

	@Override
	public void setFlag(PartitionFlag flag, boolean value) {
		this.flags.put(flag, value);
	}

}
