package com.api.deployer.backup.artifactory.artifacts.writers;

import com.api.deployer.backup.artifactory.WriterException;
import com.api.deployer.backup.artifactory.artifacts.IArtifact;
import com.api.deployer.backup.compression.CompressionLevel;
import com.api.deployer.backup.compression.CompressionMethod;
import com.api.deployer.backup.diff.DiffMethod;
import com.redshape.utils.config.ConfigException;
import com.redshape.utils.config.IConfig;
import com.redshape.utils.config.IWritableConfig;

import java.util.UUID;

public class DriveArtifactWriter extends AbstractArtifactWriter implements IDriveArtifactWriter {

	public DriveArtifactWriter(IArtifact artifact) {
		super(artifact);
	}

	@Override
	public void writeModel(String model) throws WriterException {
		this.setDataValue("model", model);
	}

	@Override
	public void writeUUID(UUID uuid) throws WriterException {
		this.setDataValue("uuid", uuid );
	}
	
	@Override
	public void writePath(String path) throws WriterException {
		this.setDataValue("path", path);
	}

	@Override
	public void writePartition(UUID partition) throws WriterException {
		try {
			IWritableConfig partitionsNode = this.getOrCreate("partitions");
			
			boolean found = false;
			for ( IConfig partitionNode : partitionsNode.childs() ) {
				if ( partitionNode.value() != null && partitionNode.value().equals( String.valueOf(partition) ) ) {
					found = true;
					break;
				}
			}
			
			if ( found ) {
				return;
			}
			
			partitionsNode.createChild("partition").set( String.valueOf(partition) );
		} catch ( ConfigException e  ) {
			throw new WriterException( e.getMessage(), e );
		}
	}

	@Override
	public void writeCompressionMethod(CompressionMethod method) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeCompressionLevel(CompressionLevel level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeDiffMethod(DiffMethod method) {
		// TODO Auto-generated method stub
		
	}

}
