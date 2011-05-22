package com.api.deployer.jobs.handlers;

import org.springframework.context.ApplicationContext;

import com.api.deployer.jobs.IJob;
import com.api.deployer.jobs.result.IJobResult;
import com.api.deployer.system.ISystemFacade;

public abstract class AbstractAwareJobHandler<T extends IJob, V extends IJobResult> 
							extends AbstractJobHandler<T, V> {
	private ApplicationContext context;
	
	public AbstractAwareJobHandler( ISystemFacade facade ) {
		super(facade);
	}
	
	public void setContext( ApplicationContext context ) {
		this.context = context;
	}
	
	public ApplicationContext getContext() {
		return this.context;
	}
	
}
