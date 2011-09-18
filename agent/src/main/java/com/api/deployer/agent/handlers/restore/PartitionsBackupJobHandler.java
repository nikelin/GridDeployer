package com.api.deployer.agent.handlers.restore;

import com.api.deployer.backup.BackupException;
import com.api.deployer.backup.IBackupEngine;
import com.api.deployer.backup.result.storages.IPartitionBackupResult;
import com.api.deployer.jobs.backup.IPartitionsBackupJob;
import com.api.deployer.jobs.backup.result.IBackupJobResult;
import com.api.deployer.system.ISystemFacade;
import com.api.deployer.system.devices.storage.IStorageDevicePartition;
import com.redshape.daemon.jobs.handlers.AbstractJobHandler;
import com.redshape.daemon.jobs.handlers.HandlingException;

import java.util.UUID;

public class PartitionsBackupJobHandler extends AbstractJobHandler<
											IPartitionsBackupJob, IBackupJobResult<IPartitionBackupResult>> {
	private IBackupEngine<IStorageDevicePartition, IPartitionBackupResult> engine;
	private ISystemFacade facade;

	public PartitionsBackupJobHandler(ISystemFacade facade) {
		super();

		this.facade = facade;
	}

	@Override
	public synchronized IBackupJobResult<IPartitionBackupResult> handle(IPartitionsBackupJob job)
			throws HandlingException {
		return this.createJobResult( job.getJobId() );
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
