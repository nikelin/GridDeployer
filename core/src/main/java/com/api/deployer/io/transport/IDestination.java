package com.api.deployer.io.transport;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

import com.redshape.bindings.annotations.Bindable;

public interface IDestination extends Serializable {
	
	@Bindable( name = "Remote URI" )
	public void setURI( URI uri );
	
	public URI getURI() throws URISyntaxException;
	
}
