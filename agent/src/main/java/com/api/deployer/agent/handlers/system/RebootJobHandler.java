package com.api.deployer.agent.handlers.system;

import com.api.deployer.jobs.system.IRebootJob;
import com.api.deployer.system.ISystemFacade;
import com.redshape.daemon.jobs.handlers.AbstractJobHandler;
import com.redshape.daemon.jobs.handlers.HandlingException;
import com.redshape.daemon.jobs.result.IJobResult;
import com.redshape.daemon.jobs.result.JobResult;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public class RebootJobHandler extends AbstractJobHandler<IRebootJob, IJobResult> {
	private ThreadLocal<Date> startTime = new ThreadLocal<Date>();
	private ThreadLocal<IRebootJob> job = new ThreadLocal<IRebootJob>();

	public Integer getProgress() {
		return (int) ( new Date().getTime() / ( this.startTime.get().getTime() + this.job.get().getDelay() ) );
	}

	@Override
	public void cancel() {
		throw new RuntimeException("Cancellation login not implemented yet");
	}
	
	@Override
	public synchronized IJobResult handle( IRebootJob job ) throws HandlingException {
		try {
			this.startTime.set( new Date() );
			this.job.set( job );

			this.getContext().getBean(ISystemFacade.class).reboot(job.getDelay());
			
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
