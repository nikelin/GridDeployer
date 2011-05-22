package com.api.deployer.ui.data.workstations;

import com.api.deployer.system.devices.IDevice;
import com.api.deployer.ui.data.workstations.groups.WorkstationGroup;
import com.redshape.ui.data.AbstractModelType;
import com.redshape.ui.data.IModelData;

public class WorkstationModel extends AbstractModelType {
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String HOSTNAME = "hostname";
	public static final String DESCRIPTOR = "descriptor";
	public static final String DEVICES = "devices";
	public static final String GROUP = "group";
	
	public WorkstationModel() {
		this.addField( ID )
			.setType( String.class );
		
		this.addField( GROUP )
			.setType( WorkstationGroup.class )
			.setTitle("Group");
		
		this.addField(NAME)
			.setTitle("Workstation ID");
		
		this.addField(DEVICES)
			.markList( true )
			.setType( IDevice.class );
		
		this.addField(DESCRIPTOR)
			.setTitle("Descriptor");
		
		this.addField(HOSTNAME)
			.setTitle("Hostname");
	}
	
	@Override
	public IModelData createRecord() {
		return new Workstation();
	}
	
	
}
