package com.api.deployer.ui.components.workstations;

import com.api.deployer.ui.components.groups.GroupsComponent;
import com.api.deployer.ui.components.monitoring.MonitoringComponent;
import com.api.deployer.ui.components.workstations.config.ConfigController;
import com.redshape.ui.application.events.UIEvents;
import com.redshape.ui.components.AbstractComponent;
import com.redshape.ui.components.actions.ComponentAction;

public class WorkstationComponent extends AbstractComponent {

	public static class Events extends UIEvents {
		
		protected Events( String code ) {
			super(code);
		}

        public static class Action extends Events {

            protected Action( String code ) {
                super(code);
            }

            public static final Action Deploy = new Action("WorkstationComponent.Events.Action.Deploy");

        }
		
		public static class Console extends Events {
			
			protected Console( String code ) {
				super(code);
			}
			
			public static final Console Execute = new Console("WorkstationComponent.Events.Console.Execute");
			
			public static final Console ExecuteResult = new Console("WorkstationComponent.Events.Console.ExecuteResult");
		}
		
		public static class View extends Events {
			protected View( String code ) {
				super(code);
			}
			
			public static class Configuration extends View {
				
				protected Configuration( String code ) {
					super(code);
				}
				
				public static class PartitionsEditor extends Configuration {
					
					protected PartitionsEditor( String code ) {
						super(code);
					}
					
					public static final PartitionsEditor Remove = new PartitionsEditor("WorkstationComponent.Events.View.Configuration.Partitions.Remove");
					public static final PartitionsEditor Add = new PartitionsEditor("WorkstationComponent.Events.View.Configuration.Partitions.Add");
				}

			}

            public final static View Deploy = new View("WorkstationComponent.Events.View.Deploy");
			public final static View Groups = new View("WorkstationComponent.Events.View.Groups");
			public final static View Console = new View("WorkstationComponent.Events.View.Console");
			public final static View Restore = new View("WorkstationComponent.Events.View.Restore");
			public final static View Backup = new View("WorkstationComponent.Events.View.Backup");
			public final static View Config = new View("WorkstationComponent.Events.View.Config");
			public final static View Add = new View("WorkstationComponent.Events.View.Add");
			public final static View List = new View("WorkstationComponent.Events.View.List");
		}
		
		public final static Events Reboot = new Events("WorkstationComponent.Events.Reboot");
		public final static Events Shutdown = new Events("WorkstationComponent.Events.Shutdown");
		public final static Events Exit = new Events("WorkstationComponent.Events.Exit");
	}
	
	public WorkstationComponent() {
		super("workstation", "Workstations");
	}
	
	@Override
	public void init() {
		this.addChild( new GroupsComponent() );
		this.addChild( new MonitoringComponent() );

		this.addController( new WorkstationsController() );
		this.addController( new ConfigController() );

		this.addAction( new ComponentAction("List", this, WorkstationComponent.Events.View.List) );
        this.addAction( new ComponentAction("Deploy", this, WorkstationComponent.Events.View.Deploy ) );
	}
	
}
