package com.api.deployer.ui.data.jobs.loaders;

import com.api.deployer.ui.connector.DeployAgentConnector;
import com.api.deployer.ui.data.jobs.Job;
import com.api.deployer.ui.data.jobs.categories.JobCategoryStore;
import com.redshape.daemon.jobs.IJob;
import com.redshape.ui.data.loaders.AbstractDataLoader;
import com.redshape.ui.data.loaders.LoaderException;
import com.redshape.ui.utils.UIRegistry;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author nikelin
 * @date 25/04/11
 * @package com.api.deployer.ui.data.jobs.loaders
 */
public class ScheduleJobsLoader extends AbstractDataLoader<Job> {

    protected DeployAgentConnector getConnector() {
        return UIRegistry.getContext().getBean( DeployAgentConnector.class );
    }

    @Override
    protected Collection<Job> doLoad() throws LoaderException {
        try {
            JobCategoryStore globalStore = UIRegistry.getContext().getBean( JobCategoryStore.class );

            Collection<Job> result = new HashSet<Job>();
            for ( IJob job : this.getConnector().getScheduledJobs() ) {
                Job jobItem = null;
                if ( globalStore != null ) {
                    jobItem = globalStore.findJobByJobClass( job.getClass() );
                }

                if ( jobItem == null ) {
                    jobItem = new Job();
                    jobItem.setName( job.getClass().getCanonicalName() );
                } else {
                    jobItem = new Job( jobItem );
                }

                jobItem.setState( job.getState() );
                jobItem.setId( job.getJobId() );
                jobItem.setJob( job );
                jobItem.setRelatedObject( job );

                result.add( jobItem );
            }

            return result;
        } catch ( RemoteException e ) {
            throw new LoaderException( e.getMessage(), e );
        }
    }

}
