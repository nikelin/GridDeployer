package com.api.deployer.ui.components.artifactory;

import com.api.deployer.ui.components.artifactory.settings.SettingsComponent;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.actions.ComponentAction;
import com.redshape.ui.components.AbstractComponent;
import com.redshape.ui.events.components.ComponentEvents;

public class ArtifactoryComponent extends AbstractComponent {

	public static class Events extends ComponentEvents {

		protected Events(String code) {
			super(code);
		}
		
		public static class Connection extends Events {
			
			protected Connection( String code ) {
				super(code);
			}

			public static final Connection Refresh = new Connection("ArtifactoryComponent.Events.Connection.Refresh");
			public static final Connection Provided = new Connection("ArtifactoryComponent.Events.Connection.Provided");
		}
		
		public static class Artifact extends Events {
			
			protected Artifact( String code ) {
				super(code);
			}
			
			public static Artifact Recovery = new Artifact("ArtifactoryComponent.Events.Artifact.Recovery");
			public static Artifact Archive = new Artifact("ArtifactoryComponent.Events.Artifact.Archive");
			public static Artifact Delete = new Artifact("ArtifactoryComponent.Events.Artifact.Delete");
		}
		
		public static class Views extends Events {
			
			protected Views( String code ) {
				super(code);
			}

			public static Views Version = new Views("ArtifactoryComponent.Events.Views.Version");
			public static Views ArtifactMeta = new Views("ArtifactoryComponent.Events.Views.ArtifactMeta");
			public static Views List = new Views("ArtifactoryComponent.Events.Views.List");
			public static Views Add = new Views("ArtifactoryComponent.Events.Views.Add");

            public static class Settings extends Views {

                public Settings( String code ) {
                    super(code);
                }

                public static Views Application = new Views("ArtifactoryComponent.Events.Views.Settings.Application");
                public static Views Connection = new Views("ArtifactoryComponent.Events.Views.Settings.Connection");
            }

		}
	}
	
	public ArtifactoryComponent() {
		super("artifactory", "Images storage");
	}

	@Override
	public void init() {
        this.addChild( new SettingsComponent() );

		this.addAction( new ComponentAction("Browse", this, ArtifactoryComponent.Events.Views.List ) );
		
		Dispatcher.get().addController( new ArtifactoryController() );
	}
	
}
