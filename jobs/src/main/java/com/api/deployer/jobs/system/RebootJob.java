package com.api.deployer.jobs.system;

import com.redshape.bindings.annotations.Bindable;
import com.redshape.daemon.jobs.AbstractJob;

import java.util.UUID;

public class RebootJob extends AbstractJob implements IRebootJob {
	private static final long serialVersionUID = -1775227887758785515L;

    @Bindable( name = "Operation delay" )
	private Integer delay;

    public RebootJob() {
        this(null);
    }

	public RebootJob( UUID agentId ) {
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
