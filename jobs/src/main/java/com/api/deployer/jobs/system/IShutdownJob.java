package com.api.deployer.jobs.system;

import com.redshape.daemon.jobs.IJob;

public interface IShutdownJob extends IJob {
	
	public void setDelay( Integer delay );
	
	public Integer getDelay();
	
}
