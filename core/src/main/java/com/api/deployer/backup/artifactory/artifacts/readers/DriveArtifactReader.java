package com.api.deployer.backup.artifactory.artifacts.readers;

import com.api.deployer.backup.artifactory.ReaderException;
import com.api.deployer.backup.artifactory.artifacts.IArtifact;
import com.api.deployer.backup.compression.CompressionLevel;
import com.api.deployer.backup.compression.CompressionMethod;
import com.api.deployer.backup.diff.DiffMethod;
import com.redshape.utils.config.ConfigException;
import com.redshape.utils.config.IConfig;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class DriveArtifactReader implements IDriveArtifactReader {
	private IArtifact artifact;
	
	public DriveArtifactReader( IArtifact artifact ) {
		this.artifact = artifact;
	}
	
	@Override
	public Collection<UUID> readPartitions() throws ReaderException {
		try {
			Collection<UUID> ids = new HashSet<UUID>();
			for ( IConfig node : this.artifact.getData().get("partitions").childs() ) {
				ids.add( UUID.fromString( node.attribute("uuid") ) );
			}
			
			return ids;
		} catch ( ConfigException e  ) {
			throw new ReaderException( e.getMessage(), e );
		}
	}

	@Override
	public String readModel() throws ReaderException {
		try {
			return this.artifact.getData().get("model").value();
		} catch ( ConfigException e  ) {
			throw new ReaderException( e.getMessage(), e );
		}
	}

	@Override
	public UUID readUUID() throws ReaderException {
		try {
			return UUID.fromString( this.artifact.getData().get("uuid").value() );
		} catch ( ConfigException e  ) {
			throw new ReaderException( e.getMessage(), e );
		}
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
	public boolean readCompressionState() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CompressionMethod readCompressionMethod() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompressionLevel readCompressionLevel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean readDeltaState() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DiffMethod getDiffMethod() {
		// TODO Auto-generated method stub
		return null;
	}

}
