package com.api.deployer.system.scanners.filters;

import java.util.UUID;

import com.api.commons.IFilter;
import com.api.deployer.system.devices.IDevice;

public class UUIDDeviceFilter implements IFilter<IDevice> {
	private UUID uuid;
	
	public UUIDDeviceFilter( UUID uuid ) {
		this.uuid = uuid;
	}
	
	@Override
	public boolean filter( IDevice device ) {
		if ( device.getUUID() != null && device.getUUID().equals( this.uuid ) ) {
			return true;
		}
		
		return false;
	}
	
}
