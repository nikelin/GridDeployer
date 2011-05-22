package com.api.deployer.backup.artifactory.artifacts.writers;

import java.util.UUID;

import com.api.deployer.backup.artifactory.WriterException;
import com.api.deployer.system.devices.storage.PartitionFlag;
import com.api.deployer.system.devices.storage.PartitionType;
import com.api.deployer.system.devices.storage.StorageFilesystem;

public interface IPartitionArtifactWriter extends IArtifactWriter {

	public void writeNumber( Integer number ) throws WriterException;
	
	public void writeDevice( UUID id ) throws WriterException;
	
	public void writeStart( Long start ) throws WriterException;
	
	public void writeEnd( Long end ) throws WriterException;
	
	public void writeType( PartitionType type ) throws WriterException;
	
	public void writeFileSystem( StorageFilesystem filesystem ) throws WriterException;
	
	public void writeUUID( UUID id ) throws WriterException;
	
	public void writeFlag( PartitionFlag flag ) throws WriterException;
	
}
