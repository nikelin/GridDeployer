package com.api.deployer.jobs.backup;

import java.util.UUID;

import com.api.deployer.backup.compression.CompressionLevel;
import com.api.deployer.backup.compression.CompressionMethod;
import com.api.deployer.io.transport.IDestination;
import com.api.deployer.io.transport.ssh.SSHDestination;
import com.api.deployer.jobs.AbstractJob;
import com.redshape.bindings.annotations.Bindable;
import com.redshape.bindings.annotations.BindableReader;
import com.redshape.bindings.annotations.BindableWriter;

public abstract class AbstractBackupJob extends AbstractJob implements IBackupJob {
	private static final long serialVersionUID = 3296386388356264648L;

	private CompressionMethod compressionMethod;
	private CompressionLevel compressionLevel;
	private boolean doCompression;
	private String imageName;
	private String imageDescription;
	
	public AbstractBackupJob( UUID agentId ) {
		super(agentId);
	}
	
	@Override
	@Bindable( name = "Compression" )
	@BindableWriter( name = "doCompression" )
	public void doCompression( Boolean value ) {
		this.doCompression = value;
	}
	
	@Override
	@BindableReader( name = "doCompression" )
	public Boolean doCompression() {
		return this.doCompression;
	}
	
	@Override
	@Bindable( name = "Image name" )
	public void setImageName( String name ) {
		this.imageName = name;
	}
	
	@Override
	public String getImageName() {
		return this.imageName;
	}
	
	@Override
	public void setImageDescription( String description ) {
		this.imageDescription = description;
	}
	
	@Override
	@Bindable( name = "Image description" )
	public String getImageDescription() {
		return this.imageDescription;
	}
	
	@Bindable( name = "Compression method")
	@Override
	public void setCompressionMethod( CompressionMethod method ) {
		this.compressionMethod = method;
	}
	
	@Override
	public CompressionMethod getCompressionMethod() {
		return this.compressionMethod;
	}
	
	@Override
	@Bindable( name = "Compression level" )
	public void setCompressionLevel( CompressionLevel level ) {
		this.compressionLevel = level;
	}
	
	@Override
	public CompressionLevel getCompressionLevel() {
		return this.compressionLevel;
	}

}
