package com.api.deployer.execution.services;

import com.api.deployer.execution.IExecutorDescriptor;
import com.api.deployer.jobs.JobScope;
import com.redshape.daemon.jobs.IJob;
import com.redshape.daemon.jobs.activation.JobActivationProfile;
import com.redshape.daemon.jobs.result.IJobResult;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public interface IJobsDispatcherService extends Remote {
	
	/**
	 * Get currently connected execution agents
	 * @return
	 */
	public Map<UUID, IExecutorDescriptor> getConnectedExecutors() throws RemoteException;

    public Collection<IJobResult> getExecutionResults() throws RemoteException;

    public Collection<IJobResult> getExecutionResults( UUID jobId ) throws RemoteException;

	/**
	 * Get currently processing jobs
	 * @return
	 */
	public Map<UUID, IJob> getJobs( UUID agentId ) throws RemoteException;

    public IJob getJob( UUID jobId ) throws RemoteException;

    public IJobResult executeJob( JobScope scope, UUID agentId, IJob job ) throws RemoteException;

    public Collection<IJobResult> executeJobs( JobScope scope, UUID agentId, Collection<IJob> jobs ) throws RemoteException;

	public UUID scheduleJob( UUID agentId, IJob job, JobActivationProfile profile ) throws RemoteException;

	public Collection<UUID> scheduleJobs( UUID agentId, Collection<IJob> jobs, JobActivationProfile profile ) throws RemoteException;

    public void unscheduleJob( UUID jobId ) throws RemoteException;

    public Collection<IJob> getScheduledJobs() throws RemoteException;

    public void pauseJob( UUID job ) throws RemoteException;

    public void resumeJob( UUID job ) throws RemoteException;

	public void cancelJob( UUID job ) throws RemoteException;
	
	public boolean isComplete( UUID job ) throws RemoteException;
	
	public boolean isFailed( UUID job ) throws RemoteException;

	public Integer getProgress( UUID agentId, UUID job ) throws RemoteException;
	
}
