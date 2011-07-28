package com.api.deployer.agent.handlers.configuration;

import com.api.deployer.jobs.configuration.network.NetworkConfigurationJob;
import com.api.deployer.jobs.handlers.AbstractJobHandler;
import com.api.deployer.jobs.handlers.HandlingException;
import com.api.deployer.jobs.result.IJobResult;
import com.api.deployer.jobs.result.JobResult;
import com.api.deployer.system.ISystemFacade;
import com.api.deployer.system.configurers.ConfigurerException;
import com.api.deployer.system.configurers.network.INetworkConfigurer;
import com.api.deployer.system.devices.INetworkDevice;

import java.util.UUID;

/**
 * @author nikelin
 * @date 25/04/11
 * @package com.api.deployer.agent.handlers.configuration
 */
public class NetworkConfigurationJobHandler extends AbstractJobHandler<NetworkConfigurationJob, IJobResult> {

    public NetworkConfigurationJobHandler(ISystemFacade facade) {
        super(facade);
    }

    @Override
    protected IJobResult createJobResult(UUID jobId) {
        return new JobResult(jobId);
    }

    @Override
    public IJobResult handle( NetworkConfigurationJob job) throws HandlingException {
        INetworkDevice device = job.getDevice();
        if ( device == null ) {
            throw new HandlingException("Target device must not be null!");
        }

        INetworkConfigurer configurer = this.getSystem().getNetworkConfigurer();

        try {
            configurer.configure( job.getDevice(),
                job.getAddress(), job.getGateway(),
                job.getNetmask(), job.getBroadcastAddress() );
        } catch ( ConfigurerException e ) {
            throw new HandlingException("Unable to configure specified network device", e );
        }

        return this.createJobResult( job.getId() );
    }

    @Override
    public void cancel() throws HandlingException {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    // TODO: reduce
    public Integer getProgress() throws HandlingException {
        return 0;
    }
}
