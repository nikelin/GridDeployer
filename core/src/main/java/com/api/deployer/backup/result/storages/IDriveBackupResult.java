package com.api.deployer.backup.result.storages;

import java.util.Collection;
import java.util.UUID;

import com.api.deployer.backup.result.IBackupResult;

public interface IDriveBackupResult extends IBackupResult {
	
	public void setUUID( UUID uuid );
	
	public UUID getUUID();
	
	public void setPartitionsType( String type );
	
	public String getPartitionsType();
	
	public void setName( String name );
	
	public String getName();
	
	public void setPath( String path );
	
	public String getPath();
	
	public void setModel( String model );
	
	public String getModel();
	
	public void setGrubResult( IBackupResult result );
	
	public IBackupResult getGrubResult();
	
	public void setPartitionsResult( Collection<IPartitionBackupResult> result );
	
	public void addPartitionResult( IPartitionBackupResult result );
		
	public Collection<IPartitionBackupResult> getPartitionsResult();
	
	public void setMBRResult( IMBRBackupResult result );
	
	public IMBRBackupResult getMBRResult();
	
	public void setImageName( String name );
	
	public String getImageName();
	
	public void setImageDescription( String description );
	
	public String getImageDescription();
	
}
