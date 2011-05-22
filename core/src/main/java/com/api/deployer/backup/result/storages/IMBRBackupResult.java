package com.api.deployer.backup.result.storages;

import com.api.deployer.backup.result.IBackupResult;

public interface IMBRBackupResult extends IBackupResult {

	public Integer getChunkSize();
	
}
