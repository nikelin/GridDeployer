package com.api.deployer.system.devices.network.routes;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * @author nikelin
 * @date 26/04/11
 * @package com.api.deployer.system.devices.network.routes
 */
public interface INetworkDeviceRoute extends Serializable {

    public InetAddress getNetwork();

    public InetAddress getNetmask();

    public InetAddress getGateway();

}
