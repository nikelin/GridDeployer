package com.api.deployer.jobs.backup.result;

import com.api.deployer.backup.result.IBackupResult;
import com.redshape.daemon.jobs.result.IJobResult;

public interface IBackupJobResult<T extends IBackupResult> extends IJobResult {

	public void setBackupInfo( T info );
	
	public T getBackupInfo();
	
}
