package com.api.deployer.execution;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

import com.api.deployer.jobs.JobException;

public interface IJobsDispatcher extends Remote {
	
	/**
	 * Executors registering method what each client thread should invoke
	 * to provides dispatcher such amount of information to decide what kind and amount
	 * of jobs needs to provide him.
	 *  
	 * @param descripton Object which describes client
	 * @return Unique client identifier which will be used to create jobs and etc.
	 * @throws RemoteException
	 */
	public UUID register( IExecutorDescriptor descripton ) throws RemoteException;
	
	/**
	 * Unregister client from dispatcher connections list
	 * 
	 * @param executorId
	 * @throws RemoteException
	 */
	public void unregister( UUID executorId ) throws RemoteException;
	
	/**
	 * Method which provides client with ability to send job execution result
	 * on dispatcher side.
	 * 
	 * @param job Execution result object which contains completed job ID and other job related data
	 * @throws RemoteException
	 */
	public void complete( UUID agentId, UUID jobId ) throws RemoteException;
	
	/**
	 * Marks job under given UUID as failed and provides fail case details
	 * 
	 * @param jobId 
	 * @param e
	 * @throws RemoteException
	 */
	public void fail( UUID agentId, UUID jobId, JobException e ) throws RemoteException;
	
}
