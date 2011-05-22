package com.api.deployer.ui.data.jobs.categories;

import com.api.deployer.jobs.IJob;
import com.api.deployer.ui.data.jobs.Job;
import com.redshape.ui.data.stores.ListStore;

/**
 * @author nikelin
 * @date 12/04/11
 * @package com.api.deployer.ui.data.jobs.categories
 */
public class JobCategoryStore extends ListStore<JobCategory> {

	public JobCategoryStore() {
		super( new JobCategoryModel() );
	}

    public Job findJobByJobClass( Class<? extends IJob> clazz ) {
        Job result = null;
        for ( JobCategory category : this.getList() ) {
            result = this.findJobByJobClass( category, clazz );
            if ( result != null ) {
                break;
            }
        }

        return result;
    }

    protected Job findJobByJobClass( JobCategory category, Class<? extends IJob> clazz ) {
        Job result = null;
        for ( Job job : category.getJobs() ) {
            if ( job.getJobClass().equals( clazz ) ) {
                result = job;
                break;
            }
        }

        if ( result == null ) {
            for ( JobCategory child : category.getChilds() ) {
                result = this.findJobByJobClass( child, clazz );
                if ( result != null ) {
                    break;
                }
            }
        }

        return result;
    }

}
