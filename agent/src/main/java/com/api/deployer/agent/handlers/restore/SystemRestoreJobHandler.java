package com.api.deployer.agent.handlers.restore;

import java.util.UUID;

import com.api.deployer.jobs.handlers.AbstractAwareJobHandler;
import com.api.deployer.jobs.handlers.HandlingException;
import com.api.deployer.jobs.restore.ISystemRestoreJob;
import com.api.deployer.jobs.result.JobResult;

import com.api.deployer.system.ISystemFacade;

public class SystemRestoreJobHandler extends AbstractAwareJobHandler<ISystemRestoreJob, JobResult> {
	private StorageRestoreJobHandler storageHandler;
	
	public SystemRestoreJobHandler( ISystemFacade facade ) {
		super( facade );

		this.storageHandler = new StorageRestoreJobHandler( this.getSystem() );
	}

	protected StorageRestoreJobHandler getStorageHandler() {
		if ( this.storageHandler.getContext() == null ) {
			this.storageHandler.setContext( this.getContext() );
		}
		
		return this.storageHandler;
	}
	
	public void cancel() throws HandlingException {
		this.storageHandler.cancel();
	}

	@Override
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
