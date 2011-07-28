package com.api.deployer.backup.artifactory.artifacts;

import java.util.UUID;

import com.api.commons.config.ConfigException;
import com.api.commons.config.IWritableConfig;
import com.api.commons.config.XMLConfig;
import com.api.deployer.backup.artifactory.ArtifactoryVersion;

public class ArtifactBuilder implements IArtifactBuilder {
	private ArtifactoryVersion version;
	private IArtifact context;
	
	public ArtifactBuilder( ArtifactoryVersion version ) {
		this(version, null);
	}
	
	public ArtifactBuilder( ArtifactoryVersion version, IArtifact artifact ) {
		this.version = version;
		this.context = artifact;
	}

	@Override
	public ArtifactoryVersion getVersion() {
		return this.version;
	}

	protected void configure( IArtifact artifact ) {
		if ( artifact.getParent() == null ) {
			artifact.setParent( this.context.getId() );
		}
	}
	
	@Override
	public IArtifact createArtifact( IWritableConfig config ) throws InstantiationException {
		ArtifactType type = ArtifactType.valueOf( config.attribute("type") );
		if ( type == null ) {
			throw new InstantiationException("Unknown artifact type being created");
		}
		
		return this.createArtifact( type, config );
	}
	
	@Override
	public IArtifact createArtifact(ArtifactType type) throws InstantiationException {
		return this.createArtifact( type, null );
	}
	
	@Override
	public IArtifact createArtifact(ArtifactType type, IWritableConfig config ) throws InstantiationException {
		try {
			config = config == null ? XMLConfig.createEmpty("artifact") 
									: config;
			
			IArtifact artifact = new Artifact( type, config );
			if ( config.attribute("id") != null && !config.attribute("id").isEmpty() ) {
				artifact.setId( UUID.fromString( config.attribute("id") ) );
			} else {
				artifact.setId( UUID.randomUUID() );
			}
			
			if ( !config.get("name").isNull() ) {
				artifact.setName( config.get("name").value() );
			}
			
			if ( !config.get("description").isNull() ) {
				artifact.setDescription( config.get("description").value() );
			}
			
			if ( !config.get("created").isNull() ) {
				artifact.setTimestamp( Long.valueOf( config.get("created").value() ) );
			}
			
			if ( !config.get("parent").isNull() ) {
				artifact.setParent( UUID.fromString( config.get("parent").value() ) );
			}
			
			if ( this.context != null ) {
				artifact.setParent( this.context.getId() );
				this.context.addChild( artifact );
			}
			
			return artifact;
		} catch ( ConfigException e  ) {
			throw new InstantiationException( e.getMessage() );
		}
	}
	
}
