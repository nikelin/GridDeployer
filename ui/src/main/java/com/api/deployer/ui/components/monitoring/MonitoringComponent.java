package com.api.deployer.ui.components.monitoring;

import com.redshape.ui.actions.ComponentAction;
import com.redshape.ui.components.AbstractComponent;
import com.redshape.ui.events.components.ComponentEvents;

/**
 * @author nikelin
 * @date 19/04/11
 * @package com.api.deployer.ui.components.monitoring
 */
public class MonitoringComponent extends AbstractComponent {

	public static class Events extends ComponentEvents {

		protected Events( String code ) {
			super(code);
		}

		public static class Views extends Events {

			protected Views( String code ) {
				super(code);
			}

			public static final Views Performance = new Views("MonitoringComponent.Events.Views.Performance");
			public static final Views Status = new Views("MonitoringComponent.Events.Views.Status");
			public static final Views Processes = new Views("MonitoringComponent.Events.Views.Processes");
		}

	}

	public MonitoringComponent() {
		super("monitoring", "Monitoring");
	}

	@Override
	public void init() {
		this.addAction( new ComponentAction("Performance", this, MonitoringComponent.Events.Views.Performance) );
		this.addAction( new ComponentAction("Status & Conditions", this, MonitoringComponent.Events.Views.Status ) );
		this.addAction( new ComponentAction("Processes", this, MonitoringComponent.Events.Views.Processes ) );
	}
}
