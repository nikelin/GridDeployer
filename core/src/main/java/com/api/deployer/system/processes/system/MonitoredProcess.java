package com.api.deployer.system.processes.system;

/**
 * @author nikelin
 * @date 22/04/11
 * @package com.api.deployer.system.processes.system
 */
public class MonitoredProcess implements IMonitoredProcess {

	private Integer pid;
	private String user;
	private Integer priority;
	private Integer virtUsage;
	private Integer physUsage;
	private Integer cpuUsage;
	private Float cpuTime;
	private String command;

	@Override
	public Integer getPid() {
		return this.pid;
	}

	@Override
	public String getUser() {
		return this.user;
	}

	@Override
	public Integer getPriority() {
		return this.priority;
	}

	@Override
	public Integer getVirtUsage() {
		return this.virtUsage;
	}

	@Override
	public Integer getPhysUsage() {
		return this.physUsage;
	}

	@Override
	public Integer getCpuUsage() {
		return this.cpuUsage;
	}

	@Override
	public Float getCPUTime() {
		return this.cpuTime;
	}

	@Override
	public String getCommand() {
		return this.command;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public void setVirtUsage(Integer virtUsage) {
		this.virtUsage = virtUsage;
	}

	public void setPhysUsage(Integer physUsage) {
		this.physUsage = physUsage;
	}

	public void setCpuUsage(Integer cpuUsage) {
		this.cpuUsage = cpuUsage;
	}

	public void setCpuTime(Float cpuTime) {
		this.cpuTime = cpuTime;
	}

	public void setCommand(String command) {
		this.command = command;
	}
}
