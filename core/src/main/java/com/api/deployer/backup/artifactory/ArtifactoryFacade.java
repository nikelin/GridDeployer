package com.api.deployer.backup.artifactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.api.deployer.backup.artifactory.artifacts.ArtifactBuilder;
import com.api.deployer.backup.artifactory.artifacts.ArtifactType;
import com.api.deployer.backup.artifactory.artifacts.IArtifact;
import com.api.deployer.backup.artifactory.artifacts.IArtifactBuilder;
import com.api.deployer.backup.artifactory.artifacts.readers.IArtifactReader;
import com.api.deployer.backup.artifactory.artifacts.writers.IArtifactWriter;

import com.api.deployer.backup.artifactory.index.IArtifactoryIndex;
import com.api.deployer.backup.artifactory.index.IIndexReader;
import com.api.deployer.backup.artifactory.index.IIndexWriter;
import com.api.deployer.backup.artifactory.index.LockedException;

public class ArtifactoryFacade implements IArtifactoryFacade {
	private Map<ArtifactType, Class<? extends IArtifactReader>> readers 
					= new HashMap<ArtifactType, Class<? extends IArtifactReader>>();
	private Map<ArtifactType, Class<? extends IArtifactWriter>> writers
					= new HashMap<ArtifactType, Class<? extends IArtifactWriter>>();

	private Collection<IArtifactoryIndex> indexes = new HashSet<IArtifactoryIndex>();

	private IIndexReader indexReader;
	private IIndexWriter indexWriter;
	
	@Override
	public IArtifactBuilder createArtifactBuilder() {
		return new ArtifactBuilder( ArtifactoryVersion.Current );
	}
	
	@Override
	public IArtifactBuilder createArtifactBuilder( IArtifact context ) {
		return new ArtifactBuilder( ArtifactoryVersion.Current, context );
	}
	
	@Override
	public void setReaders( Map<ArtifactType, Class<? extends IArtifactReader>> readers ) {
		this.readers = readers;
	}
	
	@Override
	public void setWriters( Map<ArtifactType, Class<? extends IArtifactWriter>> writers ) {
		this.writers = writers;
	}

    @Override
    public void flushIndex( String path, IArtifactoryIndex index) throws WriterException, LockedException {
        this.getIndexWriter().flushIndex( path, index );
    }

    @SuppressWarnings("unchecked")
	@Override
	public <V extends IArtifactReader> Class<V> findReader( ArtifactType type ) {
		return (Class<V>) this.readers.get(type);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <V extends IArtifactWriter> Class<V> findWriter( ArtifactType type ) {
		return (Class<V>) this.writers.get(type);
	}

	@Override
	public void registerIndex( IArtifactoryIndex index ) {
		this.indexes.add(index);
	}

	@Override
	public void registerWriter( ArtifactType type, Class<? extends IArtifactWriter> writer ) {
		this.writers.put(type, writer);
	}
	
	@Override
	public void registerReader( ArtifactType type, Class<? extends IArtifactReader> reader ) {
		this.readers.put(type, reader);
	}

	@Override
	public void setIndexReader( IIndexReader reader ) {
		reader.setArtifactoryFacade(this);
		this.indexReader = reader;
	}
	
	@Override
	public IIndexReader getIndexReader() {
		return this.indexReader;
	}
	
	@Override
	public void setIndexWriter( IIndexWriter writer ) {
		writer.setArtifactoryFacade(this);
		this.indexWriter = writer;
	}
	
	@Override
	public IIndexWriter getIndexWriter() {
		return this.indexWriter;
	}
	
	@Override
	public <V extends IArtifactWriter> V createArtifactWriter(IArtifact artifact)
			throws InstantiationException {
		Class<V> writerClazz = this.findWriter( artifact.getType() );
		if ( writerClazz == null ) {
			throw new InstantiationException("Writer not found for type " + artifact.getType().name() );
		}

		try {
			V reader = writerClazz.getConstructor( IArtifact.class )
								  .newInstance( artifact );

			return reader;
		} catch ( InvocationTargetException e ) {
			throw new InstantiationException( e.getCause().getMessage() );
		} catch ( Throwable e  ) {
			throw new InstantiationException( e.getMessage() );
		}
	}
	
	@Override
	public <V extends IArtifactReader> V createArtifactReader(IArtifact artifact) 
			throws InstantiationException {
		Class<V> readerClazz = this.findReader( artifact.getType() );
		if ( readerClazz == null ) {
			throw new InstantiationException("Reader not found for type " + artifact.getType().name() );
		}

		try {
			V reader = readerClazz.getConstructor( IArtifact.class )
								  .newInstance( artifact );

			return reader;
		} catch ( InvocationTargetException e ) {
			throw new InstantiationException( e.getCause().getMessage() );
		} catch ( Throwable e  ) {
			throw new InstantiationException( e.getMessage() );
		}
	}
	
}
