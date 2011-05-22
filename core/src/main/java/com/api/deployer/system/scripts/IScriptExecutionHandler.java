package com.api.deployer.system.scripts;

import java.io.IOException;

import com.api.deployer.system.processes.ISystemProcess;

public interface IScriptExecutionHandler {
	
	public Integer onProgressRequested( ISystemProcess process ) 
		throws IOException;
	
	public boolean onExit( String output );
	
	public void onError( String output );

}
