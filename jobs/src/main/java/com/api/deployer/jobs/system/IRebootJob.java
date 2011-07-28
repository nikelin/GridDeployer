package com.api.deployer.jobs.system;

import com.api.deployer.jobs.IJob;

public interface IRebootJob extends IJob {

	public void setDelay( Integer delay );
	
	public Integer getDelay();
	
}
