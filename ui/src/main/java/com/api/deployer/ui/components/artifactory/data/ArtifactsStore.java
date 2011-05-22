package com.api.deployer.ui.components.artifactory.data;

import com.api.deployer.ui.components.artifactory.data.loaders.ArtifactsLoader;
import com.redshape.ui.data.loaders.IDataLoader;
import com.redshape.ui.data.loaders.policies.RefreshPolicy;
import com.redshape.ui.data.stores.ListStore;

public class ArtifactsStore extends ListStore<Artifact> {
	
	public ArtifactsStore() {
		this( new ArtifactsLoader() );
	}
	
	public ArtifactsStore( IDataLoader<Artifact> loader ) {
		super( new ArtifactModel(), new RefreshPolicy( loader, 5000 ) );
	}

}
