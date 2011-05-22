package com.api.deployer.ui.data.deploy;

import com.api.deployer.ui.components.artifactory.ArtifactoryComponent.Events.Artifact;
import com.api.deployer.ui.data.BackupType;
import com.redshape.ui.data.AbstractModelData;

public class DeployDescriptor extends AbstractModelData {

	public DeployDescriptor() {
		super();
	}
	
	public Object getSubject() {
		return this.get( DeployDescriptorModel.SUBJECT );
	}
	
	public Artifact getArtifact() {
		return this.get( DeployDescriptorModel.ARTIFACT );
	}
	
	public BackupType getType() {
		return this.get( DeployDescriptorModel.TYPE );
	}
	
}
