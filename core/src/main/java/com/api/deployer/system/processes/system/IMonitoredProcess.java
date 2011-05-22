package com.api.deployer.system.processes.system;

/**
 * @author nikelin
 * @date 22/04/11
 * @package com.api.deployer.system.processes.system
 */
public interface IMonitoredProcess {

	public Integer getPid();

	public String getUser();

	public Integer getPriority();

	public Integer getVirtUsage();

	public Integer getPhysUsage();

	public Integer getCpuUsage();

	public Float getCPUTime();

	public String getCommand();

}
