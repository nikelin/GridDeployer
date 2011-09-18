package com.api.deployer.execution.services;

import com.api.deployer.execution.IJobExecutor;
import com.api.deployer.system.devices.IDevice;
import com.redshape.daemon.IRemoteService;
import com.redshape.utils.IFilter;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.UUID;

public interface IDeployAgentService extends IJobExecutor, IRemoteService {
	
	public Collection<IDevice> getDevices() throws RemoteException;
	
	public Collection<IDevice> getDevices( IFilter<IDevice> filter ) throws RemoteException;
	
	public IDevice getDevice( IFilter<IDevice> filter ) throws RemoteException;
	
	public String executeScript( String script ) throws RemoteException;
	
	public boolean ping() throws RemoteException;

	public Integer getJobProgress( UUID jobId ) throws RemoteException;

}
