package com.api.deployer.backup.artifactory.filters;

import com.api.commons.IFilter;
import com.api.deployer.backup.artifactory.artifacts.ArtifactType;
import com.api.deployer.backup.artifactory.artifacts.IArtifact;

public class TypeFilter implements IFilter<IArtifact> {
	private ArtifactType type;
	
	public TypeFilter( ArtifactType type ) {
		this.type = type;
	}
	
	@Override
	public boolean filter( IArtifact subject ) {
		return subject.getType() != null
			&& subject.getType().equals( this.type );
	}
	
}
