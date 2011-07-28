package com.api.deployer.ui.components.settings;

import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.events.components.ComponentEvents;
import com.redshape.ui.components.AbstractComponent;
import com.redshape.ui.components.actions.ComponentAction;

public class SettingsComponent extends AbstractComponent {
	
	public static class Events extends ComponentEvents {
		
		protected Events( String code ) {
			super(code);
		}
		
		public static class Views extends Events {
			
			protected Views( String code ) {
				super(code);
			}
			
			public static final Views LogsConsole = new Views("SettingsComponent.Events.Views.LogsConsole");
		}
		
	}
	
	public SettingsComponent() {
		this(null, null);
	}
	
	public SettingsComponent( String name, String title ) {
		super(name, title);
	}
	
	@Override
	public void init() {
		this.addAction( new ComponentAction("Activity log", this, Events.Views.LogsConsole ) );
		
		Dispatcher.get().addController( new SettingsController() );
	}
	
}
