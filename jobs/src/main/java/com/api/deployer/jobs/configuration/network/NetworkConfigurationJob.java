package com.api.deployer.jobs.configuration.network;

import com.api.commons.StringUtils;
import com.api.deployer.jobs.AbstractJob;
import com.api.deployer.system.devices.INetworkDevice;
import com.redshape.bindings.annotations.Bindable;
import com.redshape.bindings.annotations.BindableWriter;
import com.redshape.bindings.annotations.IgnoreViolations;
import com.redshape.bindings.types.BindableType;
import com.redshape.validators.annotations.NotNull;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * @author nikelin
 * @date 22/04/11
 * @package com.api.deployer.jobs.configuration
 */
public class NetworkConfigurationJob extends AbstractJob {
    @NotNull
    @Bindable( name = "Device", type = BindableType.LIST )
    private INetworkDevice device;

    @NotNull
    @Bindable( name = "Address", type = BindableType.STRING )
    private InetAddress address;

    @NotNull
    @Bindable( name = "Netmask", type = BindableType.STRING  )
    private InetAddress netmask;

    @NotNull
    @Bindable( name = "Gateway", type = BindableType.STRING  )
    private InetAddress gateway;

    @NotNull
    @Bindable( name = "Broadcast", type = BindableType.STRING  )
    private InetAddress broadcastAddress;

    public NetworkConfigurationJob() {
        this(null);
    }

    public NetworkConfigurationJob(UUID agentId) {
        super(agentId);
    }

    public void setDevice( INetworkDevice device ) {
        this.device = device;
    }

    public INetworkDevice getDevice() {
        return this.device;
    }

    @BindableWriter( name = "address" )
    @IgnoreViolations
    public void setAddress( String address ) throws UnknownHostException {
        if ( address == null || address.isEmpty() ) {
            return;
        }

        this.setAddress(InetAddress.getByAddress(StringUtils.stringToIP(address)));
    }

    public void setAddress( InetAddress address ) {
        this.address = address;
    }

    public InetAddress getAddress() {
        return this.address;
    }

    @BindableWriter( name = "gateway" )
    @IgnoreViolations
    public void setGateway( String address ) throws UnknownHostException {
        if ( address == null || address.isEmpty() ) {
            return;
        }

        this.setGateway( InetAddress.getByAddress( StringUtils.stringToIP(address) ) );
    }

    public void setGateway( InetAddress address ) {
        this.gateway = address;
    }

    public InetAddress getGateway() {
        return this.gateway;
    }

    @BindableWriter( name = "netmask" )
    @IgnoreViolations
    public void setNetmask( String address ) throws UnknownHostException {
        if ( address == null || address.isEmpty() ) {
            return;
        }

        this.setNetmask( InetAddress.getByAddress( StringUtils.stringToIP(address) ) );
    }

    public InetAddress getNetmask() {
        return this.netmask;
    }

    public void setNetmask( InetAddress address ) {
        this.netmask = address;
    }

    @BindableWriter( name = "broadcastAddress" )
    @IgnoreViolations
    public void setBroadcastAddress( String address ) throws UnknownHostException {
        if ( address == null || address.isEmpty() ) {
            return;
        }

        this.setBroadcastAddress( InetAddress.getByAddress( StringUtils.stringToIP(address) ) );
    }

    public void setBroadcastAddress( InetAddress address ) {
        this.broadcastAddress = address;
    }

    public InetAddress getBroadcastAddress() {
        return this.broadcastAddress;
    }

    @Override
    public String toString() {
        return StringUtils.IPToString( this.getAddress().getAddress() );
    }

}
