package com.api.deployer.backup.artifactory.filters;

import java.util.UUID;

import com.api.commons.IFilter;
import com.api.deployer.backup.artifactory.artifacts.IArtifact;

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
