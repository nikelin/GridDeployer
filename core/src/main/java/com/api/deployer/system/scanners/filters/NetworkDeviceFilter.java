package com.api.deployer.system.scanners.filters;

import com.api.commons.IFilter;
import com.api.deployer.system.devices.IDevice;
import com.api.deployer.system.devices.INetworkDevice;

/**
 * @author nikelin
 * @date 26/04/11
 * @package com.api.deployer.system.scanners.filters
 */
public class NetworkDeviceFilter implements IFilter<IDevice> {
    private IFilter<IDevice> chain;

    public NetworkDeviceFilter() {
        this(null);
    }

    public NetworkDeviceFilter( IFilter<IDevice> chain ) {
        this.chain = chain;
    }

    @Override
    public boolean filter(IDevice filterable) {
        return filterable instanceof INetworkDevice && ( this.chain == null || this.chain.filter(filterable) );
    }
}
