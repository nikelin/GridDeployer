package com.api.deployer.system.linux.scanners;

import com.api.deployer.system.ISystemFacade;
import com.api.deployer.system.devices.INetworkDevice;
import com.api.deployer.system.devices.network.NetworkDevice;
import com.api.deployer.system.devices.network.NetworkDeviceType;
import com.api.deployer.system.devices.network.routes.NetworkDeviceRoute;
import com.api.deployer.system.scanners.IDeviceScanner;
import com.api.deployer.system.scanners.ScannerException;
import com.api.deployer.system.scripts.IScriptExecutor;
import com.redshape.utils.StringUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: semichevskiy
 * Date: 2/17/11
 * Time: 5:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class LinuxNetworkScanner implements IDeviceScanner<INetworkDevice> {
    private ISystemFacade facade;

    private static final Pattern addressPattern = Pattern.compile("inet addr\\:(.+?)\\s");
    private static final Pattern broadcastPattern = Pattern.compile("Bcast\\:(.+?)\\s");
    private static final Pattern netmaskPattern = Pattern.compile("Mask\\:(.+?)\\s");

    public LinuxNetworkScanner() {
        this(null);
    }

	public LinuxNetworkScanner( ISystemFacade facade ) {
		this.facade = facade;
	}

	public void setFacade( ISystemFacade facade ) {
		this.facade = facade;
	}
	
	protected ISystemFacade getFacade() {
		return this.facade;
	}

    @Override
	public Collection<INetworkDevice> scan() throws ScannerException {
        Collection<INetworkDevice> result = new HashSet<INetworkDevice>();

        String routeResult;
        try {
            IScriptExecutor routesExecutor = this.getFacade().getConsole().createExecutor("route");
            routeResult = routesExecutor.execute();
        } catch ( IOException e ) {
            throw new ScannerException("Unable to retrieve routes list");
        }

        IScriptExecutor executor = this.getFacade().getConsole().createExecutor("ifconfig");
        executor.addUnnamedParameter("-a");

        String output;
        try {
            output = executor.execute();
        } catch ( IOException e ) {
            throw new ScannerException( e.getMessage(), e );
        }

        String[] partitions = output.split("\\n{2,2}");
        for ( String partition : partitions ) {
            NetworkDevice device = new NetworkDevice();
            // TODO: must be replaces with device MAC-address instead of UUID
            device.setUUID( UUID.randomUUID() );

            StringBuilder name = new StringBuilder();
            int i = 0;
            while ( partition.charAt(i++) != ' ' ) {
                name.append( partition.substring( i - 1, i ) );
            }

            device.setName( name.toString() );

            try {
                Matcher matcher = addressPattern.matcher(partition);
                if ( matcher.find() ) {
                    device.setAddress( InetAddress.getByAddress(
                        StringUtils.stringToIP(
                            partition.substring( matcher.start(1), matcher.end(1) ) )
                    ) );
                }

                matcher = broadcastPattern.matcher(partition);
                if ( matcher.find() ) {
                    device.setBroadcast( InetAddress.getByAddress(
                        StringUtils.stringToIP(
								partition.substring(matcher.start(1), matcher.end(1)))

                    ) );
                }

                matcher = netmaskPattern.matcher(partition);
                if ( matcher.find() ) {
                    device.setNetmask( this.parseAddress(
                        partition.substring( matcher.start(1), matcher.end(1) )
                    ) );
                }

                device.setPath("/dev/" + device.getName() );

                if ( partition.contains("UP") ) {
                    device.setUp(true);
                }

                if ( partition.contains("LOOPBACK") ) {
                    device.setType( NetworkDeviceType.LOOPBACK );
                } else {
                    device.setType( NetworkDeviceType.INET );
                }

                this.processRoutes( routeResult, device );
            } catch ( UnknownHostException e ) {
                throw new ScannerException("Address parsing has been failed", e );
            }

            result.add( device );
        }

        return result;
	}

    protected InetAddress parseAddress( String address ) throws UnknownHostException {
        return InetAddress.getByAddress( StringUtils.stringToIP( address ) );
    }

    protected void processRoutes( String routeResult, INetworkDevice device ) throws UnknownHostException, ScannerException {
        String[] lines = routeResult.split("\\n");
        for ( int i = 2; i < lines.length; i++ ) {
            String line = lines[i].trim();
            if ( line.isEmpty() ) {
                continue;
            }

            String[] lineColumns = this.splitLine( line );
            if ( lineColumns[7].equals( device.getName() ) ) {
                String gateway = lineColumns[1].trim();
                if ( gateway.equals( "*" ) ) {
                    gateway = "0.0.0.0";
                }

                String address = lineColumns[0].trim();
                if ( address.equals("default") ) {
                    device.setGateway( this.parseAddress( gateway ) );
                    continue;
                }

                NetworkDeviceRoute route = new NetworkDeviceRoute();
                route.setGateway( this.parseAddress( gateway ) );
                route.setNetmask( this.parseAddress( lineColumns[2] ) );

                device.addRoute( route );
            }
        }
    }

    protected String[] splitLine( String line ) {
        String[] result = new String[8];

        StringBuilder builder = new StringBuilder();
        int pos = 0;
        int i = 0;
        while ( pos < line.length() ) {
            String c = line.substring( pos, 1 + pos++ );
            if ( c.equals(" ") || c.equals("\t") ) {
                if ( builder.length() != 0 ) {
                    result[i++] = builder.toString();
                    builder.delete(0, builder.length() );
                }

                continue;
            }

            builder.append( c );
        }

        if ( 0 != builder.length() && i != result.length ) {
            result[i++] = builder.toString();
        }

        return result;
    }

}
