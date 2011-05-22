package com.api.deployer.backup.diff;

import com.api.deployer.backup.compression.CompressionMethod;
import com.api.deployer.backup.compression.ICompressionEngine;

public interface ICompressionEngineFacade {
	
	public ICompressionEngine getEngine( CompressionMethod method );
	
	public void registerEngine( CompressionMethod method, ICompressionEngine engien );
	
	public boolean isSupported( CompressionMethod method );
	
}
