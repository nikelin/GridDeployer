package com.api.deployer.ui.data.devices.network;

import com.api.deployer.ui.data.devices.AbstractDevice;

import java.net.InetAddress;

/**
 * @author nikelin
 * @date 26/04/11
 * @package com.api.deployer.ui.data.devices.network
 */
public class NetworkDevice extends AbstractDevice {

    public NetworkDevice() {
        super();
    }

    public void setAddress( InetAddress address ) {
        this.set( NetworkDeviceModel.ADDRESS, address );
    }

    public InetAddress getAddress() {
        return this.get( NetworkDeviceModel.ADDRESS );
    }

    public void setGateway( InetAddress gateway ) {
        this.set( NetworkDeviceModel.GATEWAY, gateway );
    }

    public void setNetmask( InetAddress netmask ) {
        this.set( NetworkDeviceModel.NETMASK, netmask );
    }

    public InetAddress getNetmask() {
        return this.get( NetworkDeviceModel.NETMASK );
    }

    public void setBroadcast( InetAddress address ) {
        this.set( NetworkDeviceModel.BROADCAST, address );
    }

    public InetAddress getBroadcastAddress() {
        return this.get( NetworkDeviceModel.BROADCAST );
    }

}
