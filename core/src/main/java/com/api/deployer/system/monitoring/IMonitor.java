package com.api.deployer.system.monitoring;

import com.api.deployer.system.devices.IDevice;

import java.io.IOException;

/**
 * @author nikelin
 * @date 22/04/11
 * @package com.api.deployer.system.monitoring
 */
public interface IMonitor {

	/**
	 * Get total monitor usage
	 * @return
	 */
	public Float getUsage() throws IOException;

	/**
	 * Get usage limit
	 * @return
	 */
	public Float getLimit() throws IOException;

	/**
	 * Get free amount of usage capability
	 * @return
	 */
	public Float getFree() throws IOException;

}
