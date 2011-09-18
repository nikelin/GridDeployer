package com.api.deployer.backup.artifactory.filters;

import com.api.deployer.backup.artifactory.artifacts.IArtifact;
import com.redshape.utils.IFilter;

import java.util.UUID;

public class UUIDFilter implements IFilter<IArtifact> {
	private UUID id;
		
	public UUIDFilter( UUID id ) {
		this.id = id;
	}
	
	@Override
	public boolean filter( IArtifact artifact ) {
		return artifact.getId() != null 
			&& artifact.getId().equals( this.id );
	}
	
}
