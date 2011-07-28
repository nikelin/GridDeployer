package com.api.deployer.system.scripts;

import java.io.IOException;

/**
 * @author nikelin
 */
public interface IScriptExecutor {
	
	public void kill();
	
	public boolean isSuccess();
	
	public boolean isFinished();
	
	public String getExecutionResult();
	
	public String getExecutionErrors();
	
	public String execute() throws IOException;
	
	public String getExecCommand();
	
	public IScriptExecutor addUnnamedParameter( Object value );
	
	public IScriptExecutor setParameter( String name, Object value );
	
	public boolean hasDeclaredInputSource();
	
	public IScriptExecutor declareInputSource( IScriptExecutor executor );
	
	public IScriptExecutor declareInputSource( String source );
	
	public boolean hasDeclaredOutputSource();
	
	public IScriptExecutor declareOutputSource( IScriptExecutor executor );
	
	public IScriptExecutor declareOutputSource( String source );
	
	public Integer getProgress() throws IOException;
	
	public IScriptExecutor setHandler( IScriptExecutionHandler handler );
	
}
