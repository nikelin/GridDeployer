package com.api.deployer.system.scanners;

import java.util.Collection;

import com.api.deployer.system.ISystemFacade;
import com.api.deployer.system.devices.IDevice;

/**
 * @author nikelin
 */
public interface IDeviceScanner<T extends IDevice> {
   
	// TODO: remove it's from interface
	public void setFacade( ISystemFacade facade );
	
	public Collection<T> scan() throws ScannerException;
	
}
