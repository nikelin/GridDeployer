package com.api.deployer.ui.components.artifactory;

import com.api.deployer.ui.components.artifactory.data.Artifact;
import com.api.deployer.ui.components.artifactory.views.ListView;
import com.api.deployer.ui.components.artifactory.views.windows.ListWindow;
import com.api.deployer.ui.components.artifactory.views.windows.settings.ApplicationWindow;
import com.api.deployer.ui.components.artifactory.views.windows.settings.ConnectionWindow;
import com.api.deployer.ui.connector.ArtifactoryConnector;


import com.api.deployer.ui.connector.DeployAgentConnector;

import com.redshape.ui.AbstractController;
import com.redshape.ui.UnhandledUIException;
import com.redshape.ui.annotations.Action;
import com.redshape.ui.events.AppEvent;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.views.ViewException;
import com.redshape.ui.windows.swing.ISwingWindowsManager;

import java.rmi.RemoteException;

public class ArtifactoryController extends AbstractController {
	public static final String ListView = "ArtifactoryController.ListView";
	
	public ArtifactoryController() {
		super();
	}
	
	@Override
	protected void initEvents() {
		this.registerEvent( ArtifactoryComponent.Events.Connection.Provided );
		this.registerEvent( ArtifactoryComponent.Events.Views.Add );
		this.registerEvent( ArtifactoryComponent.Events.Views.List );

        this.registerEvent( ArtifactoryComponent.Events.Views.Settings.Application );
		this.registerEvent( ArtifactoryComponent.Events.Views.Settings.Connection );
		
		this.registerEvent( ArtifactoryComponent.Events.Artifact.Delete );
		this.registerEvent( ArtifactoryComponent.Events.Artifact.Archive );
		this.registerEvent(ArtifactoryComponent.Events.Artifact.Recovery);
	}
	
	@Override
	protected void initViews() {
		UIRegistry.getViewsManager().register( new ListView(), ArtifactoryController.ListView );
	}

	protected DeployAgentConnector getDeployAgentConnector() {
		return UIRegistry.getContext().getBean( DeployAgentConnector.class );
	}

	protected ArtifactoryConnector getArtifactoryConnector() {
		return UIRegistry.getContext().getBean( ArtifactoryConnector.class );
	}

	@Action( eventType = "ArtifactoryComponent.Events.Artifact.Delete")
	public void artifactDeleteAction( AppEvent event ) {
		try {
			this.getArtifactoryConnector().removeArtifact( event.<Artifact>getArg(0).getId() );
		} catch ( RemoteException e ) {
			throw new UnhandledUIException( e.getMessage(), e );
		}
	}
	
	@Action( eventType = "ArtifactoryComponent.Events.Views.Settings.Connection" )
	public void connectionSettingsView( AppEvent event ) throws ViewException {
		UIRegistry.<ISwingWindowsManager>getWindowsManager().open( ConnectionWindow.class );
	}

    @Action( eventType = "ArtifactoryComponent.Events.Views.Settings.Application" )
	public void applicationSettingsView( AppEvent event ) throws ViewException {
		UIRegistry.<ISwingWindowsManager>getWindowsManager().open( ApplicationWindow.class );
	}
	
	@Action( eventType = "ArtifactoryComponent.Events.Views.List" )
	public void listView( AppEvent event ) throws ViewException {
		UIRegistry.<ISwingWindowsManager>getWindowsManager().open(ListWindow.class);
	}

	@Action( eventType = "ArtifactoryComponent.Events.Connection.Provided" )
	public void connectionDataProvided( AppEvent event ) {
		ArtifactoryConnector connector = getArtifactoryConnector();

		connector.setHost( event.<String>getArg(0) );
		connector.setPort( event.<Integer>getArg(1) );
		connector.setPath( event.<String>getArg(2) );

		try {
			connector.start();

			if ( this.getDeployAgentConnector().isConnected() ) {
				this.getDeployAgentConnector().setArtifactoryURI( connector.getURI() );
			}
		} catch ( Throwable e ) {
			throw new UnhandledUIException("Artifactory connector exception", e );
		}
	}

	
}
