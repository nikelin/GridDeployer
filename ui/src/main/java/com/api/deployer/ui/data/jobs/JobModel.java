package com.api.deployer.ui.data.jobs;

import com.redshape.ui.data.AbstractModelType;
import com.redshape.ui.data.IModelData;

public class JobModel extends AbstractModelType {
	public static final String ID = "id";
    public static final String SCOPE = "scope";
    public static final String STATE = "state";
	public static final String NAME = "name";
	public static final String JOB = "job";
	public static final String JOB_CLASS = "jobClass";
	public static final String JOB_CATEGORY = "jobCategory";
	public static final String JOB_TARGET = "jobTarget";
    public static final String DESCRIPTION = "description";
	
	public JobModel() {
        super( Job.class );

		this.addField( ID )
			.setTitle("Job ID");

        this.addField( SCOPE )
            .setTitle("Job scope");

        this.addField( STATE )
            .setTitle("Job state");

		this.addField( NAME )
			.setTitle("Job name");

		this.addField( JOB_CATEGORY )
			.setTitle("Job category");

		this.addField( JOB_CLASS )
			.setTitle("Job class");

        this.addField( DESCRIPTION )
            .setTitle("Description");

		this.addField( JOB )
			.makeTransient(true);
	}

	@Override
	public IModelData createRecord() {
		return new Job();
	}
	
}
