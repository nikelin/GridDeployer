package com.api.deployer.jobs.backup;

public interface ISettingsBackupJob extends IBackupJob {

	public boolean doAccountsBackup();
	
	public void doAccountsBackup( boolean value );
	
	public boolean doNetworkBackup();
	
	public void doNetworkBackup( boolean value );
	
	public boolean doBootloaderBackup();
	
	public void doBootloaderBackup( boolean value );
	
	public boolean doSSHBackup();
	
	public void doSSHBackup( boolean value );
	
}
