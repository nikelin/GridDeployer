package com.api.deployer.agent.services;

import com.api.deployer.execution.services.IArtifactoryService;
import com.api.deployer.execution.services.IDeployAgentService;
import com.api.deployer.execution.services.IDeployServerService;
import com.api.deployer.system.ISystemFacade;
import com.api.deployer.system.devices.IDevice;
import com.api.deployer.system.scanners.ScannerException;
import com.redshape.daemon.IRemoteService;
import com.redshape.daemon.jobs.IJob;
import com.redshape.daemon.jobs.JobException;
import com.redshape.daemon.jobs.handlers.HandlingException;
import com.redshape.daemon.jobs.managers.IJobsManager;
import com.redshape.daemon.jobs.result.IJobResult;
import com.redshape.daemon.services.Connector;
import com.redshape.utils.IFilter;
import com.redshape.utils.events.AbstractEventDispatcher;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeployAgentService extends AbstractEventDispatcher
								implements IDeployAgentService, IRemoteService, Serializable {
	private static final Logger log = Logger.getLogger( DeployAgentService.class );
	private static final long serialVersionUID = -8900430169833689348L;

	private IJobsManager jobsManager;
	private ApplicationContext context;
	private IDeployServerService service;
	private String serviceName;
	private UUID id;
	private IArtifactoryService artifactoryService;
	private Connector connector;
    private ExecutorService threadsExecutor;

	public DeployAgentService( String serviceName,
							   IDeployServerService service,
							   IArtifactoryService artifactoryService,
							   ApplicationContext context )
		throws RemoteException {
		this.artifactoryService = artifactoryService;
		this.service = service;
		this.context = context;
		this.serviceName = serviceName;
		this.connector = new Connector();
        this.threadsExecutor = Executors.newFixedThreadPool(10);
	}

    public ExecutorService getThreadsExecutor() {
        return this.threadsExecutor;
    }

	protected IArtifactoryService getArtifactoryService() {
		return this.artifactoryService;
	}

	protected IDeployServerService getDeployServer() {
		return this.service;
	}

	public UUID getId() {
		return this.id;
	}

	@Override
	public void setId( UUID id ) {
		this.id = id;
	}
	
	@Override
	public boolean ping() {
		return true;
	}
	
	protected ISystemFacade getSystemFacade() {
		return this.context.getBean(ISystemFacade.class);
	}
	
	protected IJobsManager getJobsManager() throws RemoteException {
		this.jobsManager = this.context.getBean( IJobsManager.class );
		this.jobsManager.setApplicationContext(this.context);

		return this.jobsManager;
	}
	
	protected void failJob( IJob job, Throwable exception ) throws RemoteException {
		this.getDeployServer().fail(this.getId(), job != null ? job.getJobId()
												: null, new JobException(exception.getMessage()
												,  exception));
		log.error("Job: " + job.getJobId() + " has been failed", exception );
	}

	@Override
	public Integer getJobProgress( UUID jobId ) throws RemoteException {
//		try {
			// @TODO
			// return this.getJobsManager().getProgress(jobId);
			return null;
//		} catch ( HandlingException e ) {
//			throw new RemoteException( "Unexpected exception while progress gathering", e );
//		}
	}

	@Override
	public IJobResult accept( IJob job ) throws RemoteException {
		try {
			log.info("New job accepted...");

            IJobResult jobResult = this.getThreadsExecutor().submit( new ExecutionThread( this.getJobsManager(), job ) ).get();
            log.info("Job result: " + jobResult );

            this.getDeployServer().complete( this.getId(), job.getJobId() );

            return jobResult;
		} catch ( Throwable e ) {
			log.error( e.getMessage(), e );
			this.failJob( job, e );
            throw new RemoteException( e.getMessage(), e);
		}
	}
	
	@Override
	public void cancel( UUID jobId ) throws RemoteException {
		try {
			log.info("Canceling job: " + jobId );
			
			this.getJobsManager().cancel( jobId );
		} catch ( HandlingException e  ) {
			log.error( e.getMessage(), e );
			throw new RemoteException("Job canceling been failed", e);
		}
	}
	
	@Override
	public void pause( UUID jobId ) throws RemoteException {
		// @TODO: implements through JobsManager
	}
	
	@Override
	public String getServiceName() throws RemoteException {
		return this.serviceName;
	}

	@Override
	public Collection<IDevice> getDevices() throws RemoteException {
		try {
			return this.getSystemFacade().getDevices();
		} catch ( ScannerException e  ) {
			throw new RemoteException( e.getMessage(), e );
		}
	}

	@Override
	public Collection<IDevice> getDevices(IFilter<IDevice> filter) throws RemoteException {
		try {
			return this.getSystemFacade().getDevices(filter);
		} catch ( ScannerException e  ) {
			throw new RemoteException( e.getMessage(), e );
		}
	}

	@Override
	public IDevice getDevice(IFilter<IDevice> filter) throws RemoteException {
		try {
			return this.getSystemFacade().getDevice(filter);
		} catch ( ScannerException e  ) {
			throw new RemoteException( e.getMessage(), e );
		}
	}
	
	@Override
	public String executeScript( String script ) throws RemoteException { 
		try {
			return this.getSystemFacade().getConsole().createExecutor(script).execute();
		} catch ( IOException e ) {
			throw new RemoteException("Script execution failed...", e);
		}
	}

    public static class ExecutionThread implements Callable<IJobResult> {
        private IJobsManager manager;
        private IJob job;

        public ExecutionThread( IJobsManager manager, IJob job ) {
            this.manager = manager;
            this.job = job;
        }

        @Override
        public IJobResult call() throws Exception {
            return this.manager.execute(job).get();
        }
    }
	
}
