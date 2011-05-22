package com.api.deployer.agent.handlers.system;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import com.api.deployer.jobs.handlers.AbstractJobHandler;
import com.api.deployer.jobs.handlers.HandlingException;
import com.api.deployer.jobs.result.IJobResult;
import com.api.deployer.jobs.result.JobResult;
import com.api.deployer.jobs.system.IRebootJob;
import com.api.deployer.jobs.system.IShutdownJob;
import com.api.deployer.system.ISystemFacade;


public class ShutdownJobHandler extends AbstractJobHandler<IShutdownJob, IJobResult> {
	private ThreadLocal<Date> startTime = new ThreadLocal<Date>();
	private ThreadLocal<IRebootJob> job = new ThreadLocal<IRebootJob>();

	public ShutdownJobHandler( ISystemFacade facade ) {
		super(facade);
	}

	@Override
	public Integer getProgress() {
		return (int) ( new Date().getTime() / ( this.startTime.get().getTime() + this.job.get().getDelay() ) );
	}

	@Override
	public void cancel() {
		throw new UnsupportedOperationException("Cancellation not implemented yet");
	}
	
	@Override
	// TODO
	public IJobResult handle( IShutdownJob job ) throws HandlingException {
		try {
			this.getSystem().shutdown(job.getDelay());
			
			return this.createJobResult( job.getId() );
		} catch ( IOException e ) {
			throw new HandlingException( e.getMessage(), e );
		}
	}

	@Override
	protected IJobResult createJobResult( UUID jobId ) {
		return new JobResult( jobId );
	}
	
}
