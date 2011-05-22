package com.api.deployer.backup.artifactory.artifacts.writers;

import java.util.UUID;

import com.api.deployer.backup.artifactory.WriterException;

public interface IDriveArtifactWriter extends IArtifactWriter {
	
	public void writeModel( String model ) throws WriterException ;
	
	public void writeUUID( UUID uuid ) throws WriterException ;
	
	public void writePartition( UUID partition ) throws WriterException ;
	
	public void writePath( String path ) throws WriterException ;
	
}
