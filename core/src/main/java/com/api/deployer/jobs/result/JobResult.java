package com.api.deployer.jobs.result;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JobResult implements IJobResult {
	private static final long serialVersionUID = 640853501807730074L;

    private UUID id;
	private UUID jobId;
	private Date date;
    private Map<JobResultAttribute, Object> attributes = new HashMap<JobResultAttribute, Object>();
	private JobStatus status;
	
	public JobResult( UUID jobId ) {
        this.id = UUID.randomUUID();
		this.jobId = jobId;
		this.date = new Date();
	}

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
	public UUID getJobId() {
		return this.jobId;
	}

	@Override
	public Date getCompletionDate() {
		return this.date;
	}

	@Override
	public JobStatus getStatus() {
		return this.status;
	}

    @Override
    public <V> Map<JobResultAttribute, V> getAttributes() {
        return (Map<JobResultAttribute, V>) this.attributes;
    }

    @Override
    public <V> V getAttribute(JobResultAttribute attribute) {
        return (V) this.attributes.get(attribute);
    }

    @Override
    public void setAttribute(JobResultAttribute attribute, Object value) {
        this.attributes.put( attribute, value );
    }
	
}
