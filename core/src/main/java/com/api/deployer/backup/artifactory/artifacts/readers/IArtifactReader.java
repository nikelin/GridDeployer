package com.api.deployer.backup.artifactory.artifacts.readers;

import com.api.deployer.backup.compression.CompressionLevel;
import com.api.deployer.backup.compression.CompressionMethod;
import com.api.deployer.backup.diff.DiffMethod;

public interface IArtifactReader {
	
	public boolean readCompressionState();
	
	public CompressionMethod readCompressionMethod();
	
	public CompressionLevel readCompressionLevel();
	
	public boolean readDeltaState();
	
	public DiffMethod getDiffMethod();
	
}
