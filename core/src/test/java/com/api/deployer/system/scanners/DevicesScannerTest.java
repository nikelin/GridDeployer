package com.api.deployer.system.scanners;

import java.io.IOException;
import java.util.Collection;

import static org.junit.Assert.*;

import com.api.deployer.system.ISystemFacade;
import org.junit.Test;

import com.api.deployer.AbstractContextAwareTest;
import com.api.deployer.system.devices.IDevice;

public class DevicesScannerTest extends AbstractContextAwareTest<Object> {
	
	public DevicesScannerTest() {
		super("src/test/resources/context.xml");
	}
	
	public ISystemFacade getSystemFacade() {
		return (ISystemFacade) this.getContext().getBean( "systemFacade" );
	}
	
	@Test
	public void testMain() throws ScannerException, IOException {
		ISystemFacade facade = this.getSystemFacade();
		if ( !facade.isUnderRoot() ) {
			throw new ScannerException("Needs to be under root process");
		}
		
		Collection<IDevice> devices = facade.getDevices();
		assertNotNull(devices);
		assertFalse( devices.isEmpty() );
	}

}