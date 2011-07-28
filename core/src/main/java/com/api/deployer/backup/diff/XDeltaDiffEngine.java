package com.api.deployer.backup.diff;

import com.api.deployer.system.console.IConsole;
import com.api.deployer.system.scripts.IScriptExecutor;

public class XDeltaDiffEngine implements IDiffEngine {
	private IConsole console;
	
	public XDeltaDiffEngine( IConsole console ) {
		this.console = console;
	}
	
	protected IConsole getConsole() {
		return this.console;
	}
	
	@Override
	public IScriptExecutor encode(String sourcePath, String targetPath,
			String patchPath) {
		IScriptExecutor executor = this.createEncodeExecutor();
		
		executor.addUnnamedParameter("-s")
				.addUnnamedParameter( sourcePath )
		   	    .addUnnamedParameter( targetPath )
		     	.addUnnamedParameter( patchPath );
		
		return executor;
	}

	@Override
	public IScriptExecutor encode( IScriptExecutor executor, String outputPath ) {
		IScriptExecutor deltaExecutor = this.createDecodeExecutor();
		
		deltaExecutor.declareInputSource( executor );
		deltaExecutor.declareOutputSource( outputPath );
		
		return deltaExecutor;
	}

	@Override
	public IScriptExecutor decode(String sourcePath, String patchPath, String outputPath ) {
		IScriptExecutor deltaExecutor = this.createEncodeExecutor();
		
		deltaExecutor.addUnnamedParameter("-s")
					 .addUnnamedParameter( sourcePath )
					 .addUnnamedParameter( patchPath )
					 .addUnnamedParameter( outputPath );
		
		return deltaExecutor;
	}

	@Override
	public IScriptExecutor decode(IScriptExecutor executor, String output ) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected IScriptExecutor createEncodeExecutor() {
		return this.getConsole().createExecutor( this, "xdelta3" )
								.addUnnamedParameter("-e");
	}
	
	protected IScriptExecutor createDecodeExecutor() {
		return this.getConsole().createExecutor( this, "xdelta3")
								.addUnnamedParameter("-d");
	}
	
}
