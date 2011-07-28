package com.api.deployer.backup.artifactory;

import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

import com.api.commons.config.ConfigException;
import com.api.commons.config.IConfig;
import com.api.deployer.AbstractContextAwareTest;
import com.api.deployer.backup.artifactory.artifacts.ArtifactType;
import com.api.deployer.backup.artifactory.index.IndexException;
import com.api.deployer.backup.artifactory.artifacts.IArtifact;
import com.api.deployer.backup.artifactory.artifacts.IArtifactBuilder;
import com.api.deployer.backup.artifactory.artifacts.readers.IDriveArtifactReader;
import com.api.deployer.backup.artifactory.artifacts.writers.IDriveArtifactWriter;
import com.api.deployer.backup.artifactory.artifacts.writers.IPartitionArtifactWriter;
import com.api.deployer.backup.artifactory.index.IArtifactoryIndex;
import com.api.deployer.backup.artifactory.index.IIndexReader;
import com.api.deployer.backup.artifactory.index.IIndexWriter;
import com.api.deployer.system.devices.storage.PartitionType;
import com.api.deployer.system.devices.storage.StorageFilesystem;

public class ArtifactoryTest extends AbstractContextAwareTest<ArtifactoryTest.Attribute> {
	private static final Logger log = Logger.getLogger( ArtifactoryTest.class );
	
	public enum Attribute {
		INDEX_VERSION,
		ARTIFACT_NAME,
		ARTIFACT_DESCRIPTION,
		ARTIFACT_ID,
		DEVICE_MODEL,
		DEVICE_PATH,
		DEVICE_UUID,
		FIRST_PARTITION_NUMBER,
		FIRST_PARTITION_UUID,
		FIRST_PARTITION_START,
		FIRST_PARTITION_END,
		FIRST_PARTITION_PARENT,
		FIRST_PARTITION_TYPE,
		FIRST_PARTITION_FILESYSTEM,
		FIRST_PARTITION_FLAGS
	}
	
	public ArtifactoryTest() {
		super("src/test/resources/context.xml");
		
		this.setAttribute( ArtifactoryTest.Attribute.INDEX_VERSION, ArtifactoryVersion.Current );
		this.setAttribute( ArtifactoryTest.Attribute.ARTIFACT_NAME, "DriveX");
		this.setAttribute( ArtifactoryTest.Attribute.ARTIFACT_DESCRIPTION, "Disk drive artifact #1");
		this.setAttribute( ArtifactoryTest.Attribute.DEVICE_MODEL, "SAMSUNG 33333");
		this.setAttribute( ArtifactoryTest.Attribute.DEVICE_PATH, "/dev/sda");
		this.setAttribute( ArtifactoryTest.Attribute.DEVICE_UUID, UUID.randomUUID() );
		this.setAttribute( ArtifactoryTest.Attribute.FIRST_PARTITION_START, 100000000L );
		this.setAttribute( ArtifactoryTest.Attribute.FIRST_PARTITION_END, 200000000L );
		this.setAttribute( ArtifactoryTest.Attribute.FIRST_PARTITION_FILESYSTEM, StorageFilesystem.EXT3 );
		this.setAttribute( ArtifactoryTest.Attribute.FIRST_PARTITION_NUMBER, 5 );
		this.setAttribute( ArtifactoryTest.Attribute.FIRST_PARTITION_TYPE, PartitionType.PRIMARY );
	}
	
	@Test
	public void testMain() throws ConfigException, InstantiationException, 
								  IndexException, WriterException, ReaderException {
		IArtifactoryFacade facade = this.getContext().getBean( IArtifactoryFacade.class );
		
		IArtifactBuilder builder = facade.createArtifactBuilder();
		assertNotNull(builder);
		
		IArtifact artifact = builder.createArtifact( ArtifactType.DRIVE );
		artifact.setId( this.<UUID>getAttribute( ArtifactoryTest.Attribute.ARTIFACT_ID ) );
		artifact.setDescription( this.<String>getAttribute( ArtifactoryTest.Attribute.ARTIFACT_DESCRIPTION ) );
		artifact.setName(this.<String>getAttribute( ArtifactoryTest.Attribute.ARTIFACT_NAME ));
		artifact.setTimestamp( new Date().getTime() );
		assertNotNull(artifact);
		assertEquals( artifact.getType(), ArtifactType.DRIVE );
		
		IDriveArtifactWriter writer = facade.createArtifactWriter( artifact );
		assertNotNull(writer);
		writer.writeModel( this.<String>getAttribute( ArtifactoryTest.Attribute.DEVICE_MODEL ) );
		writer.writePath( this.<String>getAttribute( ArtifactoryTest.Attribute.DEVICE_PATH ) );
		writer.writeUUID( this.<UUID>getAttribute( ArtifactoryTest.Attribute.DEVICE_UUID ) );
		writer.flush();
		
		IDriveArtifactReader reader = facade.createArtifactReader( artifact );
		assertEquals( reader.readModel(), this.<String>getAttribute( ArtifactoryTest.Attribute.DEVICE_MODEL ) );
		assertEquals( reader.readUUID(), this.<UUID>getAttribute( ArtifactoryTest.Attribute.DEVICE_UUID ) );
		assertEquals( reader.readPath(), this.<String>getAttribute( ArtifactoryTest.Attribute.DEVICE_PATH ) );
		
		IArtifactBuilder deviceContextBuilder = facade.createArtifactBuilder( artifact );
		IArtifact firstPartitionArtifact = deviceContextBuilder.createArtifact( ArtifactType.PARTITION );
		assertNotNull( firstPartitionArtifact );
		assertEquals( firstPartitionArtifact.getParent(), artifact.getId() );
		IPartitionArtifactWriter firstPartitionWriter = facade.createArtifactWriter(firstPartitionArtifact);
		firstPartitionWriter.writeStart( this.<Long>getAttribute( ArtifactoryTest.Attribute.FIRST_PARTITION_START ) );
		firstPartitionWriter.writeEnd( this.<Long>getAttribute( ArtifactoryTest.Attribute.FIRST_PARTITION_START ) );
		
		IIndexReader indexReader = facade.getIndexReader();
		IArtifactoryIndex index = indexReader.readIndex("src/test/resources/testArtifactoryIndex.xml");
		assertEquals( this.getAttribute( ArtifactoryTest.Attribute.INDEX_VERSION), index.getVersion() );
		index.addArtifact( artifact );
		assertEquals( 3, index.getArtifacts().size() );
		
		IIndexWriter indexWriter = facade.getIndexWriter();
		
		IConfig indexData = indexWriter.writeIndex( index );
		assertNotNull(indexData);
		assertEquals( indexData.get("artifacts").childs().length, 3 );
		
		log.info( indexData.serialize() );
	}
	
}
