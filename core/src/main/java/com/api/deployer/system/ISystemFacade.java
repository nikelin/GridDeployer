package com.api.deployer.system;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

import com.api.commons.IFilter;
import com.api.deployer.system.configurers.IPartitionsEditor;
import com.api.deployer.system.configurers.network.INetworkConfigurer;
import com.api.deployer.system.devices.IDevice;
import com.api.deployer.system.devices.storage.IStorageDriveDevice;
import com.api.deployer.system.scanners.ScannerException;
import com.api.deployer.system.console.IConsole;

/**
 * Interface for system interaction facade
 * 
 * @author nikelin
 */
public interface ISystemFacade {

	public void init();

	public void refresh() throws ScannerException;
	
	public boolean isUnderRoot() throws IOException;

	public UUID detectStationId() throws IOException;
	
	public IConsole getConsole();
	
	public IPartitionsEditor createPartitionsEditor( IStorageDriveDevice device );

    public INetworkConfigurer getNetworkConfigurer();
	
	public <T extends IDevice> T getDevice( IFilter<IDevice> filter ) throws ScannerException;
	
	public Collection<IDevice> getDevices() throws ScannerException;

	public <T extends IDevice> Collection<T> getDevices( IFilter<IDevice> filter) throws ScannerException;

	public void reboot() throws IOException;
	
	public void reboot( Integer delay ) throws IOException;

	public void shutdown() throws IOException;
	
	public void shutdown( Integer delay )  throws IOException;
	
	public String getPlatformAttribute( PlatformAttribute attr ) throws IOException;

    public Integer getStationArch() throws IOException;

}