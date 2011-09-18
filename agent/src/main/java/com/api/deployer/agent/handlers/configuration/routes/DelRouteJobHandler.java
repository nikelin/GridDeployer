package com.api.deployer.agent.handlers.configuration.routes;

import com.api.deployer.jobs.configuration.network.routes.DelRouteJob;
import com.api.deployer.system.ISystemFacade;
import com.api.deployer.system.configurers.ConfigurerException;
import com.api.deployer.system.configurers.network.INetworkConfigurer;
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
public class DelRouteJobHandler extends AbstractJobHandler<DelRouteJob, IJobResult> {
	private ISystemFacade facade;

    public DelRouteJobHandler( ISystemFacade facade ) {
		super();

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
    public IJobResult handle(DelRouteJob job) throws HandlingException {
        INetworkConfigurer configurer = this.getFacade().getNetworkConfigurer();

        try {
            configurer.removeRoute( job.getDevice(), job.getNetwork() );
        } catch ( ConfigurerException e ) {
            throw new HandlingException( e.getMessage(), e );
        }

        return this.createJobResult( job.getJobId() );
    }

    @Override
    public void cancel() throws HandlingException {
        throw new UnsupportedOperationException("Operation not supports");
    }

    public Integer getProgress() throws HandlingException {
        return 0;
    }
}
