package com.api.deployer.system.linux;

import com.api.commons.IFilter;
import com.api.deployer.system.configurers.network.INetworkConfigurer;
import com.api.deployer.system.devices.IDevice;
import com.api.deployer.system.devices.storage.IStorageDriveDevice;
import com.api.deployer.system.linux.configurers.network.StatelessNetworkConfigurer;
import com.api.deployer.system.scanners.IDeviceScanner;
import com.api.deployer.system.scanners.ScannerException;
import com.api.deployer.system.INativesLoader;
import com.api.deployer.system.configurers.IPartitionsEditor;
import com.api.deployer.system.ISystemFacade;
import com.api.deployer.system.PlatformAttribute;
import com.api.deployer.system.console.IConsole;
import com.api.deployer.system.scripts.IScriptExecutor;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * @author semichevsky
 * @author nikelin
 */
public class SystemFacade implements ISystemFacade {
	private static Logger log = Logger.getLogger( SystemFacade.class );
	
	private static final Pattern UUID_PATTERN = Pattern.compile("UUID: (.*?)\\n");

	private IConsole console;
    private INetworkConfigurer networkConfigurer;
    private Collection<IDeviceScanner<IDevice>> scanners = new HashSet<IDeviceScanner<IDevice>>();
    private Collection<IDevice> devices = new HashSet<IDevice>();
    private boolean scanned;
    private boolean refreshDevices;
    private INativesLoader nativesLoader;
    
    public SystemFacade( IConsole console ){
    	this.console = console;
    }
    
    public void setNativesLoader( INativesLoader loader ) {
    	this.nativesLoader = loader;
    }
    
    protected INativesLoader getNativesLoader() {
    	return this.nativesLoader;
    }
    
    /* (non-Javadoc)
	 * @see com.api.deployer.system.ISystemFacade#getConsole()
	 */
    public IConsole getConsole() {
    	return this.console;
    }
    
    public void setScanners( Collection<IDeviceScanner<IDevice>> scanners ) {
    	for ( IDeviceScanner<IDevice> device : scanners ) {
    		device.setFacade( this );
    	}
    	
    	this.scanners = scanners;
    }
    
    public Collection<IDeviceScanner<IDevice>> getScanners() {
    	return this.scanners;
    }

    @Override
	public void init(){
    	
    }

    @Override
    public INetworkConfigurer getNetworkConfigurer() {
        if ( this.networkConfigurer == null ) {
            this.networkConfigurer = new StatelessNetworkConfigurer(this);
        }

        return this.networkConfigurer;
    }

    @Override
    public UUID detectStationId() throws IOException {
    	IScriptExecutor executor = this.getConsole().createExecutor("dmidecode");
    	executor.addUnnamedParameter("--type")
    			.addUnnamedParameter( 1 );
    	
    	String output = executor.execute();
    	
    	Matcher matcher = UUID_PATTERN.matcher(output);
    	if ( !matcher.find() ) {
    		throw new IOException("Cannot detect UUID from dmidetect output");
    	}
    	
    	return UUID.fromString( output.substring( matcher.start(1), matcher.end(1) ) );
    }
    
    @Override 
    /**
     * @about if we running under the root privileges id -u will return "0" value
     */
    public boolean isUnderRoot() throws IOException {
    	String result = this.getConsole().createExecutor("id").addUnnamedParameter("-u").execute();
    	log.info("isUnderRoot result: " + result);
    	return result.startsWith("0");
    }

    @Override
	public void refresh() throws ScannerException {
    	this.refreshDevices = true;
    	this.devices = this.getDevices();
    }
    
    @Override
    public IPartitionsEditor createPartitionsEditor( IStorageDriveDevice device ) {
    	return new PartedPartitionsEditor( this, device);
    }
    
    @Override
	public Collection<IDevice> getDevices() throws ScannerException {
    	if ( this.scanned && !this.refreshDevices ) {
    		return this.devices;
    	}
    	
    	Collection<IDevice> devices = new HashSet<IDevice>();
    	for ( IDeviceScanner<IDevice> scanner : this.getScanners() ) {
    		devices.addAll( scanner.scan() );
    	}
    	
    	this.devices = devices;
    	this.scanned = true;
    	this.refreshDevices = false;
    	
    	return devices;
    }
    
    @Override
    public <T extends IDevice> T getDevice( IFilter<IDevice> filter ) throws ScannerException {
    	Collection<T> devices = this.getDevices(filter);
    	if ( devices.isEmpty() ) {
    		return null;
    	}
    	
    	return devices.iterator().next();
    }

    @Override
	public <T extends IDevice> Collection<T> getDevices(IFilter<IDevice> filter)  throws ScannerException {
    	Collection<T> result = new HashSet<T>();
    	
    	Collection<IDevice> devices = this.getDevices();
    	for ( IDevice device : devices ) {
    		if ( filter.filter( device) ) {
    			result.add( (T) device );
    		}
    	}
    	
        return result;
    }

    @Override
	public void reboot() throws IOException {
    	this.reboot(0);
    }
    
    @Override
	public void shutdown() throws IOException {
    	this.shutdown(0);
    }

    @Override
	public String getPlatformAttribute( PlatformAttribute attr ) throws IOException {
    	switch ( attr ) {
    	case KERNEL_RELEASE:
    		return this.getConsole().createExecutor("uname").addUnnamedParameter("-r").execute();
    	case NETWORK_NAME:
    		return this.getConsole().createExecutor("uname").addUnnamedParameter("-n").execute();
    	case PROCESSOR:
    		return this.getConsole().createExecutor("uname").addUnnamedParameter("-p").execute();
    	case OS_FAMILY:
    		return this.getConsole().createExecutor("uname").addUnnamedParameter("-s").execute();
    	case KERNEL_VERSION:
    		return this.getConsole().createExecutor("uname").addUnnamedParameter("-v").execute();
    	}
    	
    	throw new IllegalArgumentException("Unknown attribute in request");
    }

    @Override
    public Integer getStationArch() throws IOException {
        return Integer.valueOf( System.getProperty("sun.arch.data.mode") );
    }

	@Override
	public void reboot(Integer delay) throws IOException {
		this.getConsole().createExecutor("reboot").addUnnamedParameter(delay).execute();
	}

	@Override
	public void shutdown(Integer delay) throws IOException {
		this.getConsole().createExecutor("shutdown").addUnnamedParameter(delay).execute();
	}

}