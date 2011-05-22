package com.api.deployer.ui.data.jobs.results;

import com.redshape.ui.data.AbstractModelType;

/**
 * @author nikelin
 * @date 27/04/11
 * @package com.api.deployer.ui.data.jobs.results
 */
public class JobResultModel extends AbstractModelType {
    public static final String ID = "id";
    public static final String JOB_ID = "jobId";
    public static final String DATE = "date";
    public static final String ATTRIBUTES = "attributes";

    public JobResultModel() {
        super();

        this.addField( ID )
            .setTitle("ID");

        this.addField( JOB_ID )
            .setTitle("Job ID");

        this.addField( DATE )
            .setTitle("Completion date");
    }

    @Override
    public JobResult createRecord() {
        return new JobResult();
    }

}
