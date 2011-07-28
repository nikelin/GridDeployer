package com.api.deployer.backup.artifactory.filters;

import java.util.UUID;

import com.api.commons.IFilter;
import com.api.deployer.backup.artifactory.artifacts.IArtifact;

public class ChildArtifactsFilter implements IFilter<IArtifact> {
	private UUID parentId;
	
	public ChildArtifactsFilter( UUID parentId ) {
		this.parentId = parentId;
	}
	
	@Override
	public boolean filter( IArtifact artifact ) {
		return artifact.getParent() != null
				&& artifact.getParent().equals( this.parentId );
	}
	
}
