package com.api.deployer.execution;

import java.net.URI;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class SimpleExecutorDescriptor implements IExecutorDescriptor {
	private static final long serialVersionUID = 1741370630898839805L;
	
	private UUID stationId;
	private URI location;
	private Date startedOn;
	private Map<String, Object> attributes;
	
	public SimpleExecutorDescriptor( UUID stationId, URI location, Date startedOn ) {
		this.location = location;
		this.startedOn = startedOn;
		this.stationId = stationId;
	}
	
	@Override
	public UUID getStationID() {
		return this.stationId;
	}
	
	public void setLocation( URI location ) {
		this.location = location;
	}
	
	@Override
	public Date getStartedOn() {
		return this.startedOn;
	}
	
	@Override
	public URI getLocation() {
		return this.location;
	}

	@SuppressWarnings("unchecked")
	public <V> V getAttribute( String name ) {
		return (V) this.attributes.get(name);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <V> Map<String, V> getAttributes() {
		return (Map<String, V>) this.attributes;
	}
	
}
