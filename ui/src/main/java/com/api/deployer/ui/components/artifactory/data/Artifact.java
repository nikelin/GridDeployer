package com.api.deployer.ui.components.artifactory.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.api.deployer.backup.artifactory.artifacts.ArtifactType;
import com.redshape.ui.data.AbstractModelData;

public class Artifact extends AbstractModelData {

	public Artifact() {
		super();

		this.set( ArtifactModel.CHILDREN, new ArrayList<Artifact>() );
	}

	public List<Artifact> getChildren() {
		return this.get( ArtifactModel.CHILDREN );
	}

	public void setChildren( List<Artifact> children ) {
		this.set( ArtifactModel.CHILDREN, children );
	}

	public void removeChild( Artifact child ) {
		this.getChildren().remove( child );
	}

	public void addChild( Artifact child ) {
		this.getChildren().add( child );
	}

	public int getChildrenCount() {
		return this.getChildren().size();
	}

	public boolean hasChilds() {
		return !this.getChildren().isEmpty();
	}

	public void setId( UUID id ) {
		this.set( ArtifactModel.ID, id );
	}
	
	public UUID getId() {
		return this.get( ArtifactModel.ID );
	}
	
	public Integer getSize() {
		return this.get( ArtifactModel.SIZE );
	}
	
	public void setSize( Integer size ) {
		this.set( ArtifactModel.SIZE, size );
	}
	
	public void setName( String name ) {
		this.set( ArtifactModel.NAME, name );
	}
	
	public String getName() {
		return this.get( ArtifactModel.NAME );
	}
	
	public void setDescription( String description ) {
		this.set( ArtifactModel.DESCRIPTION, description );
	}
	
	public String getDescription() {
		return this.get( ArtifactModel.DESCRIPTION );
	}
	
	public void setHash( String hash ) {
		this.set( ArtifactModel.HASH, hash );
	}
	
	public String getHash() {
		return this.get( ArtifactModel.HASH );
	}
	
	public void setType( ArtifactType type ) {
		this.set( ArtifactModel.TYPE, type );
	}
	
	public ArtifactType getType() {
		return this.get( ArtifactModel.TYPE );
	}
	
	public void setDate( Date date ) {
		this.set( ArtifactModel.DATE, date );
	}
	
	public Date getDate() {
		return this.get( ArtifactModel.DATE );
	}
	
	@Override
	public String toString() {
		return this.getName();
	}

    @Override
    public boolean equals( Object object ) {
        if ( object instanceof Artifact && object != null ) {
            return this.getId().equals( ((Artifact) object).getId() );
        }

        return false;
    }
	
}
