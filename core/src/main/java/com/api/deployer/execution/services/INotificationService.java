package com.api.deployer.execution.services;

import com.api.daemon.IRemoteService;
import com.api.deployer.notifications.INotification;

import java.rmi.RemoteException;

/**
 * @author nikelin
 * @date 21/04/11
 * @package com.api.deployer.execution.services
 */
public interface INotificationService extends IRemoteService {

	public void receive( INotification notification ) throws RemoteException;

}
