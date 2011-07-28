package com.api.deployer.system.devices.network.routes;

import java.net.InetAddress;

/**
 * @author nikelin
 * @date 26/04/11
 * @package com.api.deployer.system.devices.network.routes
 */
public class NetworkDeviceRoute implements INetworkDeviceRoute {
    private InetAddress network;
    private InetAddress gateway;
    private InetAddress netmask;

    public NetworkDeviceRoute() {
        super();
    }

    public void setNetwork( InetAddress address ) {
        this.network = address;
    }

    @Override
    public InetAddress getNetwork() {
        return this.network;
    }

    public void setNetmask( InetAddress address ) {
        this.netmask = address;
    }

    @Override
    public InetAddress getNetmask() {
        return this.netmask;
    }

    public void setGateway( InetAddress address ) {
        this.gateway = address;
    }

    @Override
    public InetAddress getGateway() {
        return this.gateway;
    }
}
