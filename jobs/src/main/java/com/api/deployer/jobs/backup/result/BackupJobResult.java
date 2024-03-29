package com.api.deployer.jobs.backup.result;

import com.api.deployer.backup.result.IBackupResult;
import com.redshape.daemon.jobs.result.JobResult;

import java.util.UUID;

public class BackupJobResult<T extends IBackupResult> extends JobResult
													  implements IBackupJobResult<T> {
	private static final long serialVersionUID = 8219564549513874350L;
	
	private T backupInfo;
	
	public BackupJobResult(UUID jobId) {
		super(jobId);
	}

	@Override
	public void setBackupInfo(T info) {
		this.backupInfo = info;
	}

	@Override
	public T getBackupInfo() {
		return this.backupInfo;
	}
	

}
