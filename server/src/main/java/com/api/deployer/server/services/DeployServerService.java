package com.api.deployer.server.services;

import com.api.deployer.execution.IExecutorDescriptor;
import com.api.deployer.execution.services.IDeployAgentService;
import com.api.deployer.execution.services.IDeployServerService;
import com.api.deployer.io.transport.IDestination;
import com.api.deployer.jobs.JobScope;
import com.api.deployer.notifications.INotification;
import com.api.deployer.notifications.ITransportFacade;
import com.api.deployer.notifications.Notification;
import com.api.deployer.notifications.NotificationType;
import com.api.deployer.services.DeployServiceEvent;
import com.api.deployer.system.devices.IDevice;
import com.api.deployer.system.processes.ISystemProcess;
import com.redshape.daemon.IRemoteService;
import com.redshape.daemon.jobs.IJob;
import com.redshape.daemon.jobs.JobException;
import com.redshape.daemon.jobs.JobStatus;
import com.redshape.daemon.jobs.activation.ActivationAttribute;
import com.redshape.daemon.jobs.activation.JobActivationProfile;
import com.redshape.daemon.jobs.managers.IJobsManager;
import com.redshape.daemon.jobs.result.IJobResult;
import com.redshape.daemon.jobs.result.JobResult;
import com.redshape.utils.IFilter;
import com.redshape.utils.events.AbstractRemoteEventDispatcher;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.*;

import static org.quartz.DateBuilder.IntervalUnit;
import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Deploy server operational handler implementation
 *
 * Some garbage collection guy needs here....
 *
 * @author nikelin
 */
public class DeployServerService extends AbstractRemoteEventDispatcher
								implements IDeployServerService, IRemoteService,Serializable {
	private static final long serialVersionUID = -9187503332296361040L;
	private static final Logger log = Logger.getLogger( DeployServerService.class );

    public static final String EXECUTION_SERVICE = "Scheduler.Trigger.ExecutionService";
    public static final String ACTUAL_JOB = "Scheduler.Trigger.ActualJob";
    public static final String JOBS_GROUP = "DeployServerService.scheduledJobs";

    private Map<UUID, Collection<IJobResult>> executionResults
            = new HashMap<UUID, Collection<IJobResult> >();

    private Map<UUID, IJob> serverJobs = new HashMap<UUID, IJob>();
	private Map<UUID, IExecutorDescriptor> executors = new HashMap<UUID, IExecutorDescriptor>();
	private Map<UUID, IJob> jobs = new HashMap<UUID, IJob>();
	private Map<UUID, Collection<UUID>> executorJobs = new HashMap<UUID, Collection<UUID>>();
	private Map<UUID, IDeployAgentService> executorConnections
            = new HashMap<UUID, IDeployAgentService>();

	private URI artifactoryURI;
	
	private Map<UUID, UUID> failedJobs = new HashMap<UUID, UUID>();
	private Map<UUID, UUID> completedJobs = new HashMap<UUID, UUID>();
	
	private Scheduler scheduler;
	private ApplicationContext context;
	private String serviceName;
	private ExecutorService threadExecutor;
	
	public DeployServerService( ApplicationContext context, String serviceName )
            throws RemoteException {
		this.context = context;
		this.serviceName = serviceName;
		
		this.threadExecutor = Executors.newFixedThreadPool(25);
		
		try {
			this.scheduler = StdSchedulerFactory.getDefaultScheduler();
            this.scheduler.start();
		} catch ( SchedulerException e  ) {
			throw new RemoteException( e.getMessage(), e );
		}
		
		try {
			this.artifactoryURI = this.context.getBean("artifactoryURI", URI.class );
		} catch ( Throwable e ) {
			log.info("Artifactory destination not specified...");
		}
	}

	protected ApplicationContext getContext() {
		return this.context;
	}

    protected IJobsManager getJobsManager() {
        return this.getContext().getBean( IJobsManager.class );
    }
	
	public void setScheduler( Scheduler scheduler ) {
		this.scheduler = scheduler;
	}
	
	protected Scheduler getScheduler() {
		return this.scheduler;
	}

	protected ITransportFacade getNotificationsFacade() {
		return this.getContext().getBean( ITransportFacade.class );
	}

	@Override
	public void sendNotification( INotification notification ) throws RemoteException {
		try {
			this.getNotificationsFacade().send( notification );
		} catch ( Throwable e ) {
			throw new RemoteException( e.getMessage(), e );
		}
	}

	@Override
	public void registerNotifyPoint(IDestination destination) throws RemoteException {
		try {
			this.getNotificationsFacade().addEndPoint( destination );
		} catch ( Throwable e ) {
			log.error( e.getMessage(), e );
			throw new RemoteException( e.getMessage(), e );
		}
	}

	@Override
	public URI getArtifactoryURI() {
		return this.artifactoryURI;
	}

	@Override
	public void setArtifactoryURI(URI uri) {
		this.artifactoryURI = uri;
	}

	@Override
	public Collection<IDevice> getDevices(UUID agentId) throws RemoteException {
		return this.executorConnections.get(agentId).getDevices();
	}

	@Override
	public Collection<IDevice> getDevices(UUID agentId, IFilter<IDevice> filter) throws RemoteException {
		return this.executorConnections.get(agentId).getDevices(filter);
	}
	
	@Override
	public IDevice getDevice(UUID agentId, IFilter<IDevice> filter) throws RemoteException {
		return this.executorConnections.get(agentId).getDevice(filter);
	}

	@Override
	public String getHostname( UUID agentId ) throws RemoteException {
		return null;
	}

	@Override
	public Collection<ISystemProcess> getProcesses( UUID agentId ) throws RemoteException {
		return null;
	}

	@Override
	public Map<UUID, IExecutorDescriptor> getConnectedExecutors() throws RemoteException {
		return this.executors;
	}

	@Override
	public Integer getProgress( UUID agentId, UUID job ) throws RemoteException {
		return this.executorConnections.get( agentId )
									   .getJobProgress(job);
	}

    @Override
    public Collection<IJobResult> getExecutionResults() throws RemoteException {
        Collection<IJobResult> result = new ArrayList<IJobResult>();

        for ( Collection<IJobResult> values : this.executionResults.values() ) {
            result.addAll( values );
        }

        return result;
    }

    @Override
    public Collection<IJobResult> getExecutionResults( UUID jobId ) throws RemoteException {
        return this.executionResults.get( jobId );
    }

    @Override
	public Map<UUID, IJob> getJobs( UUID agentId ) throws RemoteException {
		Map<UUID, IJob> jobs = new HashMap<UUID, IJob>();
		final Collection<UUID> currentJobs = this.executorJobs.get(agentId);
		if ( currentJobs == null || currentJobs.isEmpty() ) {
			return new HashMap<UUID, IJob>();
		}
		
		for ( UUID jobId : currentJobs ) {
			jobs.put( jobId, this.jobs.get(jobId) );
		}
		
		return jobs;
	}

	protected void registerJob( UUID agentId, IJob job ) throws RemoteException {
		job.setJobId(this.generateId());
		
		this.jobs.put( job.getJobId(), job );
		
		if ( this.executorJobs.get( agentId ) == null ) {
			this.executorJobs.put( agentId, new HashSet<UUID>() );
		}
		
		this.executorJobs.get( agentId ).add( job.getJobId() );
	}
	
	protected ExecutorService getThreadExecutor() {
		return this.threadExecutor;
	}
	
	protected void unregisterJob( UUID agentId, IJob job ) {
		this.jobs.remove( job.getJobId() );
		this.executorJobs.get( agentId ).remove( job.getJobId() );
	}

    protected UUID registerServerJob( IJob job ) throws RemoteException {
        UUID id = UUID.randomUUID();
        job.setJobId(id);
        this.serverJobs.put( id, job );
        return id;
    }

	@Override
	public synchronized UUID register(IExecutorDescriptor description) throws RemoteException {
		UUID id = description.getStationID();
		
		if ( !this.executors.containsKey(id) ) {
			this.executors.put( id, description);
			log.info("New agent registered: " + description.getLocation().toString() );
			this.executorConnections.put( id, this.createAgentConnection( id, description ) );
			
			return id;
		}
		
		return id;
	}
	
	protected IDeployAgentService createAgentConnection( UUID id, IExecutorDescriptor descriptor ) throws RemoteException {
		try {
			log.info("Trying to establish connection with agent: " + descriptor.getLocation().toString() );
			IDeployAgentService service = (IDeployAgentService) Naming.lookup( descriptor.getLocation().toString() );
			
			this.scheduleThread( new AgentMonitoringThread( id, service, 5000 ) );
			
			return service;
		} catch ( MalformedURLException e ) {
			throw new RemoteException("Malformed remote service location address", e );
		} catch ( NotBoundException e ) {
			throw new RemoteException("Remote service unreachable");
		}
	}

    @Override
    public void pauseJob(UUID job) throws RemoteException {
        try {
            this.jobs.get( job ).setState( JobStatus.WAITING );
            this.scheduler.pauseJob( new JobKey( job.toString(), JOBS_GROUP ) );
        } catch ( SchedulerException e ) {
            log.error( e.getMessage(), e );
            throw new RemoteException( "Unable to pause job " + job.toString() );
        }
    }

    @Override
    public void resumeJob(UUID job) throws RemoteException {
        try {
            this.jobs.get(job).setState( JobStatus.WAITING );
            this.scheduler.resumeJob(new JobKey(job.toString(), JOBS_GROUP));
        } catch ( SchedulerException e ) {
            log.error( e.getMessage(), e );
            throw new RemoteException( "Unable to resume job " + job.toString() );
        }
    }

    protected void scheduleThread( Runnable thread ) {
		this.getThreadExecutor().execute( thread );
	}

	protected Future<IJobResult> executeJob( Callable<IJobResult> thread ) {
		return this.getThreadExecutor().submit( thread );
	}
	
	protected IDeployAgentService getConnection( UUID agentId ) {
		return this.executorConnections.get(agentId);
	}

	@Override
	public synchronized void unregister(UUID executorId) throws RemoteException {
		this.executors.remove(executorId);
		this.executorConnections.remove(executorId);
		this.executorJobs.remove(executorId);
	}

	@Override
	public synchronized void complete( UUID agentId, UUID jobId ) throws RemoteException {
		log.info("Job completed: " + jobId );
		this.jobs.remove( jobId );
		this.raiseEvent( new DeployServiceEvent.Job.Complete( jobId ) );
		this.completedJobs.put(jobId, agentId);
	}

	@Override
	// TODO
	public synchronized void fail( UUID agentId, UUID jobId, JobException e) throws RemoteException {
		log.info("Job has been failed!");
		this.executorJobs.remove(jobId);
		this.raiseEvent( new DeployServiceEvent.Job.Fail( jobId ) );
		this.failedJobs.put(jobId, agentId);
		this.completedJobs.put( jobId, agentId );

        this.sendNotification(
            new Notification(
                NotificationType.ERROR,
                "Job execution failed",
                "Job with ID#" + jobId.toString()
                       + " has been failed while processing by agent with ID#"
                            + agentId.toString()
            )
        );
	}

	@Override
	public String getServiceName() throws RemoteException {
		return this.serviceName;
	}

    protected void addResult( IJobResult result ) {
        Collection<IJobResult> results = this.executionResults.get(result.getJobId());
        if ( results == null ) {
            this.executionResults.put( result.getJobId(), results = new HashSet<IJobResult>() );
        }

        results.add( result );
    }

    protected IJobResult executeAgentJob( UUID agentId, IJob job ) throws RemoteException {
        this.registerJob(agentId, job);

        this.executeJob(new JobExecutionThread(this.executorConnections.get(agentId), agentId, job));

        return this.createJobResult( job );
    }

    protected IJobResult executeServerJob( IJob job ) throws RemoteException {
        try {
            this.registerServerJob( job );
            log.info("New job accepted...");

            Future<IJobResult> result = this.getThreadExecutor().submit(
                new JobProcessingThread( this.getJobsManager(), job )
            );

            return result.get();
        } catch ( Throwable e ) {
            throw new RemoteException("Unexpected exception while processing specified job", e );
        }
    }

    protected IJobResult executeArtifactoryJob( IJob job ) throws RemoteException {
        throw new UnsupportedOperationException("Support for specified scope not implemented");
    }

    @Override
    // TODO: add scope processor selection strategy instead of hard-coded selection
    public IJobResult executeJob( JobScope scope, UUID agentId, IJob job ) throws RemoteException {
        IJobResult result;
        if (scope.equals(JobScope.SERVER) ) {
            result = this.executeServerJob( job );
        } else if ( scope.equals(JobScope.AGENT)) {
            result = this.executeAgentJob( agentId, job );
        } else if ( scope.equals(JobScope.ARTIFACTORY) ) {
            result = this.executeArtifactoryJob( job );
        } else {
            throw new RemoteException("Unknown job scope");
        }

        this.addResult( result );

        this.sendNotification( new Notification(
                NotificationType.INFO,
                "Job execution completed!",
                "Job with ID#" + job.getJobId().toString() + " has been completed successfuly!" )
        );

        return result;
    }

	@Override
	public Collection<IJobResult> executeJobs( JobScope scope, UUID agentId, Collection<IJob> jobs ) throws RemoteException {
		Collection<IJobResult> results = new HashSet<IJobResult>();
		for ( IJob job : jobs ) {
			results.add( this.executeJob(scope, agentId, job) );
		}

		return results;
	}

    @Override
    public Collection<IJob> getScheduledJobs() throws RemoteException {
        try {
            Collection<IJob> result = new HashSet<IJob>();

            Set<JobKey> keys = this.scheduler.getJobKeys( GroupMatcher.groupEquals( JOBS_GROUP ) );
            for ( JobKey key : keys ) {
                result.add(this.jobs.get(UUID.fromString(key.getName())));
            }

            for ( IJob job : this.jobs.values() ) {
                result.add( job );
            }

            for ( IJob job : this.serverJobs.values() ) {
                result.add( job );
            }

            return result;
        } catch ( SchedulerException e ) {
            throw new RemoteException("Unable to request list of scheduled jobs");
        }
    }

    @Override
    public IJob getJob(UUID jobId) throws RemoteException {
        return this.jobs.get(jobId);
    }

    @Override
	public UUID scheduleJob( UUID agentId, IJob job, JobActivationProfile profile ) throws RemoteException {
        this.registerJob( agentId, job );

        job.setState( JobStatus.WAITING );

        try {
            JobDataMap map = new JobDataMap();
            map.put( ACTUAL_JOB, job );
            map.put( EXECUTION_SERVICE, this );

            switch ( profile.getActivationType() ) {
                case SINGLE:
                    Integer delay = profile.getAttribute(ActivationAttribute.Single.Delay );
                    if ( delay == null ) {
                        delay = 0;
                    }

                    this.scheduler.scheduleJob(
                            newJob(JobExecutionBroker.class)
                                    .usingJobData(map)
                                    .withIdentity(job.getJobId().toString(), JOBS_GROUP )
                                    .build(),
                            newTrigger()
                                    .withSchedule(
                                            simpleSchedule()
                                                    .withRepeatCount(1)
                                                    .withIntervalInSeconds(delay)
                                    )
                                    .build()
                    );
                break;
                case DATE:
                    Date date = profile.getAttribute( ActivationAttribute.Date.Point );
                    if ( date == null ) {
                        throw new RemoteException("Schedule date not specified");
                    }

                    this.scheduler.scheduleJob(
                            newJob(JobExecutionBroker.class)
                                    .usingJobData(map)
                                    .withIdentity(job.getJobId().toString(), JOBS_GROUP)
                                    .build(),
                            newTrigger()
                                    .startAt(date)
                                    .build()
                    );
                break;
                case TIMER:
                    Integer ticks = profile.getAttribute( ActivationAttribute.Timer.Ticks );
                    Integer interval = profile.getAttribute( ActivationAttribute.Timer.Interval );
                    Boolean unlimited = profile.getAttribute( ActivationAttribute.Timer.Unlimited );
                    Integer timerDelay = profile.getAttribute( ActivationAttribute.Timer.Delay );

                    SimpleScheduleBuilder schedule = simpleSchedule();
                    if ( unlimited != null && unlimited || ticks == null ) {
                        schedule.repeatForever();
                    } else {
                        schedule.repeatSecondlyForTotalCount( ticks );
                    }

                    schedule.withIntervalInSeconds(interval);

                    this.scheduler.scheduleJob(
                        newJob(JobExecutionBroker.class)
                            .withIdentity(job.getJobId().toString(), JOBS_GROUP )
                            .usingJobData(map)
                                .build(),
                        newTrigger()
                            .startAt( futureDate(timerDelay, IntervalUnit.SECOND ) )
                            .withSchedule( schedule )
                                .build() );
                break;
                case TRIGGER:
                    throw new UnsupportedOperationException("Specified scheduling method not supported");
            }

            return job.getJobId();
        } catch ( Throwable e ) {
            throw new RemoteException( e.getMessage() );
        }
	}

    @Override
    public Collection<UUID> scheduleJobs( UUID agentId, Collection<IJob> jobs, JobActivationProfile profile ) throws RemoteException {
        Collection<UUID> descriptors = new HashSet<UUID>();

        for ( IJob job : jobs ) {
            descriptors.add( this.scheduleJob( agentId, job, profile ) );
        }

        return descriptors;
    }
	
	@Override
	public String executeScript( UUID agentId, String script ) throws RemoteException {
		IDeployAgentService agent = this.executorConnections.get(agentId);
		if ( agent == null ) {
			throw new RemoteException("Agent with ID: " + agentId + " does not registered");
		}
		
		return agent.executeScript( script );
	}

    @Override
    public void unscheduleJob( UUID jobId ) throws RemoteException {
        try {
            JobKey key = new JobKey( jobId.toString(), JOBS_GROUP );
            if ( this.scheduler.checkExists(key) ) {
                this.scheduler.deleteJob( key);
            }
        } catch ( SchedulerException e ) {
            try {
                log.error("Unable to unschedule task... Trying to restart scheduler instance.");
                this.scheduler.shutdown();
                this.scheduler.start();
            } catch ( Throwable e1 ) {
                throw new RemoteException("Unable to unschedule job...");
            }
        }
    }

	@Override
	public void cancelJob( UUID jobId ) throws RemoteException {
        this.unscheduleJob(jobId);

        for ( UUID agentId : this.executorJobs.keySet() ) {
			for ( UUID job : this.executorJobs.get(agentId) ) {
				if ( job.equals( jobId ) ) {
					log.info("Canceling job: " + jobId );
					this.getConnection(agentId).cancel(jobId);
					break;
				}
			}
		}

	}
	
	protected UUID generateId() {
		return UUID.randomUUID();
	}
	
	protected IJobResult createJobResult( final IJob job ) {
		return new JobResult(job.getJobId());
	}
	
	@Override
	public boolean isComplete(UUID job) {
		return this.completedJobs.get(job) != null;
	}

	@Override
	public boolean isFailed(UUID job) {
		return this.failedJobs.get(job) != null;
	}
	
	public class AgentMonitoringThread implements Runnable {
		private IDeployAgentService agent;
		private UUID id;
		private Integer interval;
		
		public AgentMonitoringThread( UUID id, IDeployAgentService agent, Integer interval ) {
			this.agent = agent;
			this.id = id;
			this.interval = interval;
		}
		
		@Override
		public void run() {
			boolean failed = false;
			
			while( !failed ) {
				try {
					log.info("Pinging agent: " + this.id );
					failed = !this.agent.ping();
					
					try {
						Thread.sleep( this.interval );
					} catch ( InterruptedException e  ) {}
					
					log.info("Ping successful...");
				} catch ( RemoteException e  ) {
					log.info("Unresponsible agent...");
					failed = true;
				}
			}
			
			try {
				log.info("Closing unresponsible connection to agent: " + this.id );
				DeployServerService.this.unregister( this.id );
			} catch ( Throwable e ) {
				log.error("sic...", e);
			}
		}
		
	}

    public static class JobProcessingThread implements Callable<IJobResult> {
        private IJobsManager manager;
        private IJob job;

        public JobProcessingThread( IJobsManager manager, IJob job ) {
            this.manager = manager;
            this.job = job;
        }

        @Override
        public IJobResult call() throws Exception {
            return this.manager.execute(job).get();
        }
    }

	public class JobExecutionThread implements Callable<IJobResult> {
		private IDeployAgentService agent;
		private UUID agentId;
		private IJob job;
		
		public JobExecutionThread( IDeployAgentService agent, UUID agentId, IJob job ) {
			this.agentId = agentId;
			this.agent = agent;
			this.job = job;
		}
		
		@Override
		public IJobResult call() throws ExecutionException {
			try {
				try {
					return this.agent.accept( this.job );
				} catch ( RemoteException e  ) {
					DeployServerService.this.fail( 
						this.agentId,
						this.job.getJobId(),
						new JobException(
							"Job execution failed by agent: " + agentId,
							e ) );
                    return null;
				}
			} catch ( RemoteException e  ) {
                throw new ExecutionException( e.getMessage(), e );
			}
		}
		
	}
}
