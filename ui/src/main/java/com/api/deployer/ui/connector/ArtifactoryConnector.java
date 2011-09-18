package com.api.deployer.ui.connector;

import com.api.deployer.backup.artifactory.ArtifactoryVersion;
import com.api.deployer.backup.artifactory.artifacts.IArtifact;
import com.api.deployer.execution.services.IArtifactoryService;
import com.api.deployer.io.transport.IDestination;
import com.redshape.daemon.DaemonException;
import com.redshape.daemon.events.ServiceBindExceptionEvent;
import com.redshape.daemon.services.Connector;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.events.EventType;
import com.redshape.utils.IFilter;
import com.redshape.utils.events.IEventListener;
import org.apache.log4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author nikelin
 * @date 13/04/11
 * @package com.api.deployer.ui.connector
 */
public class ArtifactoryConnector implements IArtifactoryService {
	private static final Logger log = Logger.getLogger(ArtifactoryConnector.class);

	private String host;
	private Integer port;
	private String path;

	private boolean isConnected;

	public static class Event extends EventType {

		protected Event( String message ) {
			super(message);
		}

		public static final Event Failed = new Event("ArtifactoryConnector.Event.Failed");
		public static final Event Connected = new Event("ArtifactoryConnector.Event.Connected");
	}

	private IArtifactoryService service;
	private ExecutorService threadExecutor;

	public ArtifactoryConnector() {
		this(null, null);
	}

	public ArtifactoryConnector( String host, Integer port ) {
		super();

		this.threadExecutor = Executors.newFixedThreadPool(5);
		this.host = host;
		this.port = port;
	}

	public URI getURI() throws URISyntaxException {
		return new URI( "rmi://" + this.getHost() + ":" + this.getPort() + "/" + this.getPath() );
	}

	protected ExecutorService getThreadExecutor() {
		return this.threadExecutor;
	}

	public boolean isConnected() {
		return this.isConnected;
	}

	private void setService( IArtifactoryService service ) {
		this.service = service;
	}

	protected IArtifactoryService getService() {
		return this.service;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath( String path ) {
		this.path = path;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	protected void onConnection( IArtifactoryService service ) {
		log.info("Connection established!");
		this.isConnected = true;
		this.setService(service);
		Dispatcher.get().forwardEvent( ArtifactoryConnector.Event.Connected );
	}

	protected void onExceptionEvent( ServiceBindExceptionEvent event ) {
		log.info("Connection exception!", event.getException() );

		this.stop();

		Dispatcher.get().forwardEvent( ArtifactoryConnector.Event.Failed );
	}

	public void start() throws DaemonException {
		this.getThreadExecutor().execute( new Runnable() {
			@Override
			public void run() {
				Connector<IArtifactoryService> connector = new Connector();

				connector.addEventListener(
					ServiceBindExceptionEvent.class,
					new IEventListener<ServiceBindExceptionEvent>() {
						@Override
						public void handleEvent(ServiceBindExceptionEvent event) {
							ArtifactoryConnector.this.onExceptionEvent( event );
						}
					}
				);

				IArtifactoryService service = connector.find(
					ArtifactoryConnector.this.getHost(),
					ArtifactoryConnector.this.getPort(),
					ArtifactoryConnector.this.getPath()
				);

				if ( service != null ) {
					ArtifactoryConnector.this.onConnection(service);
				}
			}
		});
	}

	public void stop() {
		this.isConnected = false;
		this.service = null;
	}

	@Override
	public IDestination getDestination() throws RemoteException {
		return this.getService().getDestination();
	}

	@Override
	public Collection<IArtifact> getList() throws RemoteException {
		return getService().getList();
	}

	@Override
	public Collection<IArtifact> findArtifacts(IFilter<IArtifact> filter) throws RemoteException {
		return getService().findArtifacts(filter);
	}

	@Override
	public IArtifact findArtifact(IFilter<IArtifact> filter) throws RemoteException {
		return getService().findArtifact(filter);
	}

	@Override
	public void addArtifact(IArtifact artifact) throws RemoteException {
		getService().addArtifact(artifact);
	}

	@Override
	public void removeArtifact(UUID artifactId) throws RemoteException {
		getService().removeArtifact(artifactId);
	}

	@Override
	public ArtifactoryVersion getIndexVersion() throws RemoteException {
		return getService().getIndexVersion();
	}

	@Override
	public boolean getIndexStatus() throws RemoteException {
		return getService().getIndexStatus();
	}

	@Override
	public String getServiceName() throws RemoteException {
		return getService().getServiceName();
	}
}
