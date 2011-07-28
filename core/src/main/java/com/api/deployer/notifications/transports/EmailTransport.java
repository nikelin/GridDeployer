package com.api.deployer.notifications.transports;

import com.api.commons.IFilter;
import com.api.commons.config.IConfig;
import com.api.deployer.io.transport.IDestination;
import com.api.deployer.notifications.INotification;
import com.api.deployer.notifications.ITransport;
import com.api.deployer.notifications.NotificationException;

import java.net.URISyntaxException;
import java.util.Collection;

/**
 * @author nikelin
 * @date 21/04/11
 * @package com.api.deployer.notifications.transports
 */
public class EmailTransport implements ITransport {
	private IConfig config;

	public EmailTransport( IConfig config ) {
		this.config = config;
	}

	@Override
	public boolean isSupports(IDestination destination) throws URISyntaxException {
		return destination.getURI().getScheme().equals("email");
	}

	@Override
	public void send(INotification notification, IDestination endPoint) throws NotificationException {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
