package com.api.deployer.jobs.backup;

import com.api.deployer.system.devices.storage.IStorageDriveDevice;

public interface IStorageDriveBackupJob extends IBackupJob {

	public void setDevice( IStorageDriveDevice device );
	
	public IStorageDriveDevice getDevice();
	
	public Boolean doGrubBackup();
	
	public void doGrubBackup( Boolean value );
	
	public Boolean doMBRBackup();
	
	public void doMBRBackup( Boolean value );
	
}
