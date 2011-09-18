package com.api.deployer.system.scanners.mock;

import com.api.deployer.system.console.Console;
import com.api.deployer.system.linux.scripts.bash.BashScriptExecutor;
import com.api.deployer.system.scripts.IScriptExecutor;
import com.redshape.utils.config.ConfigException;
import com.redshape.utils.config.IConfig;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: semichevskiy
 * Date: 2/28/11
 * Time: 2:38 PM
 */
public class MockConsole extends Console {
	private static final Logger log = Logger.getLogger( MockConsole.class );
	private IConfig config;
	
    public static class MockScript extends BashScriptExecutor {
    	private MockConsole context;
    	
    	public MockScript( MockConsole context, String command ) {
    		super(command);
    		
    		this.context = context;
    	}
    	
    	
    	@Override
    	public String execute() throws IOException {
    		try {
    			log.info( this.getExecCommand() );
    			
    			this.markFinished(true);
    			this.markSuccessful(true);
    			
    			return this.context.findAnswerByCommand( this.getExecCommand() );
    		} catch ( ConfigException e ) {
    			throw new IOException( e.getMessage(), e );
    		}
    	}
    }
    
    public MockConsole( IConfig config ) {
    	this.config = config;
    }
    
    @Override
    protected IScriptExecutor createExecutorObject( String command ) {
    	return new MockScript(this, command);
    }

    private String findAnswerByCommand( String command ) throws ConfigException {
        String result = null;
        log.info("Looking for command `" + command + "` result");
        for ( IConfig commandNode : this.config.childs() ) {
        	log.info("Testing: " + commandNode.get("value").value() );
        	if ( commandNode.get("value").value().trim().equals( command.trim() ) ) {
        		return commandNode.get("result").value();
        	}
        }
        return result;
    }

}
