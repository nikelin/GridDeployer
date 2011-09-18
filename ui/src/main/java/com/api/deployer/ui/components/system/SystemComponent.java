package com.api.deployer.ui.components.system;

import com.api.deployer.io.transport.URIDestination;
import com.api.deployer.ui.connector.DeployAgentConnector;
import com.api.deployer.ui.connector.NotificationsService;
import com.redshape.daemon.DaemonException;
import com.redshape.daemon.DaemonState;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.application.events.UIEvents;
import com.redshape.ui.application.events.components.ComponentEvents;
import com.redshape.ui.components.AbstractComponent;
import com.redshape.ui.components.actions.ComponentAction;
import com.redshape.ui.data.state.UIStateEvent;
import com.redshape.ui.utils.UIRegistry;
import org.apache.log4j.Logger;

import java.net.URISyntaxException;
import java.rmi.RemoteException;

/**
 *
 * @author nikelin
 */
public class SystemComponent extends AbstractComponent {
	private static final Logger log = Logger.getLogger(SystemComponent.class);

	public static class Events extends ComponentEvents {

		protected Events( String code ) {
			super(code);
		}

        public static class Action extends Events {

            protected Action( String code ) {
                super(code);
            }

            public static final Action ExecuteScript = new Action("SystemComponent.Events.Action.ExecuteScript");

        }

		public static class Views extends Events {

			protected Views( String code ) {
				super(code);
			}

			public static final Views Console = new Views("SystemComponent.Events.Views.Console");
		}

		public static final Events Notification = new Events("SystemComponent.Events.Notification");

	}

	protected NotificationsService getNotificationsService() {
		return UIRegistry.getContext().getBean( NotificationsService.class );
	}

	protected DeployAgentConnector getConnector() {
		return UIRegistry.getContext().getBean( DeployAgentConnector.class );
	}

	protected void onInit() throws DaemonException, URISyntaxException, RemoteException {
		NotificationsService service = this.getNotificationsService();
		if ( service.getState().equals( DaemonState.STARTED ) ) {
			service.start();
		}

		this.getConnector().registerNotifyPoint(
				new URIDestination(
						"console", "", service.getHost(), service.getPort(), "/" + service.getPath() ) );
	}

	@Override
	public void init() {
		Dispatcher.get().addListener( DeployAgentConnector.Event.Connected,
			new IEventHandler() {
				@Override
				public void handle(AppEvent event) {
					try {
						SystemComponent.this.onInit();
					} catch ( Throwable e ) {
						log.error( e.getMessage(), e );
						UIRegistry.getNotificationsManager().error("Unable to start notifications service");
					}
				}
			}
		);

		this.addController( new SystemController() );

		this.addAction( new ComponentAction( "Console", this, SystemComponent.Events.Views.Console ) );
		this.addAction( new ComponentAction( "Restore", this, UIStateEvent.Restore) );
		this.addAction( new ComponentAction( "Backup", this, UIStateEvent.Save ) );
		this.addAction( new ComponentAction( "Exit", this, UIEvents.Core.Exit ) );
	}

}
