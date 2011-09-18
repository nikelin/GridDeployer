package com.api.deployer.execution;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public interface IExecutorDescriptor extends Serializable {
	
	public UUID getStationID();
	
	public URI getLocation();
	
	public Date getStartedOn();
	
	public <V> V getAttribute( String attribute );
	
	public <V> Map<String, V> getAttributes();

}