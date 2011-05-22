package com.api.deployer.system.console;

import com.api.deployer.system.scripts.IScriptExecutor;
import com.api.deployer.system.scripts.IScriptListExecutor;
import com.api.deployer.system.linux.scripts.bash.BashScriptExecutor;
import com.api.deployer.system.linux.scripts.bash.BashScriptListExecutor;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author nikelin
 */
public class Console implements IConsole {
	private Map<Object, Collection<IScriptExecutor>> executors = new HashMap<Object, Collection<IScriptExecutor>>();
	
	/**
	 * For testability purpouses
	 * @param command
	 * @return
	 */
	protected IScriptExecutor createExecutorObject( String command ) {
		return new BashScriptExecutor(command);
	}
	
	/**
	 * For testability purpouses
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T extends IScriptExecutor> IScriptListExecutor<T> createListExecutorObject() {
		return (IScriptListExecutor<T>) new BashScriptListExecutor();
	}
	
	@Override
	public IScriptExecutor createExecutor(String command) {
		return this.createExecutor(this, command);
	}

	@Override
	public <T extends IScriptExecutor> IScriptListExecutor<T> createListExecutor() {
		return this.createListExecutor(this);
	}

	@Override
	public void stopScripts(Object context) {
		Collection<IScriptExecutor> executors = this.executors.get(context);
		if ( executors == null ) {
			return;
		}
		
		for ( IScriptExecutor executor : executors ) {
			executor.kill();
		}
	}

	@Override
	public IScriptExecutor createExecutor(Object context, String command) {
		IScriptExecutor executor = this.createExecutorObject(command);
		
		this.registerExecutor( context, executor );
		
		return executor;
	}
	
	@Override
	public <T extends IScriptExecutor> IScriptListExecutor<T> 
						createListExecutor( Object context) {
		IScriptListExecutor<T> executor = this.createListExecutorObject();
		
		this.registerExecutor( context, executor );
		
		return executor;
	}
	
	protected void registerExecutor( Object context, IScriptExecutor executor ) {
		if ( this.executors.get(context) == null ) {
			this.executors.put( context, new HashSet<IScriptExecutor>() );
		}
		
		this.executors.get(context).add( executor );
	}

}
