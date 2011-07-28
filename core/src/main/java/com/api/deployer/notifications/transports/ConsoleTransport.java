package com.api.deployer.notifications.transports;

import com.api.daemon.services.Connector;
import com.api.deployer.execution.services.INotificationService;
import com.api.deployer.io.transport.IDestination;
import com.api.deployer.notifications.INotification;
import com.api.deployer.notifications.ITransport;
import com.api.deployer.notifications.NotificationException;

import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;

/**
 * @author nikelin
 * @date 21/04/11
 * @package com.api.deployer.notifications.transports
 */
public class ConsoleTransport implements ITransport {
	private Connector<INotificationService> connector;

	public ConsoleTransport() {
		this( new Connector<INotificationService>() );
	}

	public ConsoleTransport( Connector<INotificationService> connector ) {
		this.connector = connector;
	}

	protected Connector<INotificationService> getConnector() {
		return this.connector;
	}

	@Override
	public boolean isSupports(IDestination destination) throws URISyntaxException {
		return destination.getURI().getScheme().equals("console");
	}

	@Override
	public void send(INotification notification, IDestination endPoint) throws NotificationException {
		try {
			URI uri = endPoint.getURI();

			INotificationService service = this.getConnector().find(
					uri.getHost(), uri.getPort(), uri.getPath().replace("/", ""));

			if ( service == null ) {
				throw new NotificationException("Destination is unreachable!");
			}

			service.receive( notification );
		} catch ( URISyntaxException e ) {
			throw new NotificationException("Malformed destination address", e );
		} catch ( RemoteException e ) {
			throw new NotificationException("Notification send exception", e );
		}
	}
}
