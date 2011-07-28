package com.api.deployer.system.linux.monitoring;

import com.api.deployer.system.monitoring.IMonitor;
import com.api.deployer.system.ISystemFacade;

import java.io.IOException;

/**
 * @author nikelin
 * @date 22/04/11
 * @package com.api.deployer.system.monitoring
 */
public class CPUMonitor extends AbstractTopMonitor {
	private ISystemFacade facade;

	public CPUMonitor( ISystemFacade facade ) {
		this.facade = facade;
	}

	protected ISystemFacade getFacade() {
		return this.facade;
	}

	@Override
	public Float getUsage() throws IOException {
		return this.getCPUUsage();
	}

	@Override
	public Float getLimit() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Float getFree() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
