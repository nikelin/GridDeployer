package com.api.deployer.ui.data.jobs.results.loaders;

import com.api.deployer.jobs.result.IJobResult;
import com.api.deployer.ui.connector.DeployAgentConnector;
import com.api.deployer.ui.data.jobs.results.JobResult;
import com.redshape.ui.data.loaders.AbstractDataLoader;
import com.redshape.ui.data.loaders.LoaderException;
import com.redshape.ui.utils.UIRegistry;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * @author nikelin
 * @date 27/04/11
 * @package com.api.deployer.ui.data.jobs.results.loaders
 */
public class JobResultsLoader extends AbstractDataLoader<JobResult> {

    protected DeployAgentConnector getConnector() {
        return UIRegistry.getContext().getBean( DeployAgentConnector.class );
    }

    @Override
    protected Collection<JobResult> doLoad() throws LoaderException {
        try {
            Collection<JobResult> result = new ArrayList<JobResult>();

            Collection<IJobResult> results = this.getConnector().getExecutionResults();
            for ( IJobResult jobResult : results ) {
                JobResult resultData = new JobResult();
                resultData.setId( jobResult.getId() );
                resultData.setJobId( jobResult.getJobId() );
                resultData.setAttributes( jobResult.<Object>getAttributes() );
                resultData.setCompletionDate( jobResult.getCompletionDate() );
                resultData.setRelatedObject( jobResult );
                result.add( resultData );
            }

            return result;
        } catch ( RemoteException e ) {
            throw new LoaderException("Network interaction exception", e );
        }
    }
}
