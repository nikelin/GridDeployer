package com.api.deployer.system.console;

import com.api.deployer.system.scripts.IScriptExecutor;
import com.api.deployer.system.scripts.IScriptListExecutor;

/**
 * @author nikelin
 */
public interface IConsole {

	public void stopScripts( Object context );
	
    public IScriptExecutor createExecutor( String command );
    
    public IScriptExecutor 
    	createExecutor( Object context, String command );
    
    public <T extends IScriptExecutor> IScriptListExecutor<T> 
    	createListExecutor();
    
    public <T extends IScriptExecutor> IScriptListExecutor<T> 
    	createListExecutor( Object context );
    
}
