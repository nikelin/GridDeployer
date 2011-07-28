package com.api.deployer.backup.artifactory.index;

import java.util.*;

import com.api.commons.IFilter;
import com.api.deployer.backup.artifactory.filters.UUIDFilter;
import com.api.deployer.backup.artifactory.ArtifactoryVersion;
import com.api.deployer.backup.artifactory.artifacts.IArtifact;

public class ArtifactoryIndex implements IArtifactoryIndex {
	private ArtifactoryVersion version;
	private Long lastUpdate;
	private Boolean dirty;
	private Map<UUID, IArtifact> artifacts = new HashMap<UUID, IArtifact>();

	private Object lock = new Object();

	@Override
	public void setVersion(ArtifactoryVersion version) {
		this.version = version;
	}

	@Override
	public ArtifactoryVersion getVersion() {
		return this.version;
	}

	@Override
	public void setLastUpdate( Long lastUpdate ) {
		this.lastUpdate = lastUpdate;
	}
	
	@Override
	public Long getLastUpdate() {
		return this.lastUpdate;
	}

	protected void markDirty( boolean value ) {
		this.dirty = value;
	}

	@Override
	public boolean isDirty() {
		if ( this.dirty ) {
			return true;
		}

		Collection<IArtifact> artifacts;
		synchronized (lock) {
			artifacts = this.artifacts.values();
		}

		for ( IArtifact artifact : artifacts ) {
			if ( artifact.isDirty() ) {
				this.dirty = true;
				return true;
			}
		}

		return false;
	}

	@Override
	public void removeArtifact( IFilter<IArtifact> filter ) {
		synchronized (this.lock) {
			for ( Iterator<IArtifact> iterator = this.getArtifacts(filter).iterator(); iterator.hasNext(); ) {
				this.removeArtifact( iterator.next() );
			}
		}
	}

	@Override
	public void removeArtifact(IArtifact artifact) {
		synchronized (this.lock) {
			this.artifacts.remove(artifact);
			this.markDirty(true);
		}
	}

	@Override
	public void addArtifact(IArtifact artifact) throws IndexException {
		this.markDirty(true);

		this.processParent(artifact);

		synchronized (lock) {
			this.artifacts.put(artifact.getId(), artifact);
		}

		if ( artifact.getChilds().size() > 0 ) {
			this.processChilds(artifact);
		}
	}
	
	private void processParent( IArtifact artifact ) throws IndexException {
		if ( artifact.getParent() != null ) {
			final IArtifact parent = this.getArtifact( new UUIDFilter( artifact.getParent() ) );
			if ( parent == null ) {
				throw new IndexException("Artifact parent not present in index");
			}
		}
	}
	
	private void processChilds( IArtifact artifact ) throws IndexException {
		for ( IArtifact childArtifact : artifact.getChilds() ) {
			childArtifact.setParent( artifact.getParent() );
			this.addArtifact(childArtifact);
		}
	}

	@Override
	public Collection<IArtifact> getArtifacts() {
		synchronized (this.lock) {
			final Collection<IArtifact> artifacts = new HashSet<IArtifact>( this.artifacts.values().size() );
			artifacts.addAll( this.artifacts.values() );
			return artifacts;
		}
	}
	
	@Override
	public IArtifact getArtifact( IFilter<IArtifact> filter ) {
		final Collection<IArtifact> artifacts = this.getArtifacts(filter);
		if ( artifacts.size() > 0 ) {
			return artifacts.iterator().next();
		}
		
		return null;
	}

	@Override
	public Collection<IArtifact> getArtifacts(IFilter<IArtifact> filter) {
		final Collection<IArtifact> artifacts = new HashSet<IArtifact>();
		for ( IArtifact artifact : this.getArtifacts() ) {
			if ( filter.filter(artifact) ) {
				artifacts.add(artifact);
			}
		}
		
		return artifacts;
	}
	
}
