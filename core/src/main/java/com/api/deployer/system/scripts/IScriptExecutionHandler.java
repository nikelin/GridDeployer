package com.api.deployer.system.scripts;

import com.api.deployer.system.processes.ISystemProcess;

import java.io.IOException;

public interface IScriptExecutionHandler {
	
	public Integer onProgressRequested( ISystemProcess process ) 
		throws IOException;
	
	public boolean onExit( String output );
	
	public void onError( String output );

}