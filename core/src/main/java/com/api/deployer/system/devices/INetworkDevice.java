package com.api.deployer.system.devices;

import com.api.deployer.system.devices.network.NetworkDeviceType;
import com.api.deployer.system.devices.network.routes.INetworkDeviceRoute;

import java.net.InetAddress;
import java.util.Collection;

/**
 * @author semichevsky
 * @author nikelin
 */
public interface INetworkDevice extends IDevice {

    public void addRoute( INetworkDeviceRoute route );

    public Collection<INetworkDeviceRoute> getRoutes();

	public void setBroadcast( InetAddress broadcast );
	
	public InetAddress getBroadcast();
	
	public void setAddress( InetAddress address );
	
    public InetAddress getAddress();
    
    public void setNetmask( InetAddress netmask );

    public InetAddress getNetmask();
    
    public void setGateway( InetAddress gateway );

    public InetAddress getGateway();

    public void setType( NetworkDeviceType type );
    
    public NetworkDeviceType getType();

}
