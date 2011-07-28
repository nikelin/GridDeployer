package com.api.deployer.jobs.backup;

import java.util.UUID;

public class SettingsBackupJob extends AbstractBackupJob implements ISettingsBackupJob {
	private static final long serialVersionUID = 4414771837805553752L;
	
	private boolean doNetworkBackup;
	private boolean doSSHBackup;
	private boolean doBootloaderBackup;
	private boolean doAccountsBackup;
	
	public SettingsBackupJob( UUID agentId ) {
		super( agentId );
	}
	
	@Override
	public boolean doAccountsBackup() {
		return this.doAccountsBackup;
	}
	
	@Override
	public void doAccountsBackup(boolean value) {
		this.doAccountsBackup = value;
	}
	
	@Override
	public boolean doNetworkBackup() {
		return this.doNetworkBackup;
	}
	
	@Override
	public void doNetworkBackup(boolean value) {
		this.doNetworkBackup = value;
	}
	
	@Override
	public boolean doBootloaderBackup() {
		return this.doBootloaderBackup;
	}
	
	@Override
	public void doBootloaderBackup(boolean value) {
		this.doBootloaderBackup = value;
	}
	
	@Override
	public boolean doSSHBackup() {
		return this.doSSHBackup;
	}
	
	@Override
	public void doSSHBackup(boolean value) {
		this.doSSHBackup = value;
	}

}
