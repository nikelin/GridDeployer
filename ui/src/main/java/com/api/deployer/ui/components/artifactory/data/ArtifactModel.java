package com.api.deployer.ui.components.artifactory.data;

import java.util.Date;
import java.util.UUID;

import com.api.deployer.ui.data.BackupType;
import com.redshape.ui.data.AbstractModelType;
import com.redshape.ui.data.IModelData;

public class ArtifactModel extends AbstractModelType {
	public static final String DATE = "date";
	public static final String TYPE = "type";
	public static final String HASH = "hash";
	public static final String ID   = "id";
	public static final String NAME = "name";
	public static final String SIZE = "size";
	public static final String DESCRIPTION = "description";
	public static final String CHILDREN = "children";
	
	public ArtifactModel() {
		super();
		
		this.addField("id")
			.setTitle("ID")
			.setType( UUID.class );
		
		this.addField("size")
			.setTitle("Size")
			.setType( Integer.class );
		
		this.addField("name")
			.setTitle("Name")
			.setType( String.class );
			
		this.addField("type")
			.setTitle("Artifact Type")
			.setType( BackupType.class );
		
		this.addField("date")
			.setTitle("Created")
			.setType( Date.class );
		
		this.addField("hash")
			.setTitle("Hash")
			.setType( String.class );
		
		this.addField("description")
			.setTitle("Description")
			.makeTransient(true)
			.setType( String.class );
	}

	@Override
	public IModelData createRecord() {
		return new Artifact();
	}
	
}
