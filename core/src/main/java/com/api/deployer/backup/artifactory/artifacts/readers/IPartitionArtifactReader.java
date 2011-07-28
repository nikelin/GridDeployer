package com.api.deployer.backup.artifactory.artifacts.readers;

import java.util.Collection;
import java.util.UUID;

import com.api.deployer.backup.artifactory.ReaderException;
import com.api.deployer.system.devices.storage.PartitionFlag;
import com.api.deployer.system.devices.storage.PartitionType;
import com.api.deployer.system.devices.storage.StorageFilesystem;

public interface IPartitionArtifactReader {
	
	public String readPath() throws ReaderException;
	
	public UUID readUUID() throws ReaderException;
	
	public Integer readNumber() throws ReaderException;
	
	public Long readStart() throws ReaderException;
	
	public Long readEnd() throws ReaderException;
	
	public PartitionType readType() throws ReaderException;
	
	public StorageFilesystem readFileSystem() throws ReaderException;
	
	public Collection<PartitionFlag> readFlags() throws ReaderException;
	
}
