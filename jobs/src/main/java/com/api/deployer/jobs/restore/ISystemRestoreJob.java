package com.api.deployer.jobs.restore;

public interface ISystemRestoreJob extends IRestoreJob {
	
	public void setDevice( String device );
	
	public String getDevice();
	
	public void doNetworkRestore( Boolean value );
	
	public Boolean doNetworkRestore();
	
	public void doSettingsRestore( Boolean value );
	
	public Boolean doSettingsRestore();
	
	public void doStorageRestore( Boolean value );
	
}
