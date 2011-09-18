package com.api.deployer.execution.services;

import com.api.deployer.execution.IJobsDispatcher;
import com.api.deployer.io.transport.IDestination;
import com.api.deployer.notifications.INotification;
import com.api.deployer.system.devices.IDevice;
import com.api.deployer.system.processes.ISystemProcess;
import com.redshape.daemon.IRemoteService;
import com.redshape.utils.IFilter;
import com.redshape.utils.events.IRemoteEventDispatcher;

import java.net.URI;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.UUID;

public interface IDeployServerService extends IJobsDispatcherService, IRemoteService,
											  IRemoteEventDispatcher, IJobsDispatcher {

	public void sendNotification( INotification notification ) throws RemoteException;

	public void registerNotifyPoint( IDestination destination ) throws RemoteException;

	public URI getArtifactoryURI() throws RemoteException;

	public void setArtifactoryURI( URI uri ) throws RemoteException;

	public Collection<IDevice> getDevices( UUID agentId ) throws RemoteException;
	
	public IDevice getDevice( UUID agentId, IFilter<IDevice> filter ) throws RemoteException;
	
	public Collection<IDevice> getDevices( UUID agentId, IFilter<IDevice> filter ) throws RemoteException;
	
	public String getHostname( UUID agentId ) throws RemoteException;
	
	public Collection<ISystemProcess> getProcesses( UUID agentId ) throws RemoteException;

	public String executeScript( UUID agentId, String script ) throws RemoteException;
	
}
