package com.api.deployer.ui.data.jobs.results;

import com.api.deployer.jobs.result.JobResultAttribute;
import com.redshape.ui.data.AbstractModelData;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author nikelin
 * @date 27/04/11
 * @package com.api.deployer.ui.data.jobs.results
 */
public class JobResult extends AbstractModelData {

    public JobResult() {
        super();
        this.set( JobResultModel.ATTRIBUTES, new HashMap<JobResultAttribute, Object>() );
    }

    public void setCompletionDate( Date date ) {
        this.set( JobResultModel.DATE, date );
    }

    public Date getCompletionDate() {
        return this.get( JobResultModel.DATE );
    }

    public void setId( UUID id ) {
        this.set( JobResultModel.ID, id );
    }

    public UUID getId() {
        return this.get( JobResultModel.ID );
    }

    public UUID getJobId() {
        return this.get( JobResultModel.JOB_ID );
    }

    public void setJobId( UUID id ) {
        this.set( JobResultModel.JOB_ID, id );
    }

    public <V> V getAttribute( JobResultAttribute attribute ) {
        return (V) this.getAttributes().get( attribute );
    }

    public void setAttribute( JobResultAttribute attribute, Object value ) {
        this.getAttributes().put( attribute, value );
    }

    public void setAttributes( Map<JobResultAttribute, Object> attributes ) {
        this.set( JobResultModel.ATTRIBUTES, attributes );
    }

    public <V> Map<JobResultAttribute, V> getAttributes() {
        return (Map<JobResultAttribute, V>) this.get( JobResultModel.ATTRIBUTES );
    }

}
