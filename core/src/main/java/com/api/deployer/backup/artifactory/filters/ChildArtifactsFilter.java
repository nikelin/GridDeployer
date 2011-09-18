package com.api.deployer.backup.artifactory.filters;

import com.api.deployer.backup.artifactory.artifacts.IArtifact;
import com.redshape.utils.IFilter;

import java.util.UUID;

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
