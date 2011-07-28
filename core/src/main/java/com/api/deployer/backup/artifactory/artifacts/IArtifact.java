package com.api.deployer.backup.artifactory.artifacts;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

import com.api.commons.config.IWritableConfig;

public interface IArtifact extends Serializable {
	
	public void setId( UUID id );
	
	public UUID getId();

	public boolean isDirty();
	
	public void setParent( UUID parent );
	
	public UUID getParent();
	
	public void addChild( IArtifact artifact );
	
	public Collection<IArtifact> getChilds();
	
	public void setName( String name );
	
	public String getName();
	
	public void setDescription( String description );
	
	public String getDescription();
	
	public void setTimestamp( Long timestamp );
	
	public Long getTimestamp();
	
	public void setType( ArtifactType type );
	
	public ArtifactType getType();
	
	public void setData( IWritableConfig config );
	
	public IWritableConfig getData();
	
}
