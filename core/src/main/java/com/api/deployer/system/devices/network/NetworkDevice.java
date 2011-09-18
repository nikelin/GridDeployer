package com.api.deployer.system.devices.network;

import com.api.deployer.system.devices.INetworkDevice;
import com.api.deployer.system.devices.network.routes.INetworkDeviceRoute;

import java.net.InetAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

/**
 * @author nikelin
 * @author semichevsky
 */
public class NetworkDevice implements INetworkDevice {
	private UUID uuid;
	private String name;
    private Boolean up;
	private InetAddress address;
	private InetAddress netmask;
	private String path;
	private InetAddress gateway;
	private InetAddress broadcast;
    private Collection<INetworkDeviceRoute> routes = new HashSet<INetworkDeviceRoute>();
	private NetworkDeviceType type;

    public NetworkDevice() {
        super();
    }

    public void setUp( boolean value ) {
        this.up = value;
    }

    public boolean getUp() {
        return this.up;
    }

    @Override
    public void addRoute(INetworkDeviceRoute route) {
        this.routes.add(route);
    }

    @Override
    public Collection<INetworkDeviceRoute> getRoutes() {
        return this.routes;
    }

    @Override
	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public UUID getUUID() {
		return this.uuid;
	}

	@Override
	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String getPath() {
		return this.path;
	}

	@Override
	public void setAddress(InetAddress address) {
		this.address = address;
	}
	
	@Override
	public InetAddress getAddress() {
		return this.address;
	}

	@Override
	public InetAddress getNetmask() {
		return this.netmask;
	}

	@Override
	public InetAddress getGateway() {
		return this.gateway;
	}

	@Override
	public NetworkDeviceType getType() {
		return this.type;
	}
	
	@Override
	public void setType(NetworkDeviceType type) {
		this.type = type;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setBroadcast(InetAddress broadcast) {
		this.broadcast = broadcast;
	}

	@Override
	public InetAddress getBroadcast() {
		return this.broadcast;
	}

	@Override
	public void setNetmask(InetAddress netmask) {
		this.netmask = netmask;
	}

	@Override
	public void setGateway(InetAddress gateway) {
		this.gateway = gateway;
	}

	
}
