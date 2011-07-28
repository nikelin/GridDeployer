package com.api.deployer.jobs.manager;

import java.util.UUID;
import java.util.concurrent.Future;

import com.api.deployer.execution.services.IArtifactoryService;
import org.springframework.context.ApplicationContext;

import com.api.deployer.jobs.IJob;
import com.api.deployer.jobs.handlers.HandlingException;
import com.api.deployer.jobs.result.IJobResult;
import org.springframework.context.ApplicationContextAware;

public interface IJobsManager extends ApplicationContextAware{

	public void setArtifactoryService( IArtifactoryService service );

	public IArtifactoryService getArtifactoryService();

	public void setApplicationContext( ApplicationContext context );
	
	public <T extends IJob, R extends IJobResult> Future<R> execute( T job ) 
				throws HandlingException;

	public void cancel( UUID jobId ) throws HandlingException;

	public Integer getProgress( UUID jobId ) throws HandlingException;

	
}
