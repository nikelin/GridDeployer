package com.api.deployer.jobs.restore;

import com.api.deployer.backup.artifactory.artifacts.ArtifactType;
import com.redshape.bindings.annotations.Bindable;

import java.util.UUID;

/**
 * @author nikelin
 * @date 12/04/11
 * @package com.api.deployer.jobs.restore
 */
public class ArtifactRestoreJob extends AbstractRestoreJob {

	@Bindable( name = "Artifact type" )
	private ArtifactType artifactType;

	public ArtifactRestoreJob() {
		this(null);
	}

	public ArtifactRestoreJob( UUID agentId ) {
		super(agentId);
	}

	public void setArtifactType( ArtifactType type ) {
		this.artifactType = type;
	}

	public ArtifactType getArtifactType() {
		return this.artifactType;
	}

}
