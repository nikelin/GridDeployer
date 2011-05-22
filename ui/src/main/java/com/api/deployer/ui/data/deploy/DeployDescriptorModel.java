package com.api.deployer.ui.data.deploy;

import com.api.deployer.ui.components.artifactory.data.Artifact;
import com.api.deployer.ui.data.BackupType;
import com.redshape.ui.data.AbstractModelType;

public class DeployDescriptorModel extends AbstractModelType {
	public static final String SUBJECT = "subject";
	public static final String ARTIFACT = "artifact";
	public static final String TYPE = "type";
	
	public DeployDescriptorModel() {
		this.addField( SUBJECT )
			.setTitle("Subject")
			.setType( Object.class );
		
		this.addField( ARTIFACT )
			.setTitle("Artifact")
			.setType( Artifact.class );
		
		this.addField( TYPE )
			.setTitle("Deploy type")
			.setType( BackupType.class );
	}
	
	public DeployDescriptor createRecord() {
		return new DeployDescriptor();
	}

}
