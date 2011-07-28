package com.api.deployer.jobs.handlers;

import com.api.deployer.jobs.IJob;
import com.api.deployer.jobs.manager.IJobsManager;
import com.api.deployer.jobs.result.IJobResult;

public interface IJobHandler<T extends IJob, V extends IJobResult> {

	public void setManagerContext( IJobsManager manager );

	public V handle( T job ) throws HandlingException;
	
	public void cancel() throws HandlingException;

	public Integer getProgress() throws HandlingException;
	
}
