package com.api.deployer.system.configurers.network;

import com.api.deployer.system.configurers.ConfigurerException;
import com.api.deployer.system.devices.INetworkDevice;

import java.net.InetAddress;

/**
 * @author nikelin
 * @date 25/04/11
 * @package com.api.deployer.system.configurers.net
 */
public interface INetworkConfigurer {

    public void configure( INetworkDevice device,
               InetAddress address,
               InetAddress gateway,
               InetAddress netmask,
               InetAddress broadcast ) throws ConfigurerException;

    public void addRoute( INetworkDevice device,
               InetAddress network,
               InetAddress netmask,
               InetAddress gateway ) throws ConfigurerException;

    public void addRoute( INetworkDevice device,
               InetAddress network,
               InetAddress netmask,
               InetAddress gateway,
               RouteOperation operation ) throws ConfigurerException;

    public void removeRoute( INetworkDevice device,
               InetAddress network ) throws ConfigurerException;

}
