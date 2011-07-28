package com.api.deployer.agent.handlers.configuration.routes;

import com.api.deployer.jobs.configuration.network.routes.AddRouteJob;
import com.api.deployer.jobs.handlers.AbstractJobHandler;
import com.api.deployer.jobs.handlers.HandlingException;
import com.api.deployer.jobs.result.IJobResult;
import com.api.deployer.jobs.result.JobResult;
import com.api.deployer.system.ISystemFacade;

import java.util.UUID;

/**
 * @author nikelin
 * @date 25/04/11
 * @package com.api.deployer.agent.handlers.configuration.routes
 */
public class AddRouteJobHandler extends AbstractJobHandler<AddRouteJob, IJobResult> {

    public AddRouteJobHandler( ISystemFacade facade ) {
        super(facade);
    }

    @Override
    protected IJobResult createJobResult(UUID jobId) {
        return new JobResult( jobId );
    }

    @Override
    public IJobResult handle(AddRouteJob job) throws HandlingException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void cancel()  {
        throw new UnsupportedOperationException("Operation not supports");
    }

    @Override
    public Integer getProgress() throws HandlingException {
        return 0;
    }
}
