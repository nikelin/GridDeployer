package com.api.deployer.ui.data.jobs.configurations;

import com.api.deployer.jobs.activation.JobActivationProfile;
import com.api.deployer.ui.data.jobs.Job;
import com.redshape.ui.data.AbstractModelData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author nikelin
 * @date 27/04/11
 * @package com.api.deployer.ui.data.jobs.configurations
 */
public class JobConfiguration extends AbstractModelData {

    public JobConfiguration() {
        this( null );
    }

    public JobConfiguration( String name ) {
        super();

        this.set( JobConfigurationModel.JOBS_COUNT, 0 );
        this.set( JobConfigurationModel.NAME, name );
        this.set( JobConfigurationModel.ITEMS, new ArrayList<Job>() );
    }

    public Date getLastActivated() {
        return this.get( JobConfigurationModel.LAST_ACTIVATED );
    }

    public void setLastActivated( Date date ) {
        this.set( JobConfigurationModel.LAST_ACTIVATED, date );
    }

    protected void updateCount() {
        this.set( JobConfigurationModel.JOBS_COUNT, this.getItems().size() );
    }

    public Integer getJobsCount() {
        return this.get( JobConfigurationModel.JOBS_COUNT );
    }

    public void setActivationProfile( JobActivationProfile profile ) {
        this.set( JobConfigurationModel.ACTIVATION_PROFILE, profile );
    }

    public JobActivationProfile getActivationProfile() {
        return this.get( JobConfigurationModel.ACTIVATION_PROFILE );
    }

    public void setName( String name ) {
        this.set( JobConfigurationModel.NAME, name );
    }

    public String getName() {
        return this.get( JobConfigurationModel.NAME );
    }

    public List<Job> getItems() {
        return this.get( JobConfigurationModel.ITEMS );
    }

    public void addItem( Job job ) {
        this.getItems().add( job );
        this.updateCount();
    }

    public void setItems( List<Job> items ) {
        this.set( JobConfigurationModel.ITEMS, items );
        this.updateCount();
    }

}
