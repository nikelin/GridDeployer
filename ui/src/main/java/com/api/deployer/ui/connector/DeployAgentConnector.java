package com.api.deployer.ui.connector;

import com.api.deployer.execution.IExecutorDescriptor;
import com.api.deployer.execution.services.IDeployServerService;
import com.api.deployer.io.transport.IDestination;
import com.api.deployer.jobs.JobScope;
import com.api.deployer.notifications.INotification;
import com.api.deployer.services.ClientsFactory;
import com.api.deployer.services.ServerFactory;
import com.api.deployer.system.devices.IDevice;
import com.api.deployer.system.processes.ISystemProcess;
import com.redshape.daemon.AbstractRMIDaemon;
import com.redshape.daemon.DaemonException;
import com.redshape.daemon.DaemonState;
import com.redshape.daemon.IDaemonAttributes;
import com.redshape.daemon.events.ServiceBindExceptionEvent;
import com.redshape.daemon.jobs.IJob;
import com.redshape.daemon.jobs.JobException;
import com.redshape.daemon.jobs.activation.JobActivationProfile;
import com.redshape.daemon.jobs.result.IJobResult;
import com.redshape.daemon.services.Connector;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.UIEvents;
import com.redshape.utils.IFilter;
import com.redshape.utils.config.ConfigException;
import com.redshape.utils.config.IConfig;
import com.redshape.utils.events.IEventListener;
import org.apache.log4j.Logger;

import java.net.URI;
import java.rmi.RemoteException;
import java.util.*;

public class DeployAgentConnector extends AbstractRMIDaemon<DeployAgentConnector.Attributes>
							  	  implements IDeployServerService {
	private static final long serialVersionUID = -2473314200826752262L;
	
	private static Logger log = Logger.getLogger( DeployAgentConnector.class );
	private IDeployServerService service;
	
	public DeployAgentConnector() throws DaemonException, ConfigException {
		this(null);
	}
	
	public DeployAgentConnector(String contextPath) throws DaemonException, ConfigException {
		super(contextPath);

		this.setAttribute( Attributes.MAX_ATTEMPTS, 5);
	}
	
	public static class Event extends UIEvents {
		
		protected Event( String message ) {
			super(message);
		}
		
		public static class Action extends Event {
			
			protected Action( String message ) {
				super(message);
			}
			
			public static final Action Discovery = new Action("DeployAgentConnector.Event.Action.Discovery");
			public static final Action Connect = new Action("DeployAgentConnector.Event.Action.Connect");
			public static final Action Disconnect = new Action("DeployAgentConnector.Event.Action.Disconnect");
			public static final Action Reboot = new Action("DeployAgentConnector.Event.Action.Reboot");
			public static final Action Shutdown = new Action("DeployAgentConnector.Event.Action.Reboot");
		}
		
		public static class Job extends Event {
			
			protected Job( String code ) {
				super(code);
			}
			
			public static final Job Canceled = new Job("DeployAgentConnector.Event.Job.Canceled");
			public static final Job Cancel = new Job("DeployAgentConnector.Event.Job.Cancel");
			public static final Job Scheduled = new Job("DeployAgentConnector.Event.Job.Scheduled");
			public static final Job Complete = new Job("DeployAgentConnector.Event.Job.Complete");
			public static final Job Fail = new Job("DeployAgentConnector.Event.Job.Fail");
		}
		
		public static final Event Reconnect = new Event("DeployAgentConnector.Event.Reconnect");
		public static final Event Disconnected = new Event("DeployAgentConnector.Event.Disconnected");
		public static final Event Connected = new Event("DeployAgentConnector.Event.Connected");
		public static final Event Fail = new Event("DeployAgentConnector.Event.Fail");
		public static final Event Provided = new Event("DeployAgentConnector.Event.Provided");
		public static final Event Broadcast = new Event("DeployAgentConnector.Event.Broadcast");
	}

	public enum Attributes implements IDaemonAttributes {
		REMOTE_HOST,
		REMOTE_PORT,
		REMOTE_SERVICE,
		MAX_ATTEMPTS,
		MAX_CONNECTIONS
	}

	public void setHost( String host ) {
		this.setAttribute( Attributes.REMOTE_HOST, host );
	}

	@Override
	public Integer getMaxConnections() {
		return this.getAttribute( Attributes.MAX_CONNECTIONS );
	}

	@Override
	public String getHost() {
		return this.getAttribute(Attributes.REMOTE_HOST);
	}

	public void setPort( Integer port ) {
		this.setAttribute( Attributes.REMOTE_PORT, port );
	}

	@Override
	public Integer getPort() {
		return this.getAttribute( Attributes.REMOTE_PORT );
	}

	public void setMaxAttempts( Integer attempts ) {
		this.setAttribute( Attributes.MAX_ATTEMPTS, attempts );
	}

	@Override
	public Integer getMaxAttempts() {
		return this.getAttribute(Attributes.MAX_ATTEMPTS);
	}

	public void setPath( String path ) {
		this.setAttribute( Attributes.REMOTE_SERVICE, path );
	}

	@Override
	public String getPath() {
		return this.getAttribute( Attributes.REMOTE_SERVICE );
	}
	
	public boolean isConnected() {
		return this.service != null;
	}

	@Override
	public String getServiceName() {
		return this.getPath();
	}

	@Override
	public void sendNotification( INotification notification ) throws RemoteException {
		this.getDeployService().sendNotification( notification );
	}

	@Override
	public void registerNotifyPoint( IDestination destination ) throws RemoteException {
		this.getDeployService().registerNotifyPoint( destination );
	}

	@Override
	public void loadConfiguration(IConfig config)
			throws DaemonException, ConfigException {
	}
	
	protected void setDeployService( IDeployServerService service ) {
		this.service = service;
	}
	
	protected IDeployServerService getDeployService() {
		return this.service;
	}

	protected void onExceptionEvent( ServiceBindExceptionEvent event ) {
		this.changeState( DaemonState.ERROR );
		Dispatcher.get().forwardEvent( DeployAgentConnector.Event.Fail );
	}

	protected void onConnection( IDeployServerService service ) {
		this.setDeployService( service );
		Dispatcher.get().forwardEvent( DeployAgentConnector.Event.Connected );
	}

	@Override
	public void start() throws DaemonException {
		this.setServerFactory( new ServerFactory( this.getHost(), this.getMaxConnections() ) );
		this.setClientsFactory( new ClientsFactory( this.getHost() ) );

		super.start();
		
		this.getThreadExecutor().execute( new Runnable() {
			@Override
			public void run() {
				Connector<IDeployServerService> connector = new Connector(
						DeployAgentConnector.this.getMaxAttempts() );


				connector.addEventListener(
					ServiceBindExceptionEvent.class,
					new IEventListener<ServiceBindExceptionEvent>() {
						@Override
						public void handleEvent(ServiceBindExceptionEvent event) {
							DeployAgentConnector.this.onExceptionEvent( event );
						}
					}
				);

				IDeployServerService service = connector.find(
					DeployAgentConnector.this.getHost(),
					DeployAgentConnector.this.getPort(),
					DeployAgentConnector.this.getPath()
				);

				if ( service != null ) {
					DeployAgentConnector.this.onConnection(service);
				}
			}
		});
	}

	@Override
	public void stop() throws DaemonException {
		super.stop();

		this.changeState( DaemonState.STOPPED );

		Dispatcher.get().forwardEvent( DeployAgentConnector.Event.Disconnected );
	}
	
	@Override
	public void startRegistry() {
		System.out.println("Registry startup declined...");
	}

	@Override
	public URI getArtifactoryURI() throws RemoteException {
		return this.getDeployService().getArtifactoryURI();
	}

	@Override
	public void setArtifactoryURI(URI uri) throws RemoteException {
		this.getDeployService().setArtifactoryURI( uri );
	}

	@Override
	public Map<UUID, IExecutorDescriptor> getConnectedExecutors()
			throws RemoteException {
		if ( !this.isConnected() ) {
			return new HashMap<UUID, IExecutorDescriptor>();
		}
		
		return this.getDeployService().getConnectedExecutors();
	}

    @Override
    public Collection<IJobResult> getExecutionResults() throws RemoteException {
        if ( !this.isConnected() ) {
            return new HashSet<IJobResult>();
        }

        return this.getDeployService().getExecutionResults();
    }

    @Override
    public Collection<IJobResult> getExecutionResults(UUID jobId) throws RemoteException {
        if ( !this.isConnected() ) {
            return new HashSet<IJobResult>();
        }

        return this.getDeployService().getExecutionResults(jobId);
    }

    @Override
	public Map<UUID, IJob> getJobs(UUID agentId) throws RemoteException {
		if ( !this.isConnected() ) {
			return new HashMap<UUID, IJob>();
		}
		
		return this.getDeployService().getJobs(agentId);
	}

	@Override
	public Collection<IDevice> getDevices(UUID agentId) throws RemoteException {
		if ( !this.isConnected() ) {
			return new HashSet<IDevice>();
		}
		
		return this.getDeployService().getDevices(agentId);
	}

	@Override
	public Collection<IDevice> getDevices(UUID agentId, IFilter<IDevice> filter)
			throws RemoteException {
		if ( !this.isConnected() ) {
			return new HashSet<IDevice>();
		}
		
		return this.getDeployService().getDevices(agentId, filter);
	}

	@Override
	public String getHostname(UUID agentId) throws RemoteException {
		if ( !this.isConnected() ) {
			return null;
		}
		
		return this.getDeployService().getHostname(agentId);
	}

	@Override
	public Collection<ISystemProcess> getProcesses(UUID agentId)
			throws RemoteException {
		if ( !this.isConnected() ) {
			return new HashSet<ISystemProcess>();
		}
		
		return this.getDeployService().getProcesses(agentId);
	}

    @Override
    public Collection<IJobResult> executeJobs( JobScope scope, UUID agentId, Collection<IJob> jobs) throws RemoteException {
        if ( !this.isConnected() ) {
            return new HashSet<IJobResult>();
        }

        Collection<IJobResult> results = this.getDeployService().executeJobs( scope, agentId, jobs );

        Dispatcher.get().forwardEvent( new AppEvent( DeployAgentConnector.Event.Job.Scheduled, results ) );

        return results;
    }

    @Override
    public IJobResult executeJob(JobScope scope, UUID agentId, IJob job) throws RemoteException {
        if ( !this.isConnected() ) {
            return null;
        }

        IJobResult result = this.getDeployService().executeJob(scope, agentId, job);

        Dispatcher.get().forwardEvent( new AppEvent( DeployAgentConnector.Event.Job.Scheduled, result ) );

        return result;
    }

    @Override
    public IJob getJob(UUID jobId) throws RemoteException {
        if ( !this.isConnected() ) {
            return null;
        }

        return this.getDeployService().getJob(jobId);
    }

    @Override
	public Collection<UUID> scheduleJobs(UUID agentId, Collection<IJob> jobs, JobActivationProfile profile) throws RemoteException {
        if ( !this.isConnected() ) {
            return new HashSet<UUID>();
        }

	    return this.getDeployService().scheduleJobs( agentId, jobs, profile );
	}

    @Override
    public void pauseJob(UUID job) throws RemoteException {
        if ( !this.isConnected() ) {
            return;
        }

        this.getDeployService().pauseJob( job );
    }

    @Override
    public void resumeJob(UUID job) throws RemoteException {
        if ( !this.isConnected() ) {
            return;
        }

        this.getDeployService().resumeJob( job );
    }

    @Override
	public UUID scheduleJob(UUID agentId, IJob job, JobActivationProfile profile ) throws RemoteException {
		if ( !this.isConnected() ) {
			return null;
		}

        UUID descriptor = this.getDeployService().scheduleJob(agentId, job, profile);

		Dispatcher.get().forwardEvent( new AppEvent( DeployAgentConnector.Event.Job.Scheduled, descriptor ) );

        return descriptor;
	}

    @Override
    public void unscheduleJob(UUID jobId) throws RemoteException {
        if ( !this.isConnected() ) {
            return;
        }

        this.getDeployService().unscheduleJob( jobId );
    }

    @Override
    public Collection<IJob> getScheduledJobs() throws RemoteException {
        if ( !this.isConnected() ) {
            return new HashSet<IJob>();
        }

        return this.getDeployService().getScheduledJobs();
    }

    @Override
	protected void onStarted() throws DaemonException {
		log.info("Connector started up...");
	}

	@Override
	public UUID register(IExecutorDescriptor descripton) throws RemoteException {
		throw new RuntimeException();
	}

	@Override
	public void unregister(UUID executorId) throws RemoteException {
		throw new RuntimeException();
	}

	@Override
	public void complete( UUID agentId, UUID jobId ) throws RemoteException {
		throw new RuntimeException();
	}

	@Override
	public void fail( UUID agentId, UUID jobId, JobException e)
			throws RemoteException {
		throw new RuntimeException();
	}

	@Override
	public IDevice getDevice(UUID agentId, IFilter<IDevice> filter)
			throws RemoteException {
		if ( !this.isConnected() ) {
			return null;
		}
		
		return this.getDeployService().getDevice(agentId, filter);
	}

	@Override
	public String executeScript( UUID agentId, String script ) throws RemoteException {
		return this.getDeployService().executeScript( agentId, script );
	}

	@Override
	public void cancelJob(UUID job) throws RemoteException {
		if ( !this.isConnected() ) {
			return;
		}
		
		Dispatcher.get().forwardEvent( new AppEvent(
				DeployAgentConnector.Event.Job.Canceled, job
		) );
		
		this.getDeployService().cancelJob( job );
	}

	@Override
	public Integer getProgress( UUID agentId, UUID jobId ) throws RemoteException {
		if ( !this.isConnected() ) {
			return 0;
		}

		return this.getDeployService().getProgress(agentId, jobId);
	}

	@Override
	public boolean isComplete(UUID job) throws RemoteException {
		if (  !this.isConnected() ) {
			return false;
		}
		
		return this.getDeployService().isComplete(job);
	}

	@Override
	public boolean isFailed(UUID job) throws RemoteException {
		if ( !this.isConnected() ) {
			return false;
		}
		
		return this.getDeployService().isFailed(job);
	}
	
}
