package com.api.deployer.ui.data.devices.network;

import com.api.deployer.ui.data.devices.AbstractDeviceModel;

/**
 * @author nikelin
 * @date 26/04/11
 * @package com.api.deployer.ui.data.devices.network
 */
public class NetworkDeviceModel extends AbstractDeviceModel {
    public static final String ADDRESS = "address";
    public static final String GATEWAY = "gateway";
    public static final String BROADCAST = "broadcast";
    public static final String NETMASK = "netmask";
    public static final String ROUTES = "routes";

    public NetworkDeviceModel() {
        super();

        this.addField( ADDRESS )
            .setTitle("Address");

        this.addField( GATEWAY )
            .setTitle("Gateway");

        this.addField( BROADCAST )
            .setTitle("Broadcast");

        this.addField( NETMASK )
            .setTitle("Netmask");
    }

    @Override
    public NetworkDevice createRecord() {
        return new NetworkDevice();
    }

}
