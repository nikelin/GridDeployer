package com.api.deployer.ui.data.workstations.groups;

import com.redshape.ui.data.AbstractModelType;

public class WorkstationGroupModel extends AbstractModelType {
	public static final String ITEMS = "items";
	public static final String NAME = "name";
	
	public WorkstationGroupModel() {
		super();
		
		this.addField( NAME )
			.setType( String.class );
		
		this.addField( ITEMS )
			.markList(true)
			.setType( WorkstationGroup.class );
	}
	
	public WorkstationGroup createRecord() {
		return new WorkstationGroup();
	}
	

}
