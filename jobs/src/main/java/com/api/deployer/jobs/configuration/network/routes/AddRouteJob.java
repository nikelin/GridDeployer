package com.api.deployer.jobs.configuration.network.routes;

import com.api.deployer.system.devices.INetworkDevice;
import com.redshape.bindings.annotations.Bindable;
import com.redshape.bindings.types.BindableType;
import com.redshape.daemon.jobs.AbstractJob;

import java.net.InetAddress;
import java.util.UUID;

/**
 * @author nikelin
 * @date 25/04/11
 * @package com.api.deployer.jobs.configuration.network.routes
 */
public class AddRouteJob extends AbstractJob {

    @Bindable( name = "Network address", type = BindableType.STRING )
    private InetAddress network;

    @Bindable( name = "Netmask", type = BindableType.STRING )
    private InetAddress netmask;

    @Bindable( name = "Gateway", type = BindableType.STRING )
    private InetAddress gateway;

    @Bindable( name = "Target device", type = BindableType.LIST )
    private INetworkDevice device;

    public AddRouteJob() {
        this(null);
    }

    public AddRouteJob( UUID agentId ) {
        super(agentId);
    }

    public InetAddress getNetwork() {
        return network;
    }

    public void setNetwork(InetAddress network) {
        this.network = network;
    }

    public InetAddress getNetmask() {
        return netmask;
    }

    public void setNetmask(InetAddress netmask) {
        this.netmask = netmask;
    }

    public InetAddress getGateway() {
        return gateway;
    }

    public void setGateway(InetAddress gateway) {
        this.gateway = gateway;
    }

    public INetworkDevice getDevice() {
        return device;
    }

    public void setDevice(INetworkDevice device) {
        this.device = device;
    }
}
