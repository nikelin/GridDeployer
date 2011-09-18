package com.api.deployer.jobs.configuration.network.routes;

import com.api.deployer.system.devices.INetworkDevice;
import com.redshape.bindings.annotations.Bindable;
import com.redshape.daemon.jobs.AbstractJob;
import com.redshape.daemon.jobs.result.JobResultAttribute;

import java.util.UUID;

/**
 * @author nikelin
 * @date 25/04/11
 * @package com.api.deployer.jobs.configuration.network.routes
 */
public class GetRoutesJob extends AbstractJob {

    public static class Attributes extends JobResultAttribute {

        protected Attributes( String code ) {
            super(code);
        }

        public static final Attributes Result = new Attributes("GetRoutesJob.Attributes.Result");

    }

    @Bindable( name = "Target device" )
    private INetworkDevice device;

    public GetRoutesJob() {
        this(null);
    }

    public GetRoutesJob( UUID agentId ) {
        super(agentId);
    }

    public INetworkDevice getDevice() {
        return this.device;
    }

    public void setDevice( INetworkDevice device ) {
        this.device = device;
    }

}
