package com.api.deployer.ui.data.jobs.configurations;

import com.redshape.ui.data.loaders.IDataLoader;
import com.redshape.ui.data.stores.ListStore;

/**
 * @author nikelin
 * @date 27/04/11
 * @package com.api.deployer.ui.data.jobs.configurations
 */
public class JobConfigurationsStore extends ListStore<JobConfiguration> {

    public JobConfigurationsStore() {
        this(null);
    }

    public JobConfigurationsStore( IDataLoader<JobConfiguration> loader ) {
        super( new JobConfigurationModel(), loader );
    }

    public JobConfiguration findByName( String name ) {
        for ( JobConfiguration configuration : this.getList() ) {
            if ( configuration.getName().equals(name) ) {
                return configuration;
            }
        }

        return null;
    }

}
