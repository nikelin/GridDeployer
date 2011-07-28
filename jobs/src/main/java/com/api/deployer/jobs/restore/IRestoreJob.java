package com.api.deployer.jobs.restore;

import java.util.UUID;

import com.api.deployer.backup.artifactory.artifacts.IArtifact;
import com.api.deployer.io.transport.IDestination;
import com.api.deployer.jobs.IJob;

public interface IRestoreJob extends IJob {
	
	public void setMountingPoint( String path );
	
	public String getMountingPoint();
	
	public void setArtifactId( IArtifact artifact );
	
	public IArtifact getArtifactId();
	
}
