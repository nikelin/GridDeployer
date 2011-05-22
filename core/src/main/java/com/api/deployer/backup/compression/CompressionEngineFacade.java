package com.api.deployer.backup.compression;

import java.util.HashMap;
import java.util.Map;

import com.api.deployer.backup.diff.ICompressionEngineFacade;

public class CompressionEngineFacade implements ICompressionEngineFacade {
	private Map<CompressionMethod, ICompressionEngine> engines 
						= new HashMap<CompressionMethod, ICompressionEngine>();
	
	@Override
	public ICompressionEngine getEngine(CompressionMethod method) {
		if ( !this.isSupported(method) ) {
			throw new IllegalArgumentException("Requested method not supports");
		}
		
		return this.engines.get(method);
	}

	@Override
	public void registerEngine(CompressionMethod method,
			ICompressionEngine engine ) {
		this.engines.put(method, engine);
	}

	@Override
	public boolean isSupported(CompressionMethod method) {
		return this.engines.containsKey(method);
	}
	
}
