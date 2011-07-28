package com.api.deployer.ui.components.scheduler;

import com.redshape.ui.application.events.components.ComponentEvents;
import com.redshape.ui.components.AbstractComponent;
import com.redshape.ui.components.actions.ComponentAction;

public class SchedulerComponent extends AbstractComponent {
	public static class Events extends ComponentEvents {
		
		protected Events( String code ) {
			super(code);
		}

		public static class Action extends Events {

			protected Action( String code ) {
				super(code);
			}

			public static final Action ProcessJobs = new Action("SchedulerComponent.Events.Action.ProcessJobs");
		}

		public static class Views extends Events {
			
			protected Views( String code ) {
				super(code);
			}

			public static class Job extends Views {

				protected Job( String code ) {
					super(code);
				}

                public static final Job Details = new Job("SchedulerComponent.Events.Views.Job.Details");
				public static final Job New = new Job("SchedulerComponent.Events.Views.Job.New");
				public static final Job Added = new Job("SchedulerComponent.Events.Views.Job.Added");
			}

            public static final Views Configurations = new Views("SchedulerComponent.Events.Views.Configurations");
			public static final Views Jobs = new Views("SchedulerComponent.Events.Views.Jobs");
			public static final Views Scheduled = new Views("SchedulerComponent.Events.Views.Scheduled");
		}
		
	}
	
	public SchedulerComponent() {
		this(null, null);
	}
	
	public SchedulerComponent( String name, String title ) {
		super(name, title);
	}
	
	@Override
	public void init() {
		this.addController( new SchedulingController() );

		this.addAction( new ComponentAction("Jobs", this, SchedulerComponent.Events.Views.Jobs ) );
		this.addAction( new ComponentAction("Scheduled", this, SchedulerComponent.Events.Views.Scheduled ) );
        this.addAction( new ComponentAction("Configurations", this, SchedulerComponent.Events.Views.Configurations ) );
	}
	
}
