package com.api.deployer.ui.data.jobs;

import java.util.UUID;

import com.api.deployer.jobs.IJob;
import com.api.deployer.jobs.JobScope;
import com.api.deployer.jobs.JobState;
import com.api.deployer.ui.data.jobs.categories.JobCategory;
import com.api.deployer.ui.data.workstations.IDeploySubject;
import com.redshape.ui.data.AbstractModelData;

public class Job extends AbstractModelData {
	private static final long serialVersionUID = 8253025593292510523L;

	public Job() {
		this( (UUID) null);
	}

    public Job( Job job ) {
        this( job.getId() );

        this.setDescription( job.getDescription() );
        this.setScope( job.getScope() );
        this.setState( job.getState() );
        this.setCategory(job.getCategory());
        this.setName(job.getName());
        this.setJob(job.getJob());
        this.setJobClass(job.getJobClass());
        this.setTarget( job.getTarget() );
    }

	public Job( UUID id ) {
		if ( id != null ) {
			this.set( JobModel.ID, id );
		}
	}

    public void setDescription( String description ) {
        this.set( JobModel.DESCRIPTION, description );
    }

    public String getDescription() {
        return this.get( JobModel.DESCRIPTION );
    }

    public void setScope( JobScope scope ) {
        this.set(JobModel.SCOPE, scope);
    }

    public JobScope getScope() {
        return this.get( JobModel.SCOPE );
    }

    public void setState( JobState state ) {
        this.set(JobModel.STATE, state );
    }

    public JobState getState() {
        return this.get( JobModel.STATE );
    }

	public void setTarget( IDeploySubject target ) {
		this.set( JobModel.JOB_TARGET, target );
	}

	public IDeploySubject getTarget() {
		return this.get( JobModel.JOB_TARGET );
	}

	public void setCategory( JobCategory category ) {
		this.set( JobModel.JOB_CATEGORY, category );
	}

	public JobCategory getCategory() {
		return this.get( JobModel.JOB_CATEGORY );
	}

	public String getName() {
		return this.get( JobModel.NAME );
	}

	public void setName( String name ) {
		this.set( JobModel.NAME, name );
	}

	public void setJob( IJob job ) {
		this.set( JobModel.JOB, job );
	}

	public IJob getJob() {
		return this.get( JobModel.JOB );
	}

	public Class<? extends IJob> getJobClass() {
		return this.get( JobModel.JOB_CLASS );
	}

	public void setJobClass( Class<? extends IJob> clazz ) {
		this.set( JobModel.JOB_CLASS, clazz );
	}
	
	public UUID getId() {
		return this.get(JobModel.ID);
	}
	
	public void setId( UUID id ) {
		this.set( JobModel.ID, id );
	}

	@Override
	public String toString() {
		return this.getName();
	}
	
}
