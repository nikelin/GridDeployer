package com.api.deployer.io.transport.mounters;

import java.net.URI;

public interface IDestinationMounter<T> {
	
	public String getMountingPoint();
	
	public void setMountingPoint( String point );
	
	public boolean isSupportedURI( URI uri );
	
	public String mount( T destination ) throws MountException;
	
	public String mount( T destination, boolean rootOnly ) throws MountException;
}
