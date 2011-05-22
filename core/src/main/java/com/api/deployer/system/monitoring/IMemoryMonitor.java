package com.api.deployer.system.monitoring;

/**
 * @author nikelin
 * @date 22/04/11
 * @package com.api.deployer.system.monitoring
 */
public interface IMemoryMonitor extends IMonitor {

	public Integer getTotalMemory();

	public Integer getTotalSwap();

	public Integer getUsedMemory();

	public Integer getUsedSwap();

	public Integer getFreeMemory();

	public Integer getFreeSwap();
}
