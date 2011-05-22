package com.api.deployer.ui.components.artifactory.data.loaders;

import com.api.deployer.backup.artifactory.artifacts.IArtifact;
import com.api.deployer.ui.components.artifactory.data.Artifact;
import com.api.deployer.ui.connector.ArtifactoryConnector;
import com.redshape.ui.data.loaders.AbstractDataLoader;
import com.redshape.ui.data.loaders.LoaderException;
import com.redshape.ui.utils.UIRegistry;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

public class ArtifactsLoader extends AbstractDataLoader<Artifact> {

	protected ArtifactoryConnector getConnector() {
		return UIRegistry.getContext().getBean( ArtifactoryConnector.class );
	}

	@Override
	protected Collection<Artifact> doLoad() throws LoaderException {
		try {
			if ( !this.getConnector().isConnected() ) {
				return new ArrayList<Artifact>();
			}

			Collection<Artifact> data = new HashSet<Artifact>();
			for ( IArtifact artifact : this.getConnector().getList() ) {
				Artifact artifactData = new Artifact();

				if ( artifact.getTimestamp() != null ) {
					artifactData.setDate( new Date( artifact.getTimestamp() ) );
				}

				artifactData.setRelatedObject(artifact);
				artifactData.setId( artifact.getId() );
				artifactData.setDescription( artifact.getDescription() );
				artifactData.setName( artifact.getName() );
				artifactData.setType( artifact.getType() );

				data.add( artifactData );
			}

			return data;
		} catch ( RemoteException e ) {
			throw new LoaderException( e.getMessage(), e );
		}
	}
}
