package com.api.deployer.ui.data.jobs.results;

import com.redshape.ui.data.loaders.IDataLoader;
import com.redshape.ui.data.stores.ListStore;

/**
 * @author nikelin
 * @date 27/04/11
 * @package com.api.deployer.ui.data.jobs.results
 */
public class JobResultsStore extends ListStore<JobResult> {

    public JobResultsStore() {
        this( null );
    }

    public JobResultsStore( IDataLoader<JobResult> loader ) {
        super( new JobResultModel(), loader );
    }

}
