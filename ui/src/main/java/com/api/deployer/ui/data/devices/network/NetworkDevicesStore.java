package com.api.deployer.ui.data.devices.network;

import com.api.deployer.ui.data.devices.AbstractDeviceModel;
import com.api.deployer.ui.data.devices.AbstractDeviceStorage;
import com.redshape.ui.data.loaders.IDataLoader;

/**
 * @author nikelin
 * @date 26/04/11
 * @package com.api.deployer.ui.data.devices.network
 */
public class NetworkDevicesStore extends AbstractDeviceStorage<NetworkDevice> {

    public NetworkDevicesStore( IDataLoader<NetworkDevice> networkDeviceIDataLoader) {
        super( new NetworkDeviceModel(), networkDeviceIDataLoader);
    }

}
