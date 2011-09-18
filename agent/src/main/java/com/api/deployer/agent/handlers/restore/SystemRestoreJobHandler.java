package com.api.deployer.agent.handlers.restore;

import com.api.deployer.jobs.restore.ISystemRestoreJob;
import com.api.deployer.system.ISystemFacade;
import com.redshape.daemon.jobs.handlers.AbstractJobHandler;
import com.redshape.daemon.jobs.handlers.HandlingException;
import com.redshape.daemon.jobs.result.JobResult;

import java.util.UUID;

public class SystemRestoreJobHandler extends AbstractJobHandler<ISystemRestoreJob, JobResult> {
	private StorageRestoreJobHandler storageHandler;
	private ISystemFacade facade;

	public SystemRestoreJobHandler( ISystemFacade facade) {
		super();

		this.facade = facade;

		this.storageHandler = new StorageRestoreJobHandler( facade );
	}

	protected ISystemFacade getFacade() {
		return this.facade;
	}

	protected StorageRestoreJobHandler getStorageHandler() {
		this.storageHandler.setApplicationContext(this.getContext());
		return this.storageHandler;
	}
	
	public void cancel() throws HandlingException {
		this.storageHandler.cancel();
	}

	public Integer getProgress() {
		throw new UnsupportedOperationException("Operation not implemented");
	}
	
	@Override
	public JobResult handle(ISystemRestoreJob job) throws HandlingException {
		throw new RuntimeException("Not implemented");
	}


	@Override
	protected JobResult createJobResult(UUID jobId) {
		return new JobResult( jobId );
	}

}
