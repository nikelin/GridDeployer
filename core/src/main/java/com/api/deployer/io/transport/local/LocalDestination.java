package com.api.deployer.io.transport.local;

import java.net.URI;

import com.api.deployer.io.transport.IDestination;

public class LocalDestination implements IDestination {
	private URI uri;
	
	public LocalDestination( URI url ) {
		this.uri = url;
	}
	
	@Override
	public URI getURI() {
		return this.uri;
	}

	@Override
	public void setURI(URI url) {
		this.uri = url;
	}

}
