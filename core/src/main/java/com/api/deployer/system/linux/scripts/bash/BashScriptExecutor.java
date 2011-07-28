package com.api.deployer.system.linux.scripts.bash;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.api.deployer.system.processes.ISystemProcess;
import com.api.deployer.system.processes.SystemProcess;
import com.api.deployer.system.scripts.IScriptExecutionHandler;
import com.api.deployer.system.scripts.IScriptExecutor;

public class BashScriptExecutor implements IScriptExecutor {
	private static final Logger log = Logger.getLogger( BashScriptExecutor.class );
	
	private String command;
	private Map<String, Object> parameters = new LinkedHashMap<String, Object>();
	private List<Object> unnamedParameters = new ArrayList<Object>();
	private Object inputSource;
	private Object outputSource;
	private boolean finished;
	private boolean successful;
	private String output;
	private String errors;
	private ISystemProcess process;
	private IScriptExecutionHandler handler;
	
	protected BashScriptExecutor() {
		this(null);
	}
	
	public BashScriptExecutor( String command ) {
		this.command = command;
	}
	
	protected void markFinished( boolean value ) {
		this.finished = value;
	}
	
	protected void markSuccessful( boolean value ) {
		this.successful= value;
	}
	
	protected String prepareSource( Object source ) {
		if ( source instanceof String ) {
			return (String) source;
		} else if ( source instanceof IScriptExecutor ) {
			return ( (IScriptExecutor) source ).getExecCommand();
		} else {
			throw new IllegalArgumentException("Invalid I/O source object passed to executor");
		}
	}
	
	@Override
	public synchronized String execute() throws IOException {
		String command = this.getExecCommand().toString();
		log.info(command);
		
		this.process = new SystemProcess( 
			Runtime.getRuntime().exec( command )
		);
		
		try {
			this.process.waitFor();
		} catch (InterruptedException e) {}
		
		String processStdInput = this.process.readStdInput();
		String processStdErr = this.process.readStdError();
		
		boolean successful = processStdErr.isEmpty();
		if ( this.handler != null ) {
			if ( processStdErr.isEmpty() ) {
				successful = this.handler.onExit( processStdInput );
			} else {
				this.handler.onError( processStdErr );
			}
		}
		
		this.finished = true;
		this.successful = successful;
		
		return processStdInput;
	}
	
	public Integer getProgress() throws IOException {
		if ( null == this.handler ) {
			return 0;
		}
		
		return this.handler.onProgressRequested( this.process );
	}
	
	@Override
	public String getExecCommand() {
		StringBuilder builder = new StringBuilder();
		boolean bothIO = false;
		if ( this.hasDeclaredInputSource() && this.hasDeclaredOutputSource() ) {
			bothIO = true;
		}
		
		if ( bothIO ) { 
			builder.append("("); 
		}
		
		builder.append( this.command )
			   .append( " " );
		
		for ( String parameter : this.parameters.keySet() ) {
			builder.append( parameter )
				   .append("=")
				   .append( String.valueOf( this.parameters.get(parameter) ) )
				   .append(" ");
		}
		
		for ( Object parameter : this.unnamedParameters ) {
			builder.append( String.valueOf( parameter ) )
				   .append( " " );
		}
		
		if ( this.hasDeclaredInputSource() ) {
			builder.append(" ")
				   .append("<")
				   .append(" ");
			
			// @author nikelin 
			// TODO: refactor with generification of inputsource from Object
			// to IScriptExecutorSource which will be return destination on *.toString()
			// invokation.
			builder.append( this.prepareSource( this.inputSource ) );
		}
		
		if ( bothIO ) {
			builder.append(")");
		}
		
		if ( this.hasDeclaredOutputSource() ) {
			builder.append(" ")
				   .append(">")
				   .append(" ")
				   .append( this.prepareSource( this.inputSource ) );
		}
		
		return builder.toString();
	}

	@Override
	public void kill() {
		if ( this.process != null ) {
			log.info("Script " + this.getExecCommand() + " has been killed...");
			this.process.destroy();
		}
	}
	
	@Override
	public boolean isFinished() {
		return this.finished;
	}
	
	@Override
	public IScriptExecutor addUnnamedParameter( Object value ) {
		this.unnamedParameters.add(value);
		return this;
	}
	
	@Override
	public IScriptExecutor setParameter(String name, Object value) {
		this.parameters.put(name, value);
		return this;
	}

	@Override
	public IScriptExecutor declareInputSource( IScriptExecutor executor ) {
		this.inputSource = executor;
		return this;
	}
	
	@Override
	public IScriptExecutor declareInputSource(String source) {
		this.inputSource = source;
		return this;
	}

	@Override
	public IScriptExecutor declareOutputSource( IScriptExecutor executor ) {
		this.outputSource = executor;
		return this;
	}
	
	@Override
	public IScriptExecutor declareOutputSource(String source) {
		this.outputSource = source;
		return this;
	}
	
	@Override
	public IScriptExecutor setHandler(IScriptExecutionHandler handler) {
		this.handler = handler;
		return this;
	}

	@Override
	public String getExecutionResult() {
		return this.output;
	}
	
	@Override
	public String getExecutionErrors() {
		return this.errors;
	}

	@Override
	public boolean hasDeclaredInputSource() {
		return this.inputSource != null;
	}

	@Override
	public boolean hasDeclaredOutputSource() {
		return this.outputSource != null;
	}
	
	@Override
	public boolean isSuccess() {
		if ( !this.isFinished() ) {
			throw new IllegalAccessError("Method is only invokable after execute() has been processed");
		}
		
		return this.successful;
	}
	
}
