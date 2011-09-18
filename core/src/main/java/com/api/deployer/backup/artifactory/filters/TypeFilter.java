package com.api.deployer.backup.artifactory.filters;

import com.api.deployer.backup.artifactory.artifacts.ArtifactType;
import com.api.deployer.backup.artifactory.artifacts.IArtifact;
import com.redshape.utils.IFilter;

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
