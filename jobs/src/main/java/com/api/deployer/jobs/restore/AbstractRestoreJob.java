package com.api.deployer.jobs.restore;

import com.api.deployer.backup.artifactory.artifacts.IArtifact;
import com.redshape.bindings.annotations.Bindable;
import com.redshape.bindings.annotations.ElementType;
import com.redshape.bindings.types.BindableType;
import com.redshape.daemon.jobs.AbstractJob;

import java.util.UUID;

/**
 * @author nikelin
 * @date 12/04/11
 * @package com.api.deployer.jobs.restore
 */
public abstract class AbstractRestoreJob extends AbstractJob implements IRestoreJob {

	private String mountPoint;
	private IArtifact artifact;

	public AbstractRestoreJob( UUID agentId ) {
		super(agentId);
	}

	@Override
	@Bindable( name = "Local mounting point" )
	public void setMountingPoint(String path) {
		this.mountPoint = path;
	}

	@Override
	public String getMountingPoint() {
		return this.mountPoint;
	}

	@Override
	@Bindable( name = "Artifact", type = BindableType.LIST )
	@ElementType( value = IArtifact.class )
	public void setArtifactId(IArtifact artifact) {
		this.artifact = artifact;
	}

	@Override
	public IArtifact getArtifactId() {
		return this.artifact;
	}

}
