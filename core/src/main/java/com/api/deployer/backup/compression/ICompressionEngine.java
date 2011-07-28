package com.api.deployer.backup.compression;

import com.api.deployer.system.scripts.IScriptExecutor;

/**
 * Compression script executors factory
 * 
 * @author nikelin
 */
public interface ICompressionEngine {
	
	/**
	 * Create script executor responsible for compression 
	 * with @see level of compression which take @see inputSource 
	 * file as source and @see outputSource file as output path.
	 * 
	 * @param inputSource
	 * @param outputSource
	 * @param level
	 * @return
	 */
	public IScriptExecutor compress(String inputSource, 
			String outputSource, CompressionLevel level );
	
	/**
	 * Create script executor responsible for compression
	 * with another @see executor result as input source of compression.
	 * 
	 * @param executor
	 * @param outputSource
	 * @param level
	 * @return
	 */
	public IScriptExecutor compress( IScriptExecutor executor, 
			String outputSource, CompressionLevel level );
	
	/**
	 * Create script executor responsible for decompression with 
	 * level @see level which takes @see inputSource file as input data 
	 * and @see outputSource as output path.
	 * 
	 * @param inputSource
	 * @param outputSource
	 * @param level
	 * @return
	 */
	public IScriptExecutor decompress( String inputSource, 
			String outputSource, CompressionLevel level );
	
	/**
	 * Create script executor responsible for decompression
	 * with another @see executor result as input source of decompression.
	 * @param executor
	 * @param outputSource
	 * @param level
	 * @return
	 */
	public IScriptExecutor decompress( IScriptExecutor executor, 
			String outputSource, CompressionLevel level );
	
}
