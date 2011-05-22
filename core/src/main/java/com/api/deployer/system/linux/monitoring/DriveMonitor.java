package com.api.deployer.system.linux.monitoring;

import com.api.deployer.system.devices.storage.IStorageDriveDevice;
import com.api.deployer.system.monitoring.IDriveMonitor;

import java.io.IOException;

/**
 * @author nikelin
 * @date 22/04/11
 * @package com.api.deployer.system.monitoring
 */
public class DriveMonitor implements IDriveMonitor {

	@Override
	public Float getReadSpeed(IStorageDriveDevice device) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Integer getReadAmount(IStorageDriveDevice device) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Float getWriteSpeed(IStorageDriveDevice device) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Integer getWrittenAmount(IStorageDriveDevice device) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Float getUsage() throws IOException {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Float getLimit() throws IOException {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Float getFree() throws IOException {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
