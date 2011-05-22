package com.api.deployer.execution.services;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.UUID;

import com.api.commons.IFilter;
import com.api.daemon.IRemoteService;
import com.api.deployer.system.devices.IDevice;
import com.api.deployer.execution.IJobExecutor;

public interface IDeployAgentService extends IJobExecutor, IRemoteService {
	
	public Collection<IDevice> getDevices() throws RemoteException;
	
	public Collection<IDevice> getDevices( IFilter<IDevice> filter ) throws RemoteException;
	
	public IDevice getDevice( IFilter<IDevice> filter ) throws RemoteException;
	
	public String executeScript( String script ) throws RemoteException;
	
	public boolean ping() throws RemoteException;

	public Integer getJobProgress( UUID jobId ) throws RemoteException;

}
