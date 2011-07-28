package com.api.deployer.io.transport.device;

import com.api.deployer.system.devices.storage.IStorageDriveDevice;
import com.api.deployer.io.transport.IDestination;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 28/03/11
 * Time: 1:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeviceDestination implements IDestination {
    private URI uri;
    private IStorageDriveDevice device;

    public DeviceDestination( IStorageDriveDevice device ) {
        this.device = device;
    }

    public IStorageDriveDevice getDevice() {
        return device;
    }

    public void setDevice(IStorageDriveDevice device) {
        this.device = device;
    }

    @Override
    public void setURI(URI uri) {
        this.uri = uri;
    }

    @Override
    public URI getURI() throws URISyntaxException {
        return this.uri;
    }
}
