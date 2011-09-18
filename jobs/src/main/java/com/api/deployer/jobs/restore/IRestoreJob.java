package com.api.deployer.jobs.restore;

import com.api.deployer.backup.artifactory.artifacts.IArtifact;
import com.redshape.daemon.jobs.IJob;

public interface IRestoreJob extends IJob {
	
	public void setMountingPoint( String path );
	
	public String getMountingPoint();
	
	public void setArtifactId( IArtifact artifact );
	
	public IArtifact getArtifactId();
	
}
