package com.api.deployer.ui.data.workstations;

import java.util.Collection;
import java.util.UUID;

import com.api.deployer.system.devices.IDevice;
import com.api.deployer.ui.data.workstations.groups.WorkstationGroup;
import com.redshape.ui.data.AbstractModelData;

public class Workstation extends AbstractModelData implements IDeploySubject {
	
	public Workstation() {
		this(null);
	}
	
	public Workstation( String name ) {
		this(name, null);
	}
	
	public Workstation( String name, String hostname ) {
		this.set( WorkstationModel.NAME, name );
		this.set( WorkstationModel.HOSTNAME, hostname );
	}

	@Override
	public boolean isComposed() {
		return false;
	}

	@Override
	public IComposedDeploySubject asComposed() {
		if ( !this.isComposed() ) {
			throw new UnsupportedOperationException("Current object is not composed");
		}

		return (IComposedDeploySubject) this;
	}
	
	public void setGroup( WorkstationGroup group ) {
		this.set( WorkstationModel.GROUP, group );
	}
	
	public WorkstationGroup getGroup() {
		return this.get( WorkstationModel.GROUP );
	}

	@Override
	public UUID getId() {
		return this.get( WorkstationModel.ID );
	}
	
	public void setId( UUID id ) {
		this.set( WorkstationModel.ID, id );
	}
	
	public void setDevices( Collection<IDevice> devices ) {
		this.set( WorkstationModel.DEVICES, devices );
	}
	
	public Collection<IDevice> getDevices() {
		return this.get( WorkstationModel.DEVICES );
	}
	
	public void setName( String name ) {
		this.set( WorkstationModel.NAME, name );
	}
	
	public String getName() {
		return String.valueOf( this.get( WorkstationModel.NAME ) );
	}
	
	public void setHostname( String name ) {
		this.set( WorkstationModel.HOSTNAME, name );
	}
	
	public String getHostname() {
		return String.valueOf( this.get( WorkstationModel.HOSTNAME ) );
	}

	@Override
	public String toString() {
		return this.getName();
	}
	
}
