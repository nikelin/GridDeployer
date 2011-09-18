package com.api.deployer.io.transport.ssh.mounters;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.api.deployer.io.transport.mounters.IDestinationMounter;
import com.api.deployer.io.transport.mounters.MountException;
import com.api.deployer.io.transport.ssh.SSHDestination;
import com.api.deployer.system.ISystemFacade;
import com.api.deployer.system.processes.ISystemProcess;
import com.api.deployer.system.scripts.IScriptExecutionHandler;
import com.api.deployer.system.scripts.IScriptExecutor;
import com.api.deployer.system.scripts.IScriptListExecutor;
import com.api.deployer.system.scripts.ScriptListStyle;

public class SSHMounter implements IDestinationMounter<SSHDestination>{
	private static final Logger log = Logger.getLogger( SSHMounter.class );
	
	private ISystemFacade system;
	private String mountingPoint;
	
	public SSHMounter( ISystemFacade facade ) {
		this.system = facade;
	}
	
	@Override
	public void setMountingPoint( String path ) {
		this.mountingPoint = path;
	}
	
	@Override
	public String getMountingPoint() {
		return this.mountingPoint;
	}
	
	protected ISystemFacade getSystem() {
		return this.system;
	}
	
	protected String prepareMountPoint() throws IOException {
		log.info("Preparing mount point: " + this.getMountingPoint() );
		
		if ( !this.checkPathExists( this.getMountingPoint() ) ) {
			throw new IOException("Mounting point does not exists!");
		}
		
		String preparedPoint = this.getMountingPoint() + File.separator + UUID.randomUUID();
		if ( !this.checkPathExists( preparedPoint ) ) {
			this.createTempDirectory( preparedPoint );
		}
		
		return preparedPoint;
	}
	
	protected void createTempDirectory( String path ) throws IOException {
		this.getSystem().getConsole().createExecutor("mkdir").addUnnamedParameter( path ).execute();
	}
	
	/**
	 * TODO: move to utilities
	 * @param path
	 * @return
	 */
	protected boolean checkPathExists( String path ) {
		return new File(path).exists();
	}
	
	@Override
	public String mount( SSHDestination destination ) throws MountException  {
		return this.mount(destination, false);
	}
	
	@Override
	public String mount( SSHDestination destination, boolean rootOnly ) throws MountException {
		try {
			String targetPath = this.getMountingPoint();
			if ( !rootOnly ) {
				targetPath = this.prepareMountPoint();
			}
			
			IScriptListExecutor<IScriptExecutor> executor = this.getSystem().getConsole().createListExecutor();
			
			IScriptExecutor passwordPipe = this.getSystem().getConsole().createExecutor("echo");
			passwordPipe.addUnnamedParameter( destination.getPassword() );
			
			IScriptExecutor script = this.getSystem().getConsole().createExecutor("sshfs");
			script.addUnnamedParameter( "-o password_stdin,nonempty" );
			script.addUnnamedParameter( destination.toString() );
			script.addUnnamedParameter( this.getMountingPoint() );
			
			script.setHandler( new IScriptExecutionHandler() {
				@Override
				public boolean onExit(String output) {
					return true;
				}
				
				@Override
				public void onError(String output) {
					log.info(output);
				}

				@Override
				public Integer onProgressRequested(ISystemProcess process)
						throws IOException {
					return process.exitValue() == 1 ? 100 : 0;
				}
			});
			
			executor.addScript( passwordPipe );
			executor.addScript( script );
			executor.setListStyle( ScriptListStyle.INCLUSIVE );
			
			log.info( executor.getExecCommand() );
			
			String output = executor.execute();
			
			if ( !executor.isSuccess() ) {
				throw new MountException("Cannot mount specified destination: " + output );
			}
			
			return targetPath;
		} catch ( IOException e ) {
			throw new MountException( e.getMessage(), e );
		}
	}
	
	@Override
	public boolean isSupportedURI( URI url ) {
		return url.getScheme().equals("ssh");
	}
	
}
