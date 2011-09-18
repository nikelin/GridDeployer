package com.api.deployer.io.transport.local.local;

import com.api.deployer.io.transport.local.LocalDestination;
import com.api.deployer.io.transport.mounters.IDestinationMounter;
import com.api.deployer.io.transport.mounters.MountException;

import java.net.URI;
import java.util.UUID;

public class LocalMounter implements IDestinationMounter<LocalDestination> {
	private String mountingPoint;

	@Override
	public String getMountingPoint() {
		return this.mountingPoint;
	}
	
	@Override
	public void setMountingPoint(String point) {
		this.mountingPoint = point;
	}
	
	@Override
	public String mount(LocalDestination destination) throws MountException { 
		return this.mount(destination, false);
	}

	@Override
	public String mount( LocalDestination destination, boolean rootOnly ) throws MountException {
		return destination.getURI().getRawSchemeSpecificPart() + ( rootOnly ? "/" + UUID.randomUUID() : "" );
	}

	@Override
	public boolean isSupportedURI(URI uri) {
		return uri.getScheme().equals("file")
			|| uri.getScheme().equals("");
	}

}