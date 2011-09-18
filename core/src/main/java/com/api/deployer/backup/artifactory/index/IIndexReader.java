package com.api.deployer.backup.artifactory.index;

import com.api.deployer.backup.artifactory.IArtifactoryFacade;
import com.api.deployer.backup.artifactory.ReaderException;
import com.redshape.utils.config.IWritableConfig;

public interface IIndexReader {
	
	/**
	 * Change artifactory facade context
	 * 
	 * @param context
	 */
	public void setArtifactoryFacade( IArtifactoryFacade context );
	
	public IArtifactoryIndex readIndex( String path ) throws ReaderException, IndexException;
	
	public IArtifactoryIndex readIndex( IWritableConfig config ) throws ReaderException, IndexException;
	
}
