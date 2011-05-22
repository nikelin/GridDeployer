package com.api.deployer.jobs.handlers;

import java.util.UUID;

import com.api.deployer.jobs.IJob;
import com.api.deployer.jobs.manager.IJobsManager;
import com.api.deployer.jobs.result.IJobResult;
import com.api.deployer.system.ISystemFacade;

public abstract class AbstractJobHandler<T extends IJob, V extends IJobResult> implements IJobHandler<T, V> {
	private ISystemFacade system;
	private IJobsManager jobsManager;
	
	public AbstractJobHandler( ISystemFacade facade ) {
		this.system = facade;
	}

	public void setManagerContext( IJobsManager manager ) {
		this.jobsManager = manager;
	}

	protected IJobsManager getManagerContext() {
		return this.jobsManager;
	}
	
	protected ISystemFacade getSystem() {
		return this.system;
	}
	
	abstract protected V createJobResult( UUID jobId );
	
}
