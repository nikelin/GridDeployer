package com.api.deployer.backup.artifactory.index;

import java.io.File;

import com.api.commons.XMLHelper;
import com.api.commons.config.ConfigException;
import com.api.commons.config.IWritableConfig;
import com.api.commons.config.IConfig;
import com.api.commons.config.XMLConfig;
import com.api.deployer.backup.artifactory.ArtifactoryVersion;
import com.api.deployer.backup.artifactory.IArtifactoryFacade;
import com.api.deployer.backup.artifactory.ReaderException;
import com.api.deployer.backup.artifactory.artifacts.IArtifact;

public class IndexReader implements IIndexReader {
	private IArtifactoryFacade artifactoryFacade;
	
	public IndexReader() {
		this(null);
	}
	
	public IndexReader( IArtifactoryFacade facade ) {
		this.artifactoryFacade = facade;
	}
	
	@Override
	public void setArtifactoryFacade( IArtifactoryFacade context ) {
		this.artifactoryFacade = context;
	}
	
	protected IArtifactoryFacade getArtifactoryFacade() {
		return this.artifactoryFacade;
	}
	
	@Override
	public IArtifactoryIndex readIndex(String path) throws ReaderException, IndexException {
		try {
			return this.readIndex( new XMLConfig( new XMLHelper(), new File(path) ) );
		} catch ( ConfigException e  ) {
			throw new ReaderException( e.getMessage(), e );
		}
	}

	@Override
	public IArtifactoryIndex readIndex(IWritableConfig config) throws ReaderException, IndexException {
		try {
			IArtifactoryIndex index = new ArtifactoryIndex();
			
			ArtifactoryVersion version = ArtifactoryVersion.valueOf( config.get("version").value() );
			if ( version == null ) {
				throw new ReaderException("Unsupported index version");
			}
			
			index.setVersion( version );
			IConfig timestampNode = config.get("timestamp");
			if ( !timestampNode.isNull() ) {
				index.setLastUpdate( Long.valueOf( timestampNode.value() ) );
			}
			
			for ( IConfig node : config.get("artifacts").childs() ) {
				IArtifact artifact = this.getArtifactoryFacade().createArtifactBuilder()
										   .createArtifact( (IWritableConfig) node );
				index.addArtifact(artifact);
			}

			this.getArtifactoryFacade().registerIndex( index );
			
			return index;
		} catch ( InstantiationException e ) {
			throw new ReaderException("Index data corrupted", e);
		} catch ( ConfigException e  ) {
			throw new ReaderException("Index processing exception", e);
		} catch ( NumberFormatException e ) {
			throw new ReaderException("Index data or structure corrupted", e);
		}
	}

}
