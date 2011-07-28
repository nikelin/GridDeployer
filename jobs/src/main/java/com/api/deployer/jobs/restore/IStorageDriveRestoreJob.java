package com.api.deployer.jobs.restore;

import java.util.UUID;

public interface IStorageDriveRestoreJob extends IRestoreJob {
	
	public void setDevice( UUID path );
	
	public UUID getDevice();
	
	public Boolean doGrubRestore();
	
	public void doGrubRestore( Boolean value );
	
	public Boolean doMBRRestore();
	
	public void doMBRRestore( Boolean value );
	
}
