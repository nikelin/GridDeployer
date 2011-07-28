package com.api.deployer.backup.diff;

import com.api.deployer.system.scripts.IScriptExecutor;

/**
 * Delta-encoding script executors factory.
 * 
 * @author nikelin
 */
public interface IDiffEngine {
	
	public IScriptExecutor encode( String sourcePath, String targetPath, String patchPath );
	
	public IScriptExecutor encode( IScriptExecutor executor, String output );
	
	public IScriptExecutor decode( String sourcePath, String patchPath, String outputPath );
	
	public IScriptExecutor decode( IScriptExecutor executor, String output );
	
}
