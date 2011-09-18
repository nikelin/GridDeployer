package com.api.deployer.agent.handlers.configuration.routes;

import com.api.deployer.jobs.configuration.network.routes.AddRouteJob;
import com.api.deployer.system.ISystemFacade;
import com.redshape.daemon.jobs.handlers.AbstractJobHandler;
import com.redshape.daemon.jobs.handlers.HandlingException;
import com.redshape.daemon.jobs.result.IJobResult;
import com.redshape.daemon.jobs.result.JobResult;

import java.util.UUID;

/**
 * @author nikelin
 * @date 25/04/11
 * @package com.api.deployer.agent.handlers.configuration.routes
 */
public class AddRouteJobHandler extends AbstractJobHandler<AddRouteJob, IJobResult> {
	private ISystemFacade facade;

    public AddRouteJobHandler( ISystemFacade facade ) {
        this.facade = facade;
    }

	public ISystemFacade getFacade() {
		return facade;
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

    public Integer getProgress() throws HandlingException {
        return 0;
    }
}
