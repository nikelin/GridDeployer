package com.api.deployer.agent;

import com.api.daemon.*;
import com.api.daemon.services.Connector;
import com.api.deployer.execution.services.IArtifactoryService;

import com.api.deployer.execution.services.IDeployServerService;
import com.api.deployer.services.ClientsFactory;
import com.api.deployer.services.ServerFactory;
import org.apache.log4j.Logger;

import java.io.IOException;

import java.net.URI;
import java.rmi.RemoteException;
import java.util.*;
import java.net.MalformedURLException;

import com.api.commons.StringUtils;
import com.api.commons.config.ConfigException;
import com.api.commons.config.IConfig;
import com.api.commons.events.AbstractEvent;
import com.api.commons.events.IEventListener;
import com.api.commons.net.broadcast.DiscoveryService;
import com.api.commons.net.broadcast.DiscoveryService.DiscoveryServiceEvent.DiscoveredEvent;

import com.api.daemon.traits.IPublishableDaemon;

import com.api.deployer.agent.services.DeployAgentService;
import com.api.deployer.execution.IExecutorDescriptor;
import com.api.deployer.execution.IJobExecutor;
import com.api.deployer.execution.IJobsDispatcher;
import com.api.deployer.execution.SimpleExecutorDescriptor;
import com.api.deployer.system.ISystemFacade;

/**
 * Deployment agent daemon which will be accessible thought RMI.
 * His main responsibility is to provide for deploy server ability of 
 * executing custom job on the machine where agent installed.
 * 
 * @author nikelin
 * @see IJobExecutor
 * @see IJobsDispatcher
 */
public class DeployAgent extends AbstractRMIDaemon<DaemonAttributes>
					 implements IPublishableDaemon<IJobExecutor, DaemonAttributes> {

	private static final long serialVersionUID = -639950034781607127L;
	
	private static final Logger log = Logger.getLogger( DeployAgent.class );
	public static final String DEFAULT_HOST = "localhost";
	public static final Integer DEFAULT_PORT = 55456;
	public static final Integer DEFAULT_DISCOVERY_TIMEOUT = 30000;

	public static class Event extends AbstractEvent {
		private static final long serialVersionUID = 2114496283496467647L;

		public static class Published extends Event {
			private static final long serialVersionUID = -5480526841781180865L;
		}
		
		public static class Started extends Event {
			private static final long serialVersionUID = 6264694742039605509L;
		}
		
	}

	public static class Attributes extends AbstractRMIDaemon.Attributes {
		protected Attributes( String code ) {
		    super(code);
		}

		public static final Attributes MOUNTING_PATH = new Attributes("MOUNT_PATH");

		public static class Accessor {
			private static Map<IDaemonAttributes, DaemonAttribute> holder =
							new HashMap<IDaemonAttributes, DaemonAttribute>();

			public static <V, T extends IDaemonAttributes> V get( T id ) {
				DaemonAttribute value;
				if (  ( value = holder.get(id) ) != null ) {
					return (V) value.getObjectValue();
				}

				return null;
			}

			public static <T extends IDaemonAttributes> void set( T id, Object value ) {
				holder.put(id, new DaemonAttribute( id.name(), value ) );
			}

			public static DaemonAttribute[] list() {
				return holder.values().toArray( new DaemonAttribute[ holder.size() ] );
			}

		}
	}
	
	private Date startedOn;
	private UUID id;
	
	/**
	 * Deploy server client service
	 */
	private IDeployServerService deployServerService;
	
	/**
	 * Current agent access service
	 */
	private DeployAgentService service;
	
	public DeployAgent() throws DaemonException, ConfigException {
		this(null);
	}
	
	public DeployAgent( String contextPath ) throws DaemonException, ConfigException {
		super(contextPath);
		
		this.setThreadExceptionsHandler( new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				log.info("Exception in thread: " + t.getId() );
				log.error( e.getMessage(), e );
			}
		});
		
		this.init();
	}
	
	public void setId( UUID id ) {
		this.id = id;
	}
	
	public UUID getId() {
		return this.id;
	}
	
	protected ISystemFacade getSystem() {
		return this.getContext().getBean(ISystemFacade.class);
	}
	
	private void init() throws DaemonException {
		try {
			if ( !this.getSystem().isUnderRoot() ) {
				throw new RuntimeException("Process must be started with root privileges!");
			}

			this.setServerFactory( new ServerFactory( this.getHost(), this.getMaxConnections() ) );
			this.setClientsFactory( new ClientsFactory( this.getHost() ) );
		} catch ( IOException e ) {
			throw new DaemonException("I/O exception");
		}
	}
	
	protected IExecutorDescriptor createDescriptor() throws IOException {
		return new SimpleExecutorDescriptor(
			this.getSystem().detectStationId(),
			this.prepareServiceLocation(
					this.getHost(), this.getPort(), this.getPath() ),
			this.startedOn );
	}

	@Override
	protected void onStarted() throws DaemonException {
	}

	protected void onServerBinded() throws DaemonException {
		try {
			this.startedOn = new Date();

			this.publish();

			log.info("Agent event");
			IExecutorDescriptor descriptor = this.createDescriptor() ;
			log.info( "UUID:" + descriptor.getStationID() );

			UUID id = this.getDeployServerService()
									  .register(descriptor) ;

			this.service.setId(id);
		} catch ( Throwable e ) {
			log.error( e.getMessage(), e );
			this.stop();
		}
	}
	
	@Override
	public synchronized void start() throws DaemonException {		
		super.start();
		
		log.info("Trying to locate RMI server");
		Connector connector = new Connector( this.getMaxAttempts() );

		IDeployServerService service = (IDeployServerService) connector.find(
			this.<String>getAttribute( Attributes.SERVER_HOST ),
			this.<Integer>getAttribute( Attributes.SERVER_PORT ),
			this.<String>getAttribute( Attributes.SERVER_SERVICE )
		);

		if ( service == null ) {
			throw new DaemonException("Cannot locate deploy server instance");
		}

		this.setDeployServerService( service );

		log.info("Deploy server binded successfuly");

		this.onServerBinded();
	}
	
	private void setDeployServerService( IDeployServerService service ) {
		this.deployServerService = service;
	}
	
	public IDeployServerService getDeployServerService() {
		return this.deployServerService;
	}
	
	@Override
	public IJobExecutor getEndPoint() {
		return this.service;
	}

	@Override
	public void setAttribute(DaemonAttributes name, Object value) {
		Attributes.Accessor.set(name, value);
	}

	@Override
	public <V> V getAttribute( DaemonAttributes name) {
		return Attributes.Accessor.<V, DaemonAttributes>get(name);
	}

	@Override
	public DaemonAttribute[] getAttributes() {
		return Attributes.Accessor.list();
	}

	@Override
	public void publish() throws DaemonPublishException {
		try {
			String serviceName = this.<String>getAttribute( DaemonAttributes.SERVICE_NAME );
			log.info("Starting service with name: " + serviceName );

			this.service = new DeployAgentService( serviceName, this.getDeployServerService(), this.connectArtifactory(), this.getContext() );

			this.exportService(this.service);
		} catch ( Throwable e ) {
			throw new DaemonPublishException( e.getMessage(), e );
		}
	}

	protected IArtifactoryService connectArtifactory() throws RemoteException {
		URI artifactoryURI = this.getDeployServerService().getArtifactoryURI();
		if ( artifactoryURI == null ) {
			throw new RemoteException("Artifactory URI is unknown");
		}

		Connector connector = new Connector();
		IArtifactoryService artifactoryService = (IArtifactoryService) connector.find(
			artifactoryURI.getHost(),
			artifactoryURI.getPort(),
			artifactoryURI.getPath()
		);

		if ( artifactoryService == null ) {
			throw new RemoteException("Artifactory service not found!");
		}

		log.info("Artifactory service binded!");


		return artifactoryService;
	}

	@Override
	public boolean doPublishing() {
		return true;
	}
	
	@Override
	public void loadConfiguration( IConfig config ) throws ConfigException {
		this.setMaxConnections( Integer.valueOf( config.get("agent").get("connections").value() ) );
		this.setPath( config.get("agent").get("service").value() );
		
		this.setHost( config.get("agent").get("host").value() );
		this.setPort( Integer.valueOf( config.get("agent").get("port").value() ) );
		
		if ( null == this.getAttribute( Attributes.SERVER_HOST ) ) {
			this.setAttribute( Attributes.SERVER_HOST, config.get("server").get("host").value() );
		}

		if ( null == this.getAttribute( Attributes.SERVER_PORT ) ) {
			this.setAttribute( Attributes.SERVER_PORT, Integer.valueOf( config.get("server").get("port").value() ) );
		}

		this.setAttribute( Attributes.MOUNTING_PATH, config.get("system").get("mountingPath").value() );
		this.setAttribute( Attributes.SERVER_SERVICE, config.get("server").get("service").value() );
		this.setAttribute( DaemonAttributes.MAX_ATTEMPTS, Integer.valueOf( config.get("agent").get("maxAttempts").value() ) );
		
		this.changeState( DaemonState.INITIALIZED );
	}
	
	protected static void printLine( String message ) {
		System.out.println( message );
	}
	
	protected static String readLine() throws IOException {
		StringBuffer result = new StringBuffer();
		char buff;
		while( '\n' != ( buff = (char) System.in.read() ) ) {
			if ( buff != -1 ) {
				result.append(buff);
			}
		}
		
		return result.toString();
	}
	
	/**
	 * Main method
	 * 
	 * @param args
	 */
	public static void main( String[] args ) {
		if ( args.length < 1 ) {
			throw new RuntimeException("Path to a spring context not provided");
		}
		
		printLine("Starting GridDeploy agent daemon...");
		printLine( StringUtils.repeat("-", 20) );
		
		try {
			final DeployAgent agent = new DeployAgent( args[0] );
			
			if ( args.length == 1 ) {
				printLine("There is also supports arguments-based agent configuration.");
				printLine("$ - agent [manual|search] ...");
				printLine("Manual configured startup:");
				printLine(String.format(
						"$ - agent manual [host=%s] [port=%d]",
						DEFAULT_HOST, DEFAULT_PORT ) );
				printLine("Broadcast-based location:");
				printLine( String.format( 
					"$ - agent search [selfAddress=%s] [port=%d] [timeout=%d]",  
					DEFAULT_HOST, DEFAULT_PORT, DEFAULT_DISCOVERY_TIMEOUT ) );
				printLine( StringUtils.repeat("-", 20) );
				
				interactionConfig(agent);
			} else {
				argumentsBasedConfig( Arrays.copyOfRange( args, 1, args.length), agent);
			}
		} catch ( ConfigException e ) {
			throw new RuntimeException("Daemon configuration exception", e);
		} catch ( DaemonException e ) {
			throw new RuntimeException("Daemon initialization exception", e);
		}
	}
	
	private static void argumentsBasedConfig( String[] args, final DeployAgent agent ) throws DaemonException, ConfigException {
		String method = args[0];
		if ( method.equals( "manual" ) ) {
			agent.setAttribute( DeployAgent.Attributes.SERVER_HOST, args.length < 2 || args[1].isEmpty() ?  DEFAULT_HOST : args[1] );
			agent.setAttribute( DeployAgent.Attributes.SERVER_PORT, args.length < 3 ? DEFAULT_PORT : parseInteger( args[2], DEFAULT_PORT )  );
			
			agent.start();
		} else if ( method.equals( "search" ) ) {
			startDiscovery( agent, args[1], parseInteger( args[3], DEFAULT_DISCOVERY_TIMEOUT ), Integer.valueOf( args[2], DEFAULT_PORT ) );
		} else if ( method.equals("config") ) {
            return;
        } else {
            throw new IllegalArgumentException("Unknown configuration option");
        }
	}
	
	private static Integer parseInteger( String value, Integer defaultValue ) {
		try {
			return Integer.valueOf(value);
		} catch ( NumberFormatException e  ) {
			return defaultValue;
		}
	}
		
	private static void startDiscovery( final DeployAgent agent, String broadcastAddress,
			Integer timeout, Integer discoveryPort ) {
		final DiscoveryService service = new DiscoveryService(broadcastAddress, agent.getPort() );
		service.setAssociatedPort( agent.getPort() );
		service.setTimeout( timeout );
		service.discoverable(true);
		service.discoverer(true);
		service.addDiscoveryPort( discoveryPort );
		
		service.addEventListener( DiscoveredEvent.class, new IEventListener<DiscoveredEvent>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 7714913618955166131L;

			@Override
			public void handleEvent( DiscoveredEvent event ) {
				Integer port = event.getPort();
				String host = event.getAddress().getHostAddress();
				
				log.info("Connection established...");
				log.info("Deploy server located on " + host + " at " + port + " port" );
				
				agent.setAttribute( DeployAgent.Attributes.SERVER_HOST, host );
				agent.setAttribute( DeployAgent.Attributes.SERVER_PORT, port );
				
				try {
					service.discoverable(false);
					service.discoverer(false);
					
					agent.start();
				} catch ( DaemonException e  ) {
					log.error( e.getMessage(), e );
					System.exit(0);
				}
			}
		});
		
		try {
			new Thread() {
				@Override
				public void run() {
					try {
						log.info("Starting discovery service...");
						
						service.start();
					} catch ( IOException e ) {
						log.info("Unexpected interaction exception (I/O)", e );
						System.exit(0);
					} 
				}
			}.start();

			Thread.sleep( timeout );
		} catch ( InterruptedException e ) {}
		
		if ( !agent.getState().equals( DaemonState.STARTED ) ) {
			log.info("Discovery timeout reached...");
		} else {
			do { 
				try {
					Thread.sleep(500);
				} catch ( InterruptedException e ) {}
			} while ( agent.getState().equals( DaemonState.STARTED ) );
		}
		
		log.info("Exiting...");
	}
	
	private static void interactionConfig( final DeployAgent agent ) {
		try {
			Integer method = null;
			do {
				if ( method != null ) {
					printLine("Wrong method value entered...");
				}
				
				printLine("Deploy server location method:");
				printLine("0 - manual ");
				printLine("1 - broadcast lookup");
				printLine("Choose:");
				method = Integer.valueOf( readLine() );
			} while ( !( method == 0 || method == 1 ) );
			
			switch ( method ) {
			case 0:
				printLine(String.format( "Enter deploy server host[default=%s]:", DEFAULT_HOST ) );
				String host = readLine();
				if ( host.isEmpty() ) {
					host = DEFAULT_HOST;
				}
				System.out.println( "\n" );
				
				printLine( String.format( "Enter deploy server port[default=%s]:", DEFAULT_HOST ) );
				Integer port = parseInteger( readLine(), DEFAULT_PORT );
				System.out.println( "\n" );
				
				agent.setAttribute( DeployAgent.Attributes.SERVER_HOST, host);
				agent.setAttribute( DeployAgent.Attributes.SERVER_PORT, port );
				
				agent.start();
			break;
			case 1:
				printLine(String.format( "Enter broadcast device IPv4 address [default %s]:", DEFAULT_HOST) );
				String broadcastAddress = readLine();
				if ( broadcastAddress.isEmpty() ) {
					broadcastAddress = DEFAULT_HOST;
				}
				
				printLine( String.format( "Set lookup timeout [default %d]:", DEFAULT_DISCOVERY_TIMEOUT) );
				Integer timeout = parseInteger( readLine(), DEFAULT_DISCOVERY_TIMEOUT );
				
				printLine(String.format( "Enter deploy server port [default %s]", DEFAULT_PORT) );
				Integer discoveryPort = parseInteger( readLine(), DEFAULT_PORT );
				
				startDiscovery( agent, broadcastAddress, timeout, discoveryPort );
			break;
			}		
		} catch ( IOException e  ) {
			throw new RuntimeException("Undefined I/O exception throwed...", e);
		} catch ( DaemonException e  ) {
			throw new RuntimeException("Daemon startup exception", e);
		}
	}
	
}
