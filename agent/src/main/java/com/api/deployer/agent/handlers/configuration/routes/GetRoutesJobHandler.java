package com.api.deployer.agent.handlers.configuration.routes;

import com.api.deployer.backup.artifactory.filters.UUIDFilter;
import com.api.deployer.jobs.configuration.network.routes.GetRoutesJob;
import com.api.deployer.jobs.handlers.AbstractJobHandler;
import com.api.deployer.jobs.handlers.HandlingException;
import com.api.deployer.jobs.result.IJobResult;
import com.api.deployer.jobs.result.JobResult;
import com.api.deployer.system.ISystemFacade;
import com.api.deployer.system.devices.INetworkDevice;
import com.api.deployer.system.devices.network.routes.INetworkDeviceRoute;
import com.api.deployer.system.scanners.IDeviceScanner;
import com.api.deployer.system.scanners.ScannerException;
import com.api.deployer.system.scanners.filters.NetworkDeviceFilter;
import com.api.deployer.system.scanners.filters.UUIDDeviceFilter;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

/**
 * @author nikelin
 * @date 26/04/11
 * @package com.api.deployer.agent.handlers.configuration.routes
 */
public class GetRoutesJobHandler extends AbstractJobHandler<GetRoutesJob, IJobResult> {

    public GetRoutesJobHandler(ISystemFacade facade) {
        super(facade);
    }

    @Override
    protected IJobResult createJobResult(UUID jobId) {
        return new JobResult( jobId );
    }

    @Override
    public IJobResult handle(GetRoutesJob job) throws HandlingException {
        try {
            INetworkDevice device = (INetworkDevice) this.getSystem().getDevice( new NetworkDeviceFilter( new UUIDDeviceFilter( job.getId() ) ) );

            Collection<INetworkDeviceRoute> routes = new HashSet<INetworkDeviceRoute>();
            if ( device.getUUID().equals( job.getDevice().getUUID() ) ) {
                routes.addAll( device.getRoutes() );
            }

            IJobResult result = this.createJobResult( job.getId() );
            result.setAttribute( GetRoutesJob.Attributes.Result, routes );
            return result;
        } catch ( ScannerException e ) {
            throw new HandlingException( e.getMessage(), e );
        }
    }

    @Override
    public void cancel() throws HandlingException {
        throw new UnsupportedOperationException("Operation cancellation not supported on current job type");
    }

    @Override
    public Integer getProgress() throws HandlingException {
        return 0;
    }
}
