package com.api.deployer.backup.artifactory.index;

import com.api.deployer.backup.artifactory.ArtifactoryVersion;
import com.api.deployer.backup.artifactory.artifacts.IArtifact;
import com.redshape.utils.IFilter;

import java.util.Collection;

public interface IArtifactoryIndex {
	
	public void setVersion( ArtifactoryVersion version );
	
	public ArtifactoryVersion getVersion();
	
	public void setLastUpdate( Long timestamp );
	
	public Long getLastUpdate();
	
	public boolean isDirty();

	public void removeArtifact( IFilter<IArtifact> filter ) throws IndexException;

	public void removeArtifact( IArtifact artifact ) throws IndexException;
	
	public void addArtifact( IArtifact artifact ) throws IndexException;
	
	public Collection<IArtifact> getArtifacts();
	
	public IArtifact getArtifact( IFilter<IArtifact> filter );
	
	public Collection<IArtifact> getArtifacts( IFilter<IArtifact> filter );
	
}
