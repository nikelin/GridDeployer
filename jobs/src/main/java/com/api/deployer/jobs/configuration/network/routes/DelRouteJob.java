package com.api.deployer.jobs.configuration.network.routes;

import com.api.deployer.jobs.AbstractJob;
import com.api.deployer.system.devices.INetworkDevice;
import com.redshape.bindings.annotations.Bindable;
import com.redshape.bindings.annotations.BindableWriter;
import com.redshape.bindings.annotations.IgnoreViolations;
import com.redshape.bindings.types.BindableType;
import com.redshape.utils.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * @author nikelin
 * @date 25/04/11
 * @package com.api.deployer.jobs.configuration.network.routes
 */
public class DelRouteJob extends AbstractJob {

    @Bindable( name = "Device", type = BindableType.LIST )
    private INetworkDevice device;

    @Bindable( name = "Target network", type = BindableType.STRING )
    private InetAddress network;

    public DelRouteJob() {
        this(null);
    }

    public DelRouteJob( UUID agentId ) {
        super(agentId);
    }

    public INetworkDevice getDevice() {
        return this.device;
    }

    public void setDevice( INetworkDevice device ) {
        this.device = device;
    }

    @BindableWriter( name = "network" )
    @IgnoreViolations
    public void setNetwork( String network ) throws UnknownHostException {
        this.setNetwork( InetAddress.getByAddress( StringUtils.stringToIP( network ) ) );
    }

    public void setNetwork( InetAddress network ) {
        this.network = network;
    }

    public InetAddress getNetwork() {
        return this.network;
    }

}
