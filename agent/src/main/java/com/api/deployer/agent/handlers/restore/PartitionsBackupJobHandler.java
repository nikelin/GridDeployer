package com.api.deployer.agent.handlers.restore;

import java.util.UUID;

import com.api.deployer.backup.BackupException;
import com.api.deployer.backup.IBackupEngine;
import com.api.deployer.backup.result.storages.IPartitionBackupResult;
import com.api.deployer.system.devices.storage.IStorageDevicePartition;
import com.api.deployer.jobs.backup.IPartitionsBackupJob;
import com.api.deployer.jobs.backup.result.IBackupJobResult;
import com.api.deployer.jobs.handlers.AbstractAwareJobHandler;
import com.api.deployer.jobs.handlers.HandlingException;
import com.api.deployer.system.ISystemFacade;

public class PartitionsBackupJobHandler extends AbstractAwareJobHandler<
											IPartitionsBackupJob, IBackupJobResult<IPartitionBackupResult>> {
	private IBackupEngine<IStorageDevicePartition, IPartitionBackupResult> engine;
	
	public PartitionsBackupJobHandler(ISystemFacade facade) {
		super(facade);
	}

	@Override
	public Integer getProgress() {
		throw new UnsupportedOperationException("Operation not implemented");
	}

	@Override
	public synchronized IBackupJobResult<IPartitionBackupResult> handle(IPartitionsBackupJob job)
			throws HandlingException {
		return this.createJobResult( job.getId() );
	}

	@Override
	public void cancel() throws HandlingException {
		try {
			this.engine.stop();
		} catch ( BackupException e ) {
			throw new HandlingException("Failed attempt to stop engine", e);
		}
	}

	@Override
	protected IBackupJobResult<IPartitionBackupResult> createJobResult(UUID jobId) {
		return null;
	}

}
