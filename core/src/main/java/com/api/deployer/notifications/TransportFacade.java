package com.api.deployer.notifications;

import com.api.deployer.io.transport.IDestination;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author nikelin
 * @date 21/04/11
 * @package com.api.deployer.notifications
 */
public class TransportFacade implements ITransportFacade {
	private Collection<IDestination> endpoints = new HashSet<IDestination>();
	private Map<String, ITransport> transports = new HashMap<String, ITransport>();

	@Override
	public void addEndPoint(IDestination endPoint) {
		this.endpoints.add(endPoint);
	}

	@Override
	public Collection<IDestination> getEndPoints() {
		return this.endpoints;
	}

	@Override
	public void registerTransport(String protocol, ITransport transport) {
		this.transports.put(protocol, transport);
	}

	public void setTransports( HashMap<String, ITransport> transports ) {
		this.transports = transports;
	}

	protected Collection<ITransport> getTransports() {
		return this.transports.values();
	}

	@Override
	public boolean isSupports(String protocol) {
		return this.transports.containsKey(protocol);
	}

	@Override
	public void send(INotification notification) throws NotificationException {
		try {
			for ( IDestination destination : this.getEndPoints() ) {
				for ( ITransport transport : this.getTransports() ) {
					if ( transport.isSupports(destination) ) {
						transport.send(notification, destination);
					}
				}
			}
		} catch ( URISyntaxException e ) {
			throw new NotificationException("Malformed destination address", e );
		}
	}
}
