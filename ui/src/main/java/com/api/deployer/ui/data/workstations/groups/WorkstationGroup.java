package com.api.deployer.ui.data.workstations.groups;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import com.api.deployer.ui.data.workstations.IComposedDeploySubject;
import com.api.deployer.ui.data.workstations.IDeploySubject;
import com.api.deployer.ui.data.workstations.Workstation;
import com.redshape.ui.data.AbstractModelData;

public class WorkstationGroup extends AbstractModelData implements IComposedDeploySubject {
	private static final long serialVersionUID = -381131489815623996L;

	public WorkstationGroup() {
		this(null);
	}
	
	public WorkstationGroup( String name ) {
		this.set( WorkstationGroupModel.NAME, name );
		this.set( WorkstationGroupModel.ITEMS, new HashSet<WorkstationGroup>() );
	}

	public void setName( String name ) {
		this.set( WorkstationGroupModel.NAME, name);
	}
	
	public String getName() {
		return this.get( WorkstationGroupModel.NAME );
	}
	
	public void addWorkstation( Workstation workstation ) {
		this.getWorkstations().add(workstation);
	}
	
	public void removeWorkstation( Workstation station ) {
		this.getWorkstations().remove(station);
	}
	
	public Collection<Workstation> getWorkstations() {
		return this.get(WorkstationGroupModel.ITEMS);
	}

	@Override
	public UUID getId() {
		throw new UnsupportedOperationException("Operation not supports");
	}

	@Override
	public <T extends IDeploySubject> Collection<T> getChildren() {
		return (Collection<T>) this.getWorkstations();
	}

	@Override
	public boolean isComposed() {
		return true;
	}

	@Override
	public IComposedDeploySubject asComposed() {
		return this;
	}

	@Override
	public boolean equals( Object object ) {
		if ( object instanceof WorkstationGroup ) {
			return this.getName().equals( ((WorkstationGroup) object).getName() );
		}

		return super.equals(object);
	}

	@Override
	public String toString() {
		return this.getName();
	}
	
}