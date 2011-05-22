package com.api.deployer.jobs;

import java.util.Date;
import java.util.UUID;

public abstract class AbstractJob implements IJob {
	private static final long serialVersionUID = -5210141269965829207L;
	
	private Date created;
	private UUID id;
    private JobScope scope;
    private JobState state;
	private UUID agentId;
	
	public AbstractJob( UUID agentId ) {
		this( agentId, JobScope.AGENT );
	}

    public AbstractJob( UUID agentId, JobScope scope ) {
        this.created = new Date();
		this.agentId = agentId;
        this.scope = scope;
    }

    @Override
    public JobScope getScope() {
        return scope;
    }

    @Override
    public void setScope(JobScope scope) {
        this.scope = scope;
    }

    @Override
    public void setState( JobState state ) {
        this.state = state;
    }

    @Override
    public JobState getState() {
        return this.state;
    }

	@Override
	public Date getCreated() {
		return this.created;
	}
	
	protected UUID generateUUID() {
		return UUID.randomUUID();
	}
	
	@Override
	public void setId(UUID id) {
		this.id = id;
	}
	
	@Override
	public UUID getId() {
		return this.id;
	}

	@Override
	public UUID getAgentId() {
		return this.agentId;
	}

	@Override
	public void setAgentId(UUID id) {
		this.agentId = id;
	}

    @Override
    public int hashCode() {
        if ( this.id != null ) {
            return this.id.hashCode();
        }

        return  super.hashCode();
    }
	
}
