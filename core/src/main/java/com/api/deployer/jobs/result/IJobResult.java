package com.api.deployer.jobs.result;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.UUID;


public interface IJobResult extends Serializable {

    public UUID getId();

	public UUID getJobId();

	public Date getCompletionDate();

	public JobStatus getStatus();

    public <V> Map<JobResultAttribute, V> getAttributes();

    public <V> V getAttribute( JobResultAttribute attribute );

    public void setAttribute( JobResultAttribute attribute, Object value );
}
