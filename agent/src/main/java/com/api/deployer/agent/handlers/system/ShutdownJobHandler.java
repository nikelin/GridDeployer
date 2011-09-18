package com.api.deployer.agent.handlers.system;

import com.api.deployer.jobs.system.IRebootJob;
import com.api.deployer.jobs.system.IShutdownJob;
import com.api.deployer.system.ISystemFacade;
import com.redshape.daemon.jobs.handlers.AbstractJobHandler;
import com.redshape.daemon.jobs.handlers.HandlingException;
import com.redshape.daemon.jobs.result.IJobResult;
import com.redshape.daemon.jobs.result.JobResult;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;


public class ShutdownJobHandler extends AbstractJobHandler<IShutdownJob, IJobResult> {
	private ThreadLocal<Date> startTime = new ThreadLocal<Date>();
	private ThreadLocal<IRebootJob> job = new ThreadLocal<IRebootJob>();
	private ISystemFacade facade;

	public ShutdownJobHandler( ISystemFacade facade ) {
		this.facade = facade;
	}

	public ISystemFacade getFacade() {
		return facade;
	}

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
			this.getFacade().shutdown(job.getDelay());
			
			return this.createJobResult( job.getJobId() );
		} catch ( IOException e ) {
			throw new HandlingException( e.getMessage(), e );
		}
	}

	@Override
	protected IJobResult createJobResult( UUID jobId ) {
		return new JobResult( jobId );
	}
	
}
