package com.api.deployer.backup.compression;

import com.api.deployer.system.console.IConsole;
import com.api.deployer.system.scripts.IScriptExecutor;

public class Bzip2CompressionEngine implements ICompressionEngine {
	private IConsole console;
	
	public Bzip2CompressionEngine( IConsole console ) {
		this.console = console;
	}
	
	protected IConsole getConsole() {
		return this.console;
	}
	
	@Override
	public IScriptExecutor compress(String sourcePath, String outputPath,
			CompressionLevel level) {
		IScriptExecutor executor = this.createExecutor( level );
		
		executor.addUnnamedParameter("-c")
				.addUnnamedParameter("-z")
				.declareInputSource( sourcePath )
				.declareOutputSource( outputPath );
		
		return executor;
	}

	@Override
	public IScriptExecutor compress(IScriptExecutor executor, String outputSource, CompressionLevel level ) {
		IScriptExecutor compressionExecutor = this.createExecutor( level );
		compressionExecutor.declareInputSource( executor );
		compressionExecutor.declareOutputSource( outputSource );
		
		return compressionExecutor;
	}
	
	@Override
	public IScriptExecutor decompress(String sourcePath, String outputPath,
			CompressionLevel level) {
		IScriptExecutor executor = this.createExecutor( level );
		
		executor.addUnnamedParameter("-c")
				.addUnnamedParameter("-d")
				.declareInputSource( sourcePath )
				.declareOutputSource( outputPath );
		
		return executor;
	}

	@Override
	public IScriptExecutor decompress(IScriptExecutor executor, String outputSource, CompressionLevel level ) {
		IScriptExecutor compressionExecutor = this.createExecutor(level);
		compressionExecutor.declareInputSource( executor );
		compressionExecutor.declareOutputSource( outputSource );
		
		return compressionExecutor;
	}
	
	protected String formatCompressionLevel( CompressionLevel level ) {
		String result;
		switch( level ) {
		case HIGH:
			result = "9";
		break;
		case NORMAL:
			result = "5";
		break;
		case LOW:
			result = "1";
		break;
		default:
			result = "9";
		break;
		}
		
		return "-" + result;
	}
	
	protected IScriptExecutor createExecutor( CompressionLevel level ) {
		IScriptExecutor executor =  this.getConsole().createExecutor( this, "bzip2" );
		executor.addUnnamedParameter( this.formatCompressionLevel(level) );
		
		return executor;
	}

}
