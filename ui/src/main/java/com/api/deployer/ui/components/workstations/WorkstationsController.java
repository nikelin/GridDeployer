package com.api.deployer.ui.components.workstations;

import com.api.deployer.jobs.activation.JobActivationProfile;
import com.api.deployer.jobs.deploy.AgentSetupJob;
import com.api.deployer.ui.components.workstations.views.ConsoleView;
import com.api.deployer.ui.components.workstations.views.ListView;
import com.api.deployer.ui.components.workstations.windows.DeployWindow;
import com.api.deployer.ui.connector.DeployAgentConnector;
import com.api.deployer.ui.data.workstations.Workstation;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.AbstractController;
import com.redshape.ui.application.UnhandledUIException;
import com.redshape.ui.application.annotations.Action;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.EventType;
import com.redshape.ui.application.events.UIEvents;
import com.redshape.ui.application.notifications.INotificationsManager;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.views.ViewException;
import com.redshape.ui.windows.swing.ISwingWindowsManager;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkstationsController extends AbstractController {
	private static final Logger log = Logger.getLogger( WorkstationsController.class );
	
	public static final String ConsoleView = "WorkstationsController.ConsoleView";
	public static final String ListView = "WorkstationsController.ListView";

	@Override
	protected void initEvents() {
		this.registerEvent( WorkstationComponent.Events.View.List );
		this.registerEvent( WorkstationComponent.Events.View.Add );
		this.registerEvent( WorkstationComponent.Events.View.Groups );
		this.registerEvent( WorkstationComponent.Events.View.Backup );
		this.registerEvent( WorkstationComponent.Events.View.Console );
        this.registerEvent( WorkstationComponent.Events.View.Deploy );

        this.registerEvent( WorkstationComponent.Events.Action.Deploy );
		
		this.registerEvent( WorkstationComponent.Events.Console.Execute );
		this.registerEvent( WorkstationComponent.Events.Reboot );
		this.registerEvent( WorkstationComponent.Events.Shutdown );
		this.registerEvent( WorkstationComponent.Events.Exit );
	}

	@Override
	protected void initViews() {
		UIRegistry.getViewsManager().register( new ListView(), ListView );
	}
	
	protected DeployAgentConnector getConnector() {
		return this.getContext().getBean( DeployAgentConnector.class );
	}

    @Action( eventType = "WorkstationComponent.Events.Action.Deploy")
    public void deployAction( AppEvent event ) {
        INotificationsManager notifier = UIRegistry.getNotificationsManager();

        if ( this.getConnector() == null || !this.getConnector().isConnected() ) {
            notifier.error("Connection with scheduler is not established!");
            return;
        }

        AgentSetupJob job = event.getArg(0);
        if ( job == null ) {
            notifier.error("Workstation setup error!");
            return;
        }

        JobActivationProfile profile = null;
        if ( event.getArgs().length > 1 ) {
            profile = event.getArg(1);
        }

        try {
            if ( profile == null ) {
                this.getConnector().executeJob( job );
                notifier.info("Workstation setup being processing!");
            } else {
                this.getConnector().scheduleJob( job, profile );
                notifier.info("Workstation setup successfuly scheduled!");
            }
        } catch ( RemoteException e ) {
            throw new UnhandledUIException("Network interaction exception", e );
        }
    }

    @Action( eventType = "WorkstationComponent.Events.View.Deploy" )
    public void deployView( AppEvent event ) {
        UIRegistry.<ISwingWindowsManager>getWindowsManager().open(DeployWindow.class);
    }

	@Action( eventType = "WorkstationComponent.Events.Console.Execute")
	public void consoleExecuteAction( AppEvent event ) {
		Workstation station = event.getArg(0);
		String command = event.getArg(1);
		
		String response = null;
		try {
			response = this.getConnector().executeScript( station.getId(), command );
		} catch ( RemoteException e  ) {
			response = e.getMessage();
			log.error( e.getMessage(), e );
		}
		
		Dispatcher.get().forwardEvent( WorkstationComponent.Events.Console.ExecuteResult, response );
	}
	
	@Action( eventType = "WorkstationComponent.Events.View.Console")
	public void consoleView( AppEvent event ) throws ViewException {
		UIRegistry.getViewsManager().register( new ConsoleView( event.<Workstation>getArg(0) ), ConsoleView );
		UIRegistry.getViewsManager().activate( ConsoleView );
	}
	
	@Action( eventType = "WorkstationComponent.Events.Reboot")
	public void rebootAction( AppEvent event ) {
		this.showDelayConfigurationDialog("Do you really want to reboot this station?", 
				  "Reboot action" , 
				  "Enter time value what executor will must be await before reboot action proceed", 
				  DeployAgentConnector.Event.Action.Reboot,
				  event.getArgs() );
	}
	
	@Action( eventType = "WorkstationComponent.Events.Shutdown" )
	public void shutdownAction( AppEvent event ) {
		this.showDelayConfigurationDialog("Do you really want to shutdown this station?", 
				  "Shutdown action" , 
				  "Enter time value what executor will must be await before shutdown action proceed", 
				  DeployAgentConnector.Event.Action.Reboot,
				  event.getArgs() );	
	}
	
	@Action( eventType = "WorkstationComponent.Events.View.Backup" )
	public void backupView( AppEvent event ) {
		// TODO
	}
	
	@Action( eventType = "WorkstationComponent.Events.View.List" )
	public void listView( AppEvent event ) throws ViewException {
		UIRegistry.getViewsManager().activate( ListView );
	}
	
	@Action( eventType = "WorkstationComponent.Events.Exit" )
	public void exitAction( AppEvent event ) {
		System.out.println("Exit requested");
		Dispatcher.get().forwardEvent(UIEvents.Core.Exit);
	}
	
	private void showDelayConfigurationDialog( String confirmMessage,
                           String dialogTitle, String message, EventType event, Object... args ) {
		JFrame parent = UIRegistry.getRootContext();
		if ( JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog( parent, confirmMessage) ) {
			boolean repeat = false;
			do {
				try {
					Integer value;
					String inputData = JOptionPane.showInputDialog(parent, message, dialogTitle, JOptionPane.QUESTION_MESSAGE );
					if ( inputData.isEmpty() ) {
						value = 0;
					} else {
						value = Integer.valueOf( inputData );
					}
					
					
					List<Object> inArgs = new ArrayList<Object>( args.length + 1 );
					inArgs.addAll( Arrays.asList( args ) );
					inArgs.add( value );
					
					Dispatcher.get().forwardEvent( 
						new AppEvent( 
							DeployAgentConnector.Event.Action.Reboot, 
							inArgs.toArray( new Object[ inArgs.size() ]) 
						) 
					);
					repeat = false;
				} catch ( NumberFormatException e ) {
					JOptionPane.showMessageDialog(parent, "Wrong number entered (0 < number)");
					repeat = true;
				}
			} while ( repeat == true );
		}
	}
	
}
