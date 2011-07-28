package com.api.deployer.notifications;

import com.api.deployer.io.transport.IDestination;

import java.util.Collection;

/**
 * @author nikelin
 * @date 21/04/11
 * @package com.api.deployer.notifications
 */
public interface ITransportFacade {

	public void addEndPoint( IDestination endPoint );

	public Collection<IDestination> getEndPoints();

	public void registerTransport( String protocol, ITransport transport );

	public boolean isSupports( String protocol );

	public void send( INotification notification ) throws NotificationException;

}
