package com.api.deployer.ui.data.jobs.configurations;

import com.redshape.ui.data.AbstractModelType;

/**
 * @author nikelin
 * @date 27/04/11
 * @package com.api.deployer.ui.data.jobs.configurations
 */
public class JobConfigurationModel extends AbstractModelType {
    public static final String NAME = "name";
    public static final String ITEMS = "items";
    public static final String JOBS_COUNT = "jobsCount";
    public static final String LAST_ACTIVATED = "lastActivated";
    public static final String ACTIVATION_PROFILE = "activationProfile";

    public JobConfigurationModel() {
        super( JobConfiguration.class );

        this.addField( NAME )
            .setTitle("Name");

        this.addField( ACTIVATION_PROFILE )
            .setTitle( "Activation type" );

        this.addField( JOBS_COUNT )
            .setTitle("Jobs count");

        this.addField( LAST_ACTIVATED )
            .setTitle("Last activated");
    }

    @Override
    public JobConfiguration createRecord() {
        return new JobConfiguration();
    }

}
