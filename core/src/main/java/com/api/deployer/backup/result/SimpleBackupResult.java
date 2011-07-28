package com.api.deployer.backup.result;

public class SimpleBackupResult implements IBackupResult {
	private String finalName;
	
	public SimpleBackupResult( String finalName ) {
		this.finalName = finalName;
	}
	
	@Override
	public String getFinalName() {
		return this.finalName;
	}
	
}
