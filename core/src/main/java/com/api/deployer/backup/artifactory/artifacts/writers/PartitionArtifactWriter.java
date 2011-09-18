package com.api.deployer.backup.artifactory.artifacts.writers;

import com.api.deployer.backup.artifactory.WriterException;
import com.api.deployer.backup.artifactory.artifacts.IArtifact;
import com.api.deployer.backup.compression.CompressionLevel;
import com.api.deployer.backup.compression.CompressionMethod;
import com.api.deployer.backup.diff.DiffMethod;
import com.api.deployer.system.devices.storage.PartitionFlag;
import com.api.deployer.system.devices.storage.PartitionType;
import com.api.deployer.system.devices.storage.StorageFilesystem;
import com.redshape.utils.config.ConfigException;
import com.redshape.utils.config.IConfig;
import com.redshape.utils.config.IWritableConfig;

import java.util.UUID;

public class PartitionArtifactWriter extends AbstractArtifactWriter implements IPartitionArtifactWriter {

	public PartitionArtifactWriter(IArtifact artifact) {
		super(artifact);
	}

	@Override
	public void writeNumber(Integer number) throws WriterException {
		this.setDataValue("number", number);
	}

	@Override
	public void writeDevice(UUID id) throws WriterException {
		this.setDataValue("device", id);
	}

	@Override
	public void writeStart(Long start) throws WriterException {
		this.setDataValue("start", start);
	}

	@Override
	public void writeEnd(Long end) throws WriterException {
		this.setDataValue("end", end);
	}

	@Override
	public void writeType(PartitionType type) throws WriterException {
		this.setDataValue("type", type);
	}

	@Override
	public void writeFileSystem(StorageFilesystem filesystem) throws WriterException {
		this.setDataValue("filesystem", filesystem);
	}

	@Override
	public void writeUUID(UUID id) throws WriterException {
		this.setDataValue("uuid", id);
	}

	@Override
	public void writeFlag(PartitionFlag flag) throws WriterException {
		try {
			IWritableConfig flagsNode = this.getOrCreate("flags");
			
			boolean found = false;
			for ( IConfig flagNode : flagsNode.childs() ) {
				if ( flagNode.value() != null && flagNode.value().equals( flag.name() ) ) {
					found = true;
					break;
				}
			}
			
			if ( found ) {
				return;
			}
			
			flagsNode.createChild("flag").set( flag.name() );
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
