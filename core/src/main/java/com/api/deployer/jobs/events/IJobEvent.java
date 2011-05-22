package com.api.deployer.jobs.events;

import com.api.daemon.IDaemonEvent;
import com.api.deployer.jobs.IJob;

public interface IJobEvent extends IDaemonEvent {
	
	public <V extends IJob> V getJob();
	
}
