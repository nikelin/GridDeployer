package com.api.deployer.system.linux.monitoring;

import com.api.commons.TimeSpan;
import com.api.deployer.system.ISystemFacade;
import com.api.deployer.system.monitoring.IMonitor;
import com.api.deployer.system.processes.system.IMonitoredProcess;
import com.api.deployer.system.processes.system.MonitoredProcess;
import com.api.deployer.system.scripts.IScriptExecutor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

/**
 * @author nikelin
 * @date 22/04/11
 * @package com.api.deployer.system.linux.monitoring
 */
public abstract class AbstractTopMonitor implements IMonitor {
	private ISystemFacade facade;
	private IScriptExecutor executor;

	public void setFacade( ISystemFacade facade ) {
		this.facade = facade;
	}

	protected ISystemFacade getFacade() {
		return this.facade;
	}

	protected IScriptExecutor prepareExecutor() {
		if ( this.executor == null ) {
			return this.executor;
		}

		IScriptExecutor executor = this.executor = this.getFacade().getConsole().createExecutor("top");
		executor.addUnnamedParameter("-b");
		executor.addUnnamedParameter("-c 1");
		return executor;
	}

	protected BufferedReader executeCommand() throws IOException {
		return new BufferedReader( new StringReader( this.prepareExecutor().execute() ) );
	}

	protected String readLine( BufferedReader reader, int line ) throws IOException {
		int i = 1;
		while ( i++ != line ) {
			reader.readLine();
			i++;
		}

		return reader.readLine();
	}

	public Collection<IMonitoredProcess> getProcesses() throws IOException {
		BufferedReader reader = this.executeCommand();

		Collection<IMonitoredProcess> processes = new HashSet<IMonitoredProcess>();

		String line;
		int i = 8;
		while ( null != ( line = this.readLine( reader, i++ ) ) ) {
			MonitoredProcess process = new MonitoredProcess();

			StringBuffer buffer = new StringBuffer();
			int pos = 0;
			int cell = 0;
			while ( pos++ < line.length() ) {
				char c = line.charAt(pos);
				if ( c != ' ' ) {
					buffer.append( c );
				} else {
					String value = buffer.toString().trim();
					switch ( cell++ ) {
						case 0:
							process.setPid( Integer.valueOf(value) );
						break;
						case 1:
							process.setUser( value );
						break;
						case 2:
							process.setPriority(Integer.valueOf(value));
						break;
						case 4:
							process.setVirtUsage( Integer.valueOf( value.replace("m", "") ) );
						break;
						case 6:
							process.setPhysUsage( Integer.valueOf( value.replace("m", "") ) );
						break;
						case 8:
							process.setCpuUsage( Integer.valueOf( value ) );
						break;
						case 10:
							process.setCpuTime(0F);
						break;
					}
				}
			}

			processes.add(process);
		}

		return processes;
	}

	public Integer getTasksCount() throws IOException {
		String[] arguments = this.readLine( this.executeCommand(), 2).split(",");
		String tasksValue = arguments[0].substring( arguments[0].indexOf("Tasks:"), arguments[0].indexOf("total") ).trim();

		return Integer.valueOf( tasksValue.trim() );
	}

	public Float getCPUUsage() throws IOException {
		String[] arguments = this.readLine( this.executeCommand(), 3 ).split(",");
		String cpuValue = arguments[0].substring( arguments[0].indexOf("Cpu(s):") + 7, arguments[0].indexOf("%") );

		return Float.valueOf( cpuValue.trim() );
	}

	public Integer getTotalMemory() throws IOException {
		String[] arguments = this.readLine( this.executeCommand(), 4 ).split(",");
		String totalValue = arguments[0].substring( arguments[0].indexOf("Mem:") + 4, arguments[0].indexOf("total") );

		return Integer.valueOf( totalValue.replace("k", "").trim() );
	}

	public Integer getTotalSwap() throws IOException {
		String[] arguments = this.readLine( this.executeCommand(), 5 ).split(",");
		String totalValue = arguments[0].substring( arguments[0].indexOf("Swap:") + 5, arguments[0].indexOf("total") );

		return Integer.valueOf( totalValue.replace("k", "").trim() );
	}

	public Integer getUsedMemory() throws IOException {
		String[] arguments = this.readLine( this.executeCommand(), 4 ).split(",");
		String totalValue = arguments[0].substring( 0, arguments[1].indexOf("used") );

		return Integer.valueOf( totalValue.replace("k", "").trim() );
	}

	public Integer getUsedSwap() throws IOException {
		String[] arguments = this.readLine( this.executeCommand(), 4 ).split(",");
		String totalValue = arguments[1].substring(0, arguments[1].indexOf( "used" ) );

		return Integer.valueOf( totalValue.replace("k", "").trim() );
	}

	public Integer getFreeMemory() throws IOException {
		String[] arguments = this.readLine( this.executeCommand(), 4 ).split(",");
		String totalValue = arguments[2].substring(0, arguments[2].indexOf( "free" ) );

		return Integer.valueOf( totalValue.replace("k", "").trim() );
	}

	public Integer getFreeSwap() throws IOException {
		String[] arguments = this.readLine( this.executeCommand(), 4 ).split(",");
		String totalValue = arguments[2].substring(0, arguments[2].indexOf( "free" ) );

		return Integer.valueOf( totalValue.replace("k", "").trim() );
	}

}
