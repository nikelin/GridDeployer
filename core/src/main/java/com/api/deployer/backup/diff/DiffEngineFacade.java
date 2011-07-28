package com.api.deployer.backup.diff;

import java.util.HashMap;
import java.util.Map;

import com.api.deployer.backup.compression.IDiffEngineFacade;

public class DiffEngineFacade implements IDiffEngineFacade {
	private Map<DiffMethod, IDiffEngine> engines = new HashMap<DiffMethod, IDiffEngine>();
	
	@Override
	public IDiffEngine getEngine(DiffMethod method) {
		if ( !this.isSupported(method) ) {
			throw new IllegalArgumentException("Requested method not supports");
		}
		
		return this.engines.get(method);
	}

	@Override
	public void registerEngine(DiffMethod method, IDiffEngine engine) {
		this.engines.put(method, engine);
	}

	@Override
	public boolean isSupported(DiffMethod method) {
		return this.engines.containsKey(method);
	}

}
