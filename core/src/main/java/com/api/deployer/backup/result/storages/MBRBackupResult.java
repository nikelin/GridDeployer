package com.api.deployer.backup.result.storages;

public class MBRBackupResult implements IMBRBackupResult {
	private String finalName;
	private Integer chunkSize;
	
	public MBRBackupResult( String finalName, Integer chunkSize ) {
		this.finalName = finalName;
		this.chunkSize = chunkSize;
	}
	
	public void setFinalName( String finalName ) {
		this.finalName = finalName;
	}
	
	public String getFinalName() {
		return this.finalName;
	}
	
	public void setChunkSize( Integer chunkSize ) {
		this.chunkSize = chunkSize;
	}
	
	public Integer getChunkSize() {
		return this.chunkSize;
	}
	
}
