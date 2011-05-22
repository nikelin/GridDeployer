package com.api.deployer.backup.artifactory.artifacts.readers;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import com.api.commons.config.ConfigException;
import com.api.commons.config.IConfig;
import com.api.deployer.backup.artifactory.ReaderException;
import com.api.deployer.backup.artifactory.artifacts.IArtifact;
import com.api.deployer.system.devices.storage.PartitionFlag;
import com.api.deployer.system.devices.storage.PartitionType;
import com.api.deployer.system.devices.storage.StorageFilesystem;

public class PartitionArtifactReader implements IPartitionArtifactReader {
	private IArtifact artifact;
	
	public PartitionArtifactReader( IArtifact artifact ) {
		this.artifact = artifact;
	}
	
	@Override
	public String readPath() throws ReaderException {
		try {
			return this.artifact.getData().get("path").value();
		} catch ( ConfigException e  ) {
			throw new ReaderException( e.getMessage(), e );
		}
	}

	@Override
	public UUID readUUID() throws ReaderException {
		try {
			return UUID.fromString( this.artifact.getData().get("uuid").value() );
		} catch ( ConfigException e ) {
			throw new ReaderException( e.getMessage(), e );
		}
	}

	@Override
	public Integer readNumber() throws ReaderException {
		try {
			return Integer.valueOf( this.artifact.getData().get("number").value() );
		} catch ( ConfigException e  ) {
			throw new ReaderException( e.getMessage(), e );
		}
	}

	@Override
	public Long readStart() throws ReaderException {
		try {
			return Long.valueOf( this.artifact.getData().get("start").value() );
		} catch ( ConfigException e  ) {
			throw new ReaderException( e.getMessage(), e );
		}
	}

	@Override
	public Long readEnd() throws ReaderException {
		try {
			return Long.valueOf( this.artifact.getData().get("end").value() );
		} catch ( ConfigException e  ) {
			throw new ReaderException( e.getMessage(), e );
		}
	}

	@Override
	public PartitionType readType() throws ReaderException {
		try {
			return PartitionType.valueOf( this.artifact.getData().get("type").value() );
		} catch ( ConfigException e ) {
			throw new ReaderException( e.getMessage(), e );
		}
	}

	@Override
	public StorageFilesystem readFileSystem() throws ReaderException {
		try {
			return StorageFilesystem.valueOf( this.artifact.getData().get("filesystem").value() );
		} catch ( ConfigException e  ) {
			throw new ReaderException( e.getMessage(), e );
		}
	}

	@Override
	public Collection<PartitionFlag> readFlags() throws ReaderException {
		try {
			Collection<PartitionFlag> flags = new HashSet<PartitionFlag>();
			for ( IConfig flagNode : this.artifact.getData().get("flags").childs() ) {
				flags.add( PartitionFlag.valueOf( flagNode.value() ) );
			}
			
			return flags;
		} catch ( ConfigException e ) {
			throw new ReaderException( e.getMessage(), e );
		}
	}
	

}
