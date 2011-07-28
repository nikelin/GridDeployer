package com.api.deployer.backup.artifactory;

import java.util.Map;

import com.api.deployer.backup.artifactory.artifacts.ArtifactType;
import com.api.deployer.backup.artifactory.artifacts.IArtifact;
import com.api.deployer.backup.artifactory.artifacts.IArtifactBuilder;
import com.api.deployer.backup.artifactory.artifacts.readers.IArtifactReader;
import com.api.deployer.backup.artifactory.artifacts.writers.IArtifactWriter;
import com.api.deployer.backup.artifactory.index.IArtifactoryIndex;
import com.api.deployer.backup.artifactory.index.IIndexReader;
import com.api.deployer.backup.artifactory.index.IIndexWriter;
import com.api.deployer.backup.artifactory.index.LockedException;

public interface IArtifactoryFacade {

    public void flushIndex( String path, IArtifactoryIndex index) throws WriterException, LockedException;

	public void registerIndex( IArtifactoryIndex index );

	/**
	 * Create artifacts builder instance with given @see context as root artifact
	 * @param context
	 * @return
	 */
	public IArtifactBuilder createArtifactBuilder( IArtifact context );
	
	/**
	 * Create artifacts builder instance without root context
	 * @return
	 */
	public IArtifactBuilder createArtifactBuilder();
	
	/**
	 * Change current index marshalling and update entity
	 * @param writer
	 */
	public void setIndexWriter( IIndexWriter writer );
	
	/**
	 * @return
	 */
	public IIndexWriter getIndexWriter();
	
	/**
	 * Change current index unmarshalling entity
	 * @param reader
	 */
	public void setIndexReader( IIndexReader reader );
	
	/**
	 * @return
	 */
	public IIndexReader getIndexReader();
	
	/**
	 * Spring helper for writers init
	 * @param writers
	 */
	public void setWriters( Map<ArtifactType, Class<? extends IArtifactWriter>> writers );
	
	/**
	 * Spring helper for readers init
	 * @param readers
	 */
	public void setReaders( Map<ArtifactType, Class<? extends IArtifactReader>> readers );
	
	/**
	 * Find artifacts writer class type related to given @see type
	 * @param <V>
	 * @param type
	 * @return
	 */
	public <V extends IArtifactWriter> Class<V> findWriter( ArtifactType type );
	
	/**
	 * Find artifacts reader class type related to given @see type
	 * @param <V>
	 * @param type
	 * @return
	 */
	public <V extends IArtifactReader> Class<V> findReader( ArtifactType type );
	
	/**
	 * Register new writer class @see writer and associate it with given @see type
	 * @param type
	 * @param writer
	 */
	public void registerWriter( ArtifactType type, Class<? extends IArtifactWriter> writer );
	
	/**
	 * Register new reader class @see reader and associate it with given @see type
	 * @param type
	 * @param reader
	 */
	public void registerReader( ArtifactType type, Class<? extends IArtifactReader> reader );
	
	/**
	 * Create new writer instance for given @see artifact
	 * @param <V>
	 * @param artifact
	 * @return
	 * @throws InstantiationException
	 */
	public <V extends IArtifactWriter> V createArtifactWriter( IArtifact artifact )
		throws InstantiationException;
	
	/**
	 * Create new reader instance for given @see artifact
	 * @param <V>
	 * @param artifact
	 * @return
	 * @throws InstantiationException
	 */
	public <V extends IArtifactReader> V createArtifactReader( IArtifact artifact ) 
		throws InstantiationException;
	
}
