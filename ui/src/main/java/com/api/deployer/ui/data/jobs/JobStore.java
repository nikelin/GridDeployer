package com.api.deployer.ui.data.jobs;

import com.redshape.ui.data.loaders.IDataLoader;
import com.redshape.ui.data.stores.ListStore;

/**
 * @author root
 * @date 12/04/11
 * @package com.api.deployer.ui.data.jobs
 */
public class JobStore extends ListStore<Job> {

	public JobStore( IDataLoader<Job> loader ) {
		super( new JobModel(), loader );
	}

}
