package com.api.deployer.ui.components.help;

import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.events.components.ComponentEvents;
import com.redshape.ui.components.AbstractComponent;
import com.redshape.ui.components.actions.ComponentAction;

public class HelpComponent extends AbstractComponent {
	
	public static class Event extends ComponentEvents {
		
		protected Event( String code ) {
			super(code);
		}
		
		public static class View extends Event {
			
			protected View( String code ) {
				super(code);
			}
			
			public static final View Contents = new View("HelpComponent.Event.View.Contents");
			
			public static final View Update = new View("HelpComponent.Event.View.Update");
			
			public static final View About = new View("HelpComponent.Event.View.About");
			
		}
		
	}
	
	public HelpComponent() {
		this(null, null);
	}
	
	public HelpComponent( String name, String title ) {
		super(name, title);
	}
	
	@Override
	public void init() {
		this.addAction( new ComponentAction("Contents", this, HelpComponent.Event.View.Contents ) );
		this.addAction( new ComponentAction("Update...", this, HelpComponent.Event.View.Update ) );
		this.addAction( new ComponentAction("About", this, HelpComponent.Event.View.About ) );
		
		Dispatcher.get().addController( new HelpController() );
	}
	
}
