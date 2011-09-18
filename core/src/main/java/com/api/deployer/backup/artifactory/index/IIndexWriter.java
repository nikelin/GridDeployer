package com.api.deployer.backup.artifactory.index;

import com.api.deployer.backup.artifactory.IArtifactoryFacade;
import com.api.deployer.backup.artifactory.WriterException;
import com.redshape.utils.config.IConfig;

/**
 * Responsible for transforming artifactory index structure to XML
 * and for flushing including all related security checks (locks, etc.).
 * 
 * @author nikelin
 */
public interface IIndexWriter {

	/**
	 * Change artifactory facade context
	 * 
	 * @param context
	 */
	public void setArtifactoryFacade( IArtifactoryFacade context );
	
	/**
	 * Create XML index representation
	 * 
	 * @param index
	 * @return
	 * @throws WriterException
	 */
	public IConfig writeIndex( IArtifactoryIndex index ) throws WriterException;

	/**
	 * Update index and update to disk
	 * 
	 * @param path
	 * @param index
	 * @throws WriterException
	 * @throws LockedException
	 */
	public void flushIndex( String path, IArtifactoryIndex index ) throws WriterException, LockedException;
	
}
