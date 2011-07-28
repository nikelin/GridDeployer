package com.api.deployer.jobs.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.api.deployer.execution.services.IArtifactoryService;
import org.springframework.context.ApplicationContext;

import com.api.deployer.jobs.IJob;
import com.api.deployer.jobs.handlers.AbstractAwareJobHandler;
import com.api.deployer.jobs.handlers.HandlingException;
import com.api.deployer.jobs.handlers.IJobHandler;
import com.api.deployer.jobs.result.IJobResult;

public class JobsManager implements IJobsManager {
	private Map<Class<? extends IJob>, 
				IJobHandler<? extends IJob, ?>> handlers = new HashMap<Class<? extends IJob>,
														  IJobHandler<? extends IJob,?>>();
	private ApplicationContext context;
	private IArtifactoryService artifactoryService;
	private ExecutorService threadsExecutor;
	
	private Map<UUID, HandlingDescriptor<?,?>> jobs = new HashMap<UUID, HandlingDescriptor<?,?>>();
	
	public JobsManager() {
		this.threadsExecutor = Executors.newFixedThreadPool(100);
	}

	@Override
	public void setArtifactoryService( IArtifactoryService service ) {
		this.artifactoryService = service;
	}

	@Override
	public IArtifactoryService getArtifactoryService() {
		return this.artifactoryService;
	}
	
	protected ExecutorService getThreadsExecutor() {
		return this.threadsExecutor;
	}

	@Override
	public void setApplicationContext( ApplicationContext context ) {
		this.context = context;
	}
	
	public ApplicationContext getApplicationContext() {
		return this.context;
	}
	
	public void setHandlers( Map<Class<? extends IJob>, IJobHandler<? extends IJob,?>> handlers ) {
		this.handlers = handlers;
	}

	public Map<Class<? extends IJob>, IJobHandler<? extends IJob,?>> getHandlers() {
		return this.handlers;
	}
	
	@Override
	public void cancel( UUID id ) throws HandlingException {
		HandlingDescriptor<?,?> handler = this.jobs.get(id);
        if ( handler == null ) {
            return;
        }

        handler.cancel();
	}

	@Override
	public Integer getProgress( UUID id ) throws HandlingException {
		return this.jobs.get(id).getProgress();
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends IJob, R extends IJobResult> Future<R> execute( T job ) throws HandlingException {
		IJobHandler<T,R> handler = (IJobHandler<T,R>) this.handlers.get( job.getClass() );
		if ( handler == null ) {
			throw new HandlingException("Job support not implemented");
		}

		handler.setManagerContext( this );
		if ( handler instanceof AbstractAwareJobHandler ) {
			( (AbstractAwareJobHandler<T, R>) handler ).setContext( this.getApplicationContext() );
		}
		
		if ( handler == null ) {
			throw new HandlingException("Handler for given job does not exists");
		}
		
		HandlingDescriptor<T,R> descriptor = new HandlingDescriptor<T, R>( handler, job );
		
		this.jobs.put( job.getId(), descriptor );
		
		return this.getThreadsExecutor().submit( descriptor );
	}
	
	public class HandlingDescriptor<T extends IJob, R extends IJobResult> implements Callable<R> {
		private IJobHandler<T, R> handler;
		private T job;
		
		public HandlingDescriptor( IJobHandler<T, R> handler, T job ) {
			this.job = job;
			this.handler = handler;
		}

		public Integer getProgress() throws HandlingException {
			return this.handler.getProgress();
		}

		@Override
		public R call() throws Exception {
			return this.handler.handle(this.job);
		}
		
		public void cancel() throws HandlingException {
			this.handler.cancel();
		}

	}
	
}
