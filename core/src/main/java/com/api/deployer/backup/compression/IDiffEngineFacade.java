package com.api.deployer.backup.compression;

import com.api.deployer.backup.diff.DiffMethod;
import com.api.deployer.backup.diff.IDiffEngine;

public interface IDiffEngineFacade {
	
	public IDiffEngine getEngine( DiffMethod method );
	
	public void registerEngine( DiffMethod method, IDiffEngine engine );
	
	public boolean isSupported( DiffMethod method );
	
}
