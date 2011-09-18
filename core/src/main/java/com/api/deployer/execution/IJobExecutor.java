package com.api.deployer.execution;

import com.redshape.daemon.jobs.IJob;
import com.redshape.daemon.jobs.result.IJobResult;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface IJobExecutor extends Remote {
	
	public void setId( UUID id ) throws RemoteException;
	
	public IJobResult accept( IJob job ) throws RemoteException;
	
	public void cancel( UUID jobId ) throws RemoteException;
	
	public void pause( UUID jobId ) throws RemoteException;
	
}