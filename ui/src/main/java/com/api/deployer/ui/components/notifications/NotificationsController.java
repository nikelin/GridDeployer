package com.api.deployer.ui.components.notifications;

import com.api.deployer.notifications.INotification;
import com.api.deployer.ui.components.notifications.windows.HistoryWindow;
import com.api.deployer.ui.components.notifications.windows.SettingsWindow;
import com.api.deployer.ui.connector.DeployAgentConnector;
import com.api.deployer.ui.connector.NotificationsService;
import com.api.deployer.ui.data.notifications.Notification;
import com.api.deployer.ui.data.notifications.NotificationsStore;
import com.redshape.daemon.DaemonException;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.AbstractController;
import com.redshape.ui.application.annotations.Action;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.application.notifications.INotificationsManager;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.windows.swing.ISwingWindowsManager;
import com.redshape.utils.config.ConfigException;
import com.redshape.utils.config.IConfig;

import java.awt.*;

/**
 * @author nikelin
 * @date 25/04/11
 * @package com.api.deployer.ui.components.notifications
 */
public class NotificationsController extends AbstractController {

    @Override
    protected void initEvents() {
        Dispatcher.get().addListener(
            DeployAgentConnector.Event.Connected,
            new IEventHandler() {
                @Override
                public void handle(AppEvent event) {
                    try {
                        UIRegistry.getContext().getBean(NotificationsService.class).start();
                    } catch (DaemonException e) {
                        UIRegistry.getNotificationsManager().error("Unable to start notifications service!");
                    }
                }
            }
        );

        this.registerEvent( NotificationsComponent.Events.Action.Received );
        this.registerEvent( NotificationsComponent.Events.Views.History );
        this.registerEvent( NotificationsComponent.Events.Views.Settings );
    }

    @Override
    protected void initViews() {
    }

    protected String getSetting( String name ) throws ConfigException {
        return UIRegistry.getSettings().get("notifications").attribute(name);
    }

    protected boolean hasSetting( String name ) throws ConfigException {
        IConfig config = UIRegistry.getSettings().get("notifications");
        if ( config == null ) {
            return false;
        }

        return config.attribute(name) != null && !config.attribute(name).trim().isEmpty();
    }

    @Action( eventType = "NotificationsComponent.Events.Action.Received" )
    public void receivedAction( AppEvent event ) throws ConfigException,
                                                        InstantiationException {
        if ( this.hasSetting("enable") && !Boolean.valueOf( this.getSetting("enable") ) ) {
            return;
        }

        INotification notification = event.getArg(0);

        this.showNotification( notification );
        this.storeNotification( notification );
    }

    protected void storeNotification( INotification notification )
        throws InstantiationException {
        Notification notificationData = new Notification();
        notificationData.setDate( notification.getDate() );
        notificationData.setMessage( notification.getMessage() );
        notificationData.setSubject( notification.getSubject() );
        notificationData.setType( notification.getType() );

        UIRegistry.getStoresManager()
                  .getStore(NotificationsStore.class)
                  .add(notificationData);
    }

    protected void showNotification( INotification notification ) throws ConfigException {
        INotificationsManager notifier = UIRegistry.getNotificationsManager();

        if ( this.hasSetting("level") ) {
            Integer level = Integer.valueOf(this.getSetting("level"));
            if ( notification.getType().level() < level ) {
                return;
            }
        }

        switch ( notification.getType() ) {
            case INFO:
                notifier.info( notification.getMessage() );
            break;
            case ERROR:
                int i = 0;
                do {
                    Toolkit.getDefaultToolkit().beep();
                    try {
                        Thread.sleep(1000);
                    } catch ( InterruptedException e ) {}
                } while ( i < 5 );

                notifier.error( notification.getMessage() );
            break;
            case DEBUG:
                notifier.info( notification.getMessage() );
            break;
            case WARNING:
                notifier.warn( notification.getMessage() );
            break;
            default:
                notifier.info( notification.getMessage() );
        }
    }

    @Action( eventType = "NotificationsComponent.Events.Views.History")
    public void historyView( AppEvent event ) {
        UIRegistry.<ISwingWindowsManager>getWindowsManager().open( HistoryWindow.class );
    }

    @Action( eventType = "NotificationsComponent.Events.Views.Settings")
    public void settingsView( AppEvent event ) {
        UIRegistry.<ISwingWindowsManager>getWindowsManager().open( SettingsWindow.class );
    }

}
