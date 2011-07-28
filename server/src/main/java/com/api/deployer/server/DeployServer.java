package com.api.deployer.server;

import java.io.IOException;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.api.daemon.*;

import org.apache.log4j.Logger;

import com.api.commons.config.ConfigException;
import com.api.commons.config.IConfig;
import com.api.commons.events.AbstractEvent;
import com.api.commons.events.IEventListener;
import com.api.commons.net.Utils;
import com.api.commons.net.broadcast.DiscoveryService;

import com.api.daemon.traits.IPublishableDaemon;
import com.api.deployer.server.services.DeployServerService;
import com.api.deployer.services.ClientsFactory;
import com.api.deployer.services.ServerFactory;

public class DeployServer extends AbstractRMIDaemon<DaemonAttributes>
						  implements IPublishableDaemon<Registry,DaemonAttributes>{
	private static final long serialVersionUID = -3022533290026140214L;

	private static final Logger log = Logger.getLogger( DeployServer.class );

	public static class Attributes extends DaemonAttributes {

		protected Attributes( String code ) {
			super(code);
		}

		public static final Attributes DISCOVERY_ENABLED = new Attributes("DeployServer.Attributes.DISCOVERY_ENABLED");
	}
	
	public static class Events extends AbstractEvent {
		private static final long serialVersionUID = 6477259648866377136L;

		public static class StartedEvent extends Events {
			private static final long serialVersionUID = 8692078242068681903L;
			private Date date;
			
			public StartedEvent() {
				this.date = new Date();
			}
			
			public Date getStarted() {
				return this.date;
			}
		}
		
		public static class PublishedEvent extends Events {
			private static final long serialVersionUID = 327356803191676517L;
			private Date date;
			
			public PublishedEvent() {
				this.date = new Date();
			}
			
			public Date getStarted() {
				return this.date;
			}
		}
		
	}
	
	public DeployServer( String contextPath ) throws ConfigException, DaemonException {
		super(contextPath);
		
		this.setThreadExceptionsHandler( new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				log.info("Exception in thread: " + t.getId() );
				log.error( e.getMessage(), e );
			}
		});
	}

	protected Boolean isDiscoveryEnabled() {
		return this.<Boolean>getAttribute( Attributes.DISCOVERY_ENABLED );
	}

	@Override
	public Registry getEndPoint() {
		return this.getRegistry();
	}
	
	@Override
	public void publish() throws DaemonPublishException {
		try {
			String serviceName = this.<String>getAttribute( Attributes.SERVICE_NAME );
			log.info("Starting service with name: " + serviceName );
			
			this.exportService( new DeployServerService( this.getContext(), serviceName ) );
			
			this.raiseEvent( new Events.PublishedEvent() );
		} catch ( Throwable e ) {
			throw new DaemonPublishException( e.getMessage(), e );
		}
	}
	
	@Override
	public void onStarted() throws DaemonPublishException {
		if ( this.doPublishing() ) {
			this.publish();
		}
		
		this.raiseEvent( new Events.StartedEvent() );
	}
	
	@Override
	public void loadConfiguration( IConfig config ) throws ConfigException, DaemonException {
		this.setHost( config.get("server").get("host").value() );
		this.setPort( Integer.valueOf( config.get("server").get("port").value() ) );
		this.setMaxConnections( Integer.valueOf( config.get("server").get("connections").value() ) );
		this.setPath( config.get("server").get("service").value() );

		this.setAttribute( Attributes.DISCOVERY_ENABLED, Boolean.valueOf( config.get("server").get("discoveryEnabled").value() ) );
	}

	@Override
	public Integer getMaxConnections() {
		return this.<Integer>getAttribute( Attributes.MAX_CONNECTIONS );
	}
	
	@Override
	public ExecutorService createThreadExecutor() {
		return Executors.newFixedThreadPool(1);
	}
	
	protected void startDiscoveryService() throws IOException {
		final DiscoveryService service = new DiscoveryService( this.getHost(), Utils.getAvailable( this.getPort() ) );
		service.setAssociatedPort( this.getPort() );
		service.discoverable(true);
		service.discoverer(false);
		
		Thread discoveryThread = new Thread() {
			@Override
			public void run() {
				try {
					service.start();
					
					log.info("Deploy service became discoverable on port " + service.getPort() + "...");
					
					while(true) { 
						try {
                            // TODO: Refactor with delayed @see ThreadExecutor
							Thread.sleep(50);
						} catch ( InterruptedException e ) {}
					}
				} catch ( IOException e ) {
					log.error( e.getMessage(), e );
				}
			}
		};
		discoveryThread.setDaemon(true);
		
		discoveryThread.start();
	}
	
	public static void main( String[] args ) {
		if ( args.length < 1 ) {
			throw new RuntimeException("Path to a spring context not provided");
		}
		
		try {
			final DeployServer server = new DeployServer( args[0] );
			server.loadConfiguration();
			server.setClientsFactory( new ClientsFactory( server.getHost() ) );
			server.setServerFactory( new ServerFactory( server.getHost(), server.getMaxConnections() ) );
			
			server.addEventListener( Events.PublishedEvent.class, new IEventListener<Events.PublishedEvent>() {
				private static final long serialVersionUID = -5415228962066400572L;

				@Override
				public void handleEvent( Events.PublishedEvent event ) {
					try {
						if ( server.isDiscoveryEnabled() ) {
							server.startDiscoveryService();
						}
					} catch ( IOException e ) {
						log.error( "Failed to start discovery service", e  );
					}
				}
			});
			
			server.start();
		} catch ( ConfigException e ) {
			throw new RuntimeException("Daemon configuration exception", e);
		} catch ( DaemonException e ) {
			log.error( e.getMessage(), e );
			throw new RuntimeException("Daemon initialization exception", e);
		}
	}

	@Override
	public boolean doPublishing() {
		return true;
	}

}
