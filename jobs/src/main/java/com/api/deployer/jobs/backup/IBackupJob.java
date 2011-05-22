package com.api.deployer.jobs.backup;

import com.api.deployer.backup.compression.CompressionLevel;
import com.api.deployer.backup.compression.CompressionMethod;
import com.api.deployer.io.transport.IDestination;
import com.api.deployer.jobs.IJob;

public interface IBackupJob extends IJob {
	
	public void setImageName( String name );
	
	public String getImageName();
	
	public void setImageDescription( String description );
	
	public String getImageDescription();

	public void doCompression( Boolean value );
	
	public Boolean doCompression();
	
	public void setCompressionMethod( CompressionMethod type );
	
	public CompressionMethod getCompressionMethod();
	
	public void setCompressionLevel( CompressionLevel level );
	
	public CompressionLevel getCompressionLevel();
	
}
