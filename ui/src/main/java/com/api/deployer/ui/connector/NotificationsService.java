package com.api.deployer.ui.connector;

import com.api.deployer.execution.services.INotificationService;
import com.api.deployer.notifications.INotification;
import com.api.deployer.ui.components.notifications.NotificationsComponent;
import com.redshape.daemon.AbstractRMIDaemon;
import com.redshape.daemon.DaemonAttributes;
import com.redshape.daemon.DaemonException;
import com.redshape.daemon.services.ClientsFactory;
import com.redshape.daemon.services.ServerFactory;
import com.redshape.daemon.traits.IPublishableDaemon;
import com.redshape.ui.Dispatcher;
import com.redshape.utils.config.ConfigException;
import com.redshape.utils.config.IConfig;
import com.redshape.utils.events.AbstractEvent;
import com.redshape.utils.events.AbstractEventDispatcher;
import com.redshape.utils.events.DelegateEventListener;
import com.redshape.utils.events.IEvent;
import org.apache.log4j.Logger;

import java.rmi.RemoteException;

/**
 * @author nikelin
 * @date 21/04/11
 * @package com.api.deployer.ui.connector
 */
public class NotificationsService extends AbstractRMIDaemon<DaemonAttributes>
								implements IPublishableDaemon<INotificationService, DaemonAttributes> {
	private static final Logger log = Logger.getLogger(NotificationsService.class);
	private INotificationService endPoint;
	private boolean doPublishing;

	public NotificationsService( IConfig config ) throws DaemonException, ConfigException {
		super(null);

		this.setConfig( config );

		this.loadConfiguration(config);

        this.setClientsFactory(new ClientsFactory(this.getHost()));
        this.setServerFactory( new ServerFactory( this.getHost(), this.getMaxConnections() ) );
	}

	@Override
	public boolean doPublishing() {
		return true;
	}

	public static void onNotification( IEvent event ) {
		log.info("Notification!");
		Dispatcher.get().forwardEvent(NotificationsComponent.Events.Action.Received, event.<INotification>getArg(0) );
	}

	@Override
	public boolean ping() {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public String status() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void publish() throws DaemonException {
		try {
			ServiceImpl service = new ServiceImpl( this.getPath() );
			service.addEventListener( IEvent.class,
					new DelegateEventListener(
						NotificationsService.class.getCanonicalName(),
						"onNotification"
					) );

			this.endPoint = service;

			this.exportService( service );
		} catch ( Throwable e ) {
			throw new DaemonException("Unable to start notifications listening service", e);
		}
	}

	@Override
	protected void onStarted() throws DaemonException {
		this.publish();
	}

	@Override
	public INotificationService getEndPoint() {
		return this.endPoint;
	}

	@Override
	public void loadConfiguration(IConfig configLocation) throws DaemonException, ConfigException {
		if ( configLocation == null ) {
            return;
        }

        this.setHost( configLocation.get("notifications").get("host").value() );
		this.setPort( Integer.valueOf(configLocation.get("notifications").get("port").value()) );
		this.setPath( configLocation.get("notifications").get("serviceName").value() );
	}

	public class ServiceImpl extends AbstractEventDispatcher implements INotificationService {
		private String name;

		public ServiceImpl( String name ) {
			this.name = name;
		}

		@Override
		public void receive(final INotification notification) throws RemoteException {
			this.raiseEvent( new AbstractEvent() {
				{
					this.setArgs( new Object[] { notification } );
				}
			});
		}

		@Override
		public String getServiceName() throws RemoteException {
			return this.name;
		}
	}
}
