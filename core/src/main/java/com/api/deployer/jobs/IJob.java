package com.api.deployer.jobs;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public interface IJob extends Serializable {

    public JobScope getScope();

    public void setScope( JobScope scope );

    public JobState getState();

    public void setState( JobState state );

	public UUID getId();
	
	public void setId( UUID id );
	
	public UUID getAgentId();
	
	public void setAgentId( UUID id );
	
	public Date getCreated();
	
}
