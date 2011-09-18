package com.api.deployer.ui.components.software;

import com.redshape.ui.Dispatcher;

import com.redshape.ui.application.events.components.ComponentEvents;
import com.redshape.ui.components.AbstractComponent;
import com.redshape.ui.components.actions.ComponentAction;

public class SoftwareComponent extends AbstractComponent {

	public static class Events extends ComponentEvents {
		
		protected Events( String code ) {
			super(code);
		}
		
		public static class Action extends Events {
			
			protected Action( String code ) {
				super(code);
			}

		}
		
		public static class Views extends Events {
			
			protected Views( String code ) {
				super(code);
			}

            public static final Views Channels = new Views("SoftwareComponent.Events.Views.Channels");
            public static final Views Overview = new Views("SoftwareComponent.Events.Views.Overview");
            public static final Views Settings = new Views("SoftwareComponent.Events.Views.Settings");
		}
		
	}

	public SoftwareComponent() {
		this(null, null);
	}
	
	public SoftwareComponent(String name, String title) {
		super(name, title);
	}
	
	public void init() {
		Dispatcher.get().addController( new SoftwareController() );

        this.addAction( new ComponentAction("Channels", this, SoftwareComponent.Events.Views.Channels ) );
        this.addAction( new ComponentAction("Overview", this, SoftwareComponent.Events.Views.Overview ) );
        this.addAction( new ComponentAction("Settings", this, SoftwareComponent.Events.Views.Settings ) );
	}
	
}
