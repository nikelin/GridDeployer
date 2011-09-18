package com.api.deployer.system.linux.configurers.network;

import com.api.deployer.system.ISystemFacade;
import com.api.deployer.system.configurers.ConfigurerException;
import com.api.deployer.system.configurers.network.INetworkConfigurer;
import com.api.deployer.system.configurers.network.RouteOperation;
import com.api.deployer.system.devices.INetworkDevice;
import com.api.deployer.system.scripts.IScriptExecutor;
import com.redshape.utils.StringUtils;

import java.io.IOException;
import java.net.InetAddress;

/**
 * @author nikelin
 * @date 25/04/11
 * @package com.api.deployer.system.linux.configurers.network
 */
public class StatelessNetworkConfigurer implements INetworkConfigurer {
    private ISystemFacade facade;

    public StatelessNetworkConfigurer(ISystemFacade facade) {
        this.facade = facade;
    }

    protected ISystemFacade getFacade() {
        return this.facade;
    }

    @Override
    public void configure(INetworkDevice device,
                          InetAddress address,
                          InetAddress gateway,
                          InetAddress netmask,
                          InetAddress broadcast) throws ConfigurerException {
        IScriptExecutor executor = this.getFacade().getConsole().createExecutor(this, "ifconfig");
        executor.addUnnamedParameter( device.getPath() );

        if ( address == null ) {
            throw new ConfigurerException("Address must be provided!");
        }

        executor.addUnnamedParameter("address")
                .addUnnamedParameter( StringUtils.IPToString(address.getAddress()) );

        if ( netmask != null ) {
            executor.addUnnamedParameter("netmask")
                    .addUnnamedParameter( StringUtils.IPToString( netmask.getAddress() ) );
        }

        if ( gateway != null ) {
            executor.addUnnamedParameter("gateway")
                    .addUnnamedParameter( StringUtils.IPToString( gateway.getAddress() ) );
        }

        if ( broadcast != null ) {
            executor.addUnnamedParameter("broadcast")
                    .addUnnamedParameter( StringUtils.IPToString( broadcast.getAddress() ) );
        }

        try {
            executor.execute();
        } catch ( IOException e ) {
            throw new ConfigurerException( e.getMessage(), e );
        }
    }

    @Override
    public void addRoute(INetworkDevice device,
                         InetAddress network,
                         InetAddress netmask,
                         InetAddress gateway ) throws ConfigurerException {
        this.addRoute( device, network, netmask, gateway, RouteOperation.ACCEPT );
    }

    @Override
    public void addRoute(INetworkDevice device,
                         InetAddress network,
                         InetAddress netmask,
                         InetAddress gateway,
                         RouteOperation operation ) throws ConfigurerException  {
        IScriptExecutor executor = this.getFacade().getConsole().createExecutor( this, "route");
        executor.addUnnamedParameter("add");

        if ( network == null ) {
            throw new ConfigurerException("Target network address must be provided!");
        }

        executor.addUnnamedParameter("-net")
                .addUnnamedParameter( StringUtils.IPToString( network.getAddress() ) );

        if ( gateway != null ) {
            executor.addUnnamedParameter("gw")
                    .addUnnamedParameter( StringUtils.IPToString( gateway.getAddress() ) );
        }

        if ( netmask != null ) {
            executor.addUnnamedParameter("netmask")
                    .addUnnamedParameter( StringUtils.IPToString( netmask.getAddress() ) );
        }

        if ( device != null ) {
            executor.addUnnamedParameter("dev")
                    .addUnnamedParameter( device.getName() );
        }

        if ( operation != null && operation.equals( RouteOperation.REJECT ) ) {
            executor.addUnnamedParameter("reject");
        }

        try {
            executor.execute();
        } catch ( IOException e ) {
            throw new ConfigurerException( e.getMessage(), e );
        }
    }

    @Override
    public void removeRoute(INetworkDevice device,
                            InetAddress network) throws ConfigurerException {
        IScriptExecutor executor = this.getFacade().getConsole().createExecutor("route");
        executor.addUnnamedParameter("del");

        if ( network == null ) {
            throw new ConfigurerException("Target network must be provided");
        }

        executor.addUnnamedParameter("-net")
                .addUnnamedParameter( StringUtils.IPToString( network.getAddress() ) );

        try {
            executor.execute();
        } catch ( IOException e ) {
            throw new ConfigurerException( e.getMessage(), e );
        }
    }
}
