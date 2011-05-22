package com.api.deployer.system.scanners.mock;

import com.api.commons.IFilter;
import com.api.deployer.system.configurers.IPartitionsEditor;
import com.api.deployer.system.configurers.network.INetworkConfigurer;
import com.api.deployer.system.devices.IDevice;
import com.api.deployer.system.devices.storage.IStorageDriveDevice;
import com.api.deployer.system.scanners.ScannerException;
import com.api.deployer.system.ISystemFacade;
import com.api.deployer.system.PlatformAttribute;
import com.api.deployer.system.console.IConsole;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: semichevskiy
 * Date: 2/28/11
 * Time: 2:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class MockSystemFacade implements ISystemFacade{
    private IConsole console;

    public MockSystemFacade(IConsole console) {
        this.console = console;
    }

    @Override
    public void init() {
    }

    @Override
    public void refresh() throws ScannerException {
    }

    @Override
    public INetworkConfigurer getNetworkConfigurer() {
        return null;
    }

    @Override
    public Integer getStationArch() throws IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public IConsole getConsole() {
        return this.console;
    }

    @Override
    public <T extends IDevice> T getDevice(IFilter<IDevice> device) throws ScannerException {
        return null;
    }

    @Override
    public Collection<IDevice> getDevices() throws ScannerException {
        return null;
    }

    @Override
    public <T extends IDevice> Collection<T> getDevices(IFilter<IDevice> filter) throws ScannerException {
        return null;
    }

    @Override
    public void reboot() throws IOException {
    }

    @Override
    public void reboot(Integer delay) throws IOException {
    }

    @Override
    public void shutdown() throws IOException {
    }

    @Override
    public void shutdown(Integer delay) throws IOException {
    }

    @Override
    public String getPlatformAttribute(PlatformAttribute attr) throws IOException {
        return null;
    }

	@Override
	public boolean isUnderRoot() throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IPartitionsEditor createPartitionsEditor(IStorageDriveDevice device) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UUID detectStationId() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}
