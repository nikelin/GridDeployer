package com.api.deployer.ui.components.notifications;

import com.redshape.ui.application.events.components.ComponentEvents;
import com.redshape.ui.components.AbstractComponent;
import com.redshape.ui.components.actions.ComponentAction;

/**
 * @author nikelin
 * @date 19/04/11
 * @package com.api.deployer.ui.components.notifications
 */
public class NotificationsComponent extends AbstractComponent {

    public static class Events extends ComponentEvents {

        protected Events( String code ) {
            super(code);
        }

        public static class Action extends Events {

            protected Action( String code ) {
                super(code);
            }

            public static final Action Received = new Action("NotificationsComponent.Events.Action.Received");

        }

        public static class Views extends Events {

            protected Views( String code ) {
                super(code);
            }

            public static final Views History = new Views("NotificationsComponent.Events.Views.History");
            public static final Views Settings = new Views("NotificationsComponent.Events.Views.Settings");
        }

    }

	public NotificationsComponent() {
		super("notifications", "Notifications");
	}

	@Override
	public void init() {
        this.addController( new NotificationsController() );

		this.addAction( new ComponentAction("History", this, NotificationsComponent.Events.Views.History ) );
		this.addAction( new ComponentAction("Settings", this, NotificationsComponent.Events.Views.Settings ) );
	}
}
