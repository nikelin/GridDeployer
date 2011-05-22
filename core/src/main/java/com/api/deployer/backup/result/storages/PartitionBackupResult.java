package com.api.deployer.backup.result.storages;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import com.api.deployer.system.devices.storage.PartitionFlag;
import com.api.deployer.system.devices.storage.PartitionType;
import com.api.deployer.system.devices.storage.StorageFilesystem;

public class PartitionBackupResult implements IPartitionBackupResult {
	private String finalName;
	private UUID uuid;
	private Integer number;
	private StorageFilesystem filesystem;
	private Long start;
	private Long end;
	private Long size;
	private PartitionType type;
	private Collection<PartitionFlag> flags = new HashSet<PartitionFlag>();
	
	public PartitionBackupResult( String finalName ) {
		this.finalName = finalName;
	}
	
	public void setType( PartitionType type ) {
		this.type = type;
	}
	
	public PartitionType getType() {
		return this.type;
	}
	
	@Override
	public String getFinalName() {
		return this.finalName;
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
	public void setSize(Long size) {
		this.size = size;
	}

	@Override
	public Long getSize() {
		return this.size;
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

	public void setEnd(Long end) {
		this.end = end;
	}

	public Long getEnd() {
		return end;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public Long getStart() {
		return start;
	}

	@Override
	public void addFlag(PartitionFlag flag) {
		this.flags.add(flag);
	}

	@Override
	public Collection<PartitionFlag> getFlags() {
		return this.flags;
	}

}
