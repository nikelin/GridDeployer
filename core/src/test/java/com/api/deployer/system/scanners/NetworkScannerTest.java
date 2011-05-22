package com.api.deployer.system.scanners;

import com.api.deployer.AbstractContextAwareTest;
import com.api.deployer.system.ISystemFacade;
import com.api.deployer.system.devices.INetworkDevice;
import com.api.deployer.system.devices.network.NetworkDeviceType;
import com.api.deployer.system.scanners.filters.NetworkDeviceFilter;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.*;

import java.util.Collection;

/**
 * @author nikelin
 * @date 27/04/11
 * @package com.api.deployer.system.scanners
 */
public class NetworkScannerTest extends AbstractContextAwareTest<Object> {
    private static final Logger log = Logger.getLogger(NetworkScannerTest.class);

    public NetworkScannerTest() {
        super("src/test/resources/context.xml");
    }

    protected ISystemFacade getFacade() {
        return this.getContext().getBean( ISystemFacade.class );
    }

    @Test
    public void testMain() throws ScannerException {
        Collection<INetworkDevice> devices = this.getFacade().getDevices( new NetworkDeviceFilter() );
        assertNotNull( devices );
        assertTrue( devices.size() > 0 );

        for ( INetworkDevice device : devices ) {
            log.info("Founded device: " + device.getPath() );
            assertNotNull( device.getType() );

            if ( device.getName().equals("eth0") ) {
                assertEquals(NetworkDeviceType.INET, device.getType() );
                assertEquals( "/192.168.100.4", device.getGateway().toString() );
                assertEquals( 2, device.getRoutes().size() );
            }
        }
    }

}
