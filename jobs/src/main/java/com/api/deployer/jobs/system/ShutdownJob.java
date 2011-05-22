package com.api.deployer.jobs.system;

import com.api.deployer.jobs.AbstractJob;
import com.redshape.bindings.annotations.Bindable;

import java.util.UUID;

public class ShutdownJob extends AbstractJob implements IShutdownJob {
	private static final long serialVersionUID = -1464901873371010920L;

    @Bindable( name = "Operation delay" )
	private Integer delay;

    public ShutdownJob() {
        this(null);
    }

	public ShutdownJob( UUID agentId ) {
        super(agentId);
	}
	
	@Override
	public void setDelay( Integer delay ) {
		this.delay = delay;
	}
	
	@Override
	public Integer getDelay() {
		return this.delay;
	}
	
}
