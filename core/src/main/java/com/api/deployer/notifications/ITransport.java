package com.api.deployer.notifications;

import com.api.deployer.io.transport.IDestination;

import java.net.URISyntaxException;

/**
 * @author nikelin
 * @date 21/04/11
 * @package com.api.deployer.notifications
 */
public interface ITransport {

	public boolean isSupports( IDestination destination ) throws URISyntaxException;

	public void send( INotification notification, IDestination endPoint ) throws NotificationException;

}
