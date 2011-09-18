package com.api.deployer.backup.artifactory.artifacts.writers;

import com.api.deployer.backup.artifactory.WriterException;
import com.api.deployer.backup.artifactory.artifacts.IArtifact;
import com.redshape.utils.config.ConfigException;
import com.redshape.utils.config.IConfig;
import com.redshape.utils.config.IWritableConfig;

public abstract class AbstractArtifactWriter implements IArtifactWriter {
	private IArtifact artifact;
	
	public AbstractArtifactWriter( IArtifact artifact ) {
		this.artifact = artifact;
	}
	
	protected IArtifact getArtifact() {
		return this.artifact;
	}
	
	@Override
	public IConfig flush() throws WriterException {
		try {
			IWritableConfig config = this.artifact.getData();
			config.attribute( "id", String.valueOf( this.artifact.getId() ) );
			config.attribute( "parent", String.valueOf( this.artifact.getParent() ) );
			config.attribute( "timestamp", String.valueOf( this.artifact.getTimestamp() ) );
			
			IWritableConfig nameNode = (IWritableConfig) config.get("name");
			if ( nameNode.isNull() ) {
				nameNode = config.createChild("name");
			}
			nameNode.set( this.artifact.getName() );
			
			IWritableConfig descriptionNode = (IWritableConfig) config.get("description");
			if ( descriptionNode.isNull() ) {
				descriptionNode = config.createChild("description");
			}
			descriptionNode.set( this.artifact.getDescription() );
			
			return config;
		} catch ( ConfigException e ) {
			throw new WriterException( e.getMessage(), e );
		}
	}
	
	protected void setDataValue( String nodeName, Object value ) throws WriterException {
		try {
			this.getOrCreate(nodeName).set( String.valueOf(value) );
		} catch ( ConfigException e ) {
			throw new WriterException( e.getMessage(), e );
		}
	}
	
	protected IWritableConfig getOrCreate( String nodeName ) throws WriterException {
		try {
			IWritableConfig data = this.artifact.getData();
			
			IWritableConfig node = (IWritableConfig) data.get(nodeName);
			if ( node.isNull() ) {
				node = data.createChild(nodeName);
			}
			
			return node;
		} catch ( ConfigException e  ) {
			throw new WriterException( e.getMessage(), e );
		}
	}
	
}
