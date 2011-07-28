package com.api.deployer.execution;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

import com.api.deployer.jobs.IJob;
import com.api.deployer.jobs.result.IJobResult;

public interface IJobExecutor extends Remote {
	
	public void setId( UUID id ) throws RemoteException;
	
	public IJobResult accept( IJob job ) throws RemoteException;
	
	public void cancel( UUID jobId ) throws RemoteException;
	
	public void pause( UUID jobId ) throws RemoteException;
	
}
