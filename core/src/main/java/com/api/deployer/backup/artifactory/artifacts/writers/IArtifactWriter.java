package com.api.deployer.backup.artifactory.artifacts.writers;

import com.api.commons.config.IConfig;
import com.api.deployer.backup.artifactory.WriterException;
import com.api.deployer.backup.compression.CompressionLevel;
import com.api.deployer.backup.compression.CompressionMethod;
import com.api.deployer.backup.diff.DiffMethod;

public interface IArtifactWriter {
	
	public void writeCompressionMethod( CompressionMethod method );
	
	public void writeCompressionLevel( CompressionLevel level );

	public void writeDiffMethod( DiffMethod method );
	
	public IConfig flush() throws WriterException;
	
}
