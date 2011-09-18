package com.api.deployer.backup.artifactory.artifacts;

import com.api.deployer.backup.artifactory.ArtifactoryVersion;
import com.redshape.utils.config.IWritableConfig;

public interface IArtifactBuilder {
	
	public ArtifactoryVersion getVersion();
	
	public IArtifact createArtifact( IWritableConfig config )
		throws InstantiationException;
	
	public IArtifact createArtifact( ArtifactType type )
		throws InstantiationException;
	
	public IArtifact createArtifact(ArtifactType type, IWritableConfig config ) 
		throws InstantiationException;
	
}
