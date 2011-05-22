package com.api.deployer.backup.artifactory.artifacts;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import com.api.commons.config.IWritableConfig;

public class Artifact implements IArtifact {
	private static final long serialVersionUID = -429312236313228172L;

	private ArtifactType type;
	private UUID parent;
	private UUID id;
	private Collection<IArtifact> childs = new HashSet<IArtifact>();
	private String name;
	private String description;
	private IWritableConfig data;
	private Long timestamp;
	private boolean dirty;
	
	public Artifact( ArtifactType type, IWritableConfig data ) {
		this.type = type;
		this.data = data;
	}

	@Override
	public void setId(UUID id) {
		this.id = id;
	}

	@Override
	public UUID getId() {
		return this.id;
	}

	public void makeDirty( boolean value ) {
		this.dirty = value;
	}

	public boolean isDirty() {
		return this.dirty;
	}

	@Override
	public void setParent(UUID parent) {
		this.parent = parent;
	}

	@Override
	public UUID getParent() {
		return this.parent;
	}

	@Override
	public void addChild(IArtifact artifact) {
		this.childs.add( artifact );
	}

	@Override
	public Collection<IArtifact> getChilds() {
		return this.childs;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public Long getTimestamp() {
		return this.timestamp;
	}

	@Override
	public void setType(ArtifactType type) {
		this.type = type;
	}

	@Override
	public ArtifactType getType() {
		return this.type;
	}

	@Override
	public IWritableConfig getData() {
		return this.data;
	}

	@Override
	public void setData(IWritableConfig config) {
		this.data = config;
	}
	
}
