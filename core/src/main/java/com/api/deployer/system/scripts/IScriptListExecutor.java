package com.api.deployer.system.scripts;

import java.util.Collection;

public interface IScriptListExecutor<T> extends IScriptExecutor {
	
	public void addScript( T script );
	
	public Collection<T> getScripts();
	
	public void setListStyle( ScriptListStyle style );
	
}
