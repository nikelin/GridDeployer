package com.api.deployer.ui.components.connector;

import com.api.commons.events.IEventListener;
import com.api.daemon.DaemonException;
import com.api.daemon.IDaemonEvent;
import com.api.daemon.events.ServiceBindedEvent;
import com.api.deployer.jobs.system.IRebootJob;
import com.api.deployer.jobs.system.IShutdownJob;
import com.api.deployer.jobs.system.RebootJob;
import com.api.deployer.jobs.system.ShutdownJob;
import com.api.deployer.ui.components.connector.windows.BroadcastLocateWindow;
import com.api.deployer.ui.components.connector.windows.ConfigWindow;
import com.api.deployer.ui.components.system.windows.JobProgressMonitor;
import com.api.deployer.ui.components.workstations.WorkstationComponent;
import com.api.deployer.ui.connector.DeployAgentConnector;
import com.api.deployer.ui.data.workstations.Workstation;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.AbstractController;
import com.redshape.ui.application.UnhandledUIException;
import com.redshape.ui.application.annotations.Action;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.application.events.UIEvents;
import com.redshape.ui.utils.UIConstants;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.windows.swing.ISwingWindowsManager;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.UUID;

public class ConnectorController extends AbstractController {
	private static final Logger log = Logger.getLogger( ConnectorController.class );
	
	public ConnectorController() {
		super();
	}
	
	protected void initEvents() {
		this.registerEvent( DeployAgentConnector.Event.Job.Complete );
		this.registerEvent( DeployAgentConnector.Event.Job.Fail );
		this.registerEvent( DeployAgentConnector.Event.Job.Scheduled );
		this.registerEvent( DeployAgentConnector.Event.Job.Cancel );
		this.registerEvent( DeployAgentConnector.Event.Action.Reboot );
		this.registerEvent( DeployAgentConnector.Event.Provided );
		this.registerEvent( DeployAgentConnector.Event.Action.Provided );
		this.registerEvent( DeployAgentConnector.Event.Action.Shutdown );
		this.registerEvent( DeployAgentConnector.Event.Action.Connect );
		this.registerEvent( DeployAgentConnector.Event.Action.Disconnect );
		this.registerEvent( DeployAgentConnector.Event.Action.Reconnect );
	}
	
	protected void initViews() {
		
	}
	
	protected ApplicationContext getContext() {
		return UIRegistry.get( UIConstants.System.SPRING_CONTEXT );
	}
	
	protected DeployAgentConnector getConnector() {
		return this.getContext().getBean( DeployAgentConnector.class );
	}
	
	@Action( eventType = "DeployAgentConnector.Event.Job.Fail" )
	public void failJobaction( AppEvent event ) {
		JOptionPane.showMessageDialog( UIRegistry.getRootContext(), "Job execution failed!" );
	}
	
	@Action( eventType = "DeployAgentConnector.Event.Job.Complete" )
	public void completeJobaction( AppEvent event ) {
		JOptionPane.showMessageDialog( UIRegistry.getRootContext(), "Proceed successfuly!!" );
	}
	
	@Action( eventType = "DeployAgentConnector.Event.Job.Cancel")
	public void cancelJobAction( AppEvent event ) {
		try {
			this.getConnector().cancelJob( event.<UUID>getArg(0) );
		} catch ( RemoteException e  ) {
			throw new UnhandledUIException("Job cancelling failed", e );
		}
	}
	
	@Action( eventType = "DeployAgentConnector.Event.Job.Scheduled")
	public void scheduledAction( AppEvent event ) {
		JobProgressMonitor monitor = new JobProgressMonitor( event.<UUID>getArg(0) );
		monitor.setVisible(true);
	}
	
	@Action( eventType = "DeployAgentConnector.Event.Broadcast")
	public void broadcastSearchAction( AppEvent event ) {
		UIRegistry.<ISwingWindowsManager>getWindowsManager().open( BroadcastLocateWindow.class );
	}
	
	@Action( eventType = "DeployAgentConnector.Event.Action.Reboot" )
	public void rebootAction( AppEvent event ) {
		Workstation subject = event.getArg(0);
		Integer delay = event.getArg(1);
		
		IRebootJob job = new RebootJob(subject.getId());
		job.setDelay( delay );
		
		try {
			this.getConnector().executeJob( job );
		} catch ( RemoteException e ) {
			throw new UnhandledUIException("Job scheduling exception", e );
		}
	}
	
	@Action( eventType = "DeployAgentConnector.Event.Action.Connect" )
	public void connectAction( AppEvent event ) {
		ISwingWindowsManager windowsManager = UIRegistry.getWindowsManager();
		final ConfigWindow window = windowsManager.get( ConfigWindow.class );

		window.onSuccess( new IEventHandler() {
			@Override
			public void handle(AppEvent event) {
				try {
					window.setVisible(false);
					Dispatcher.get().forwardEvent( WorkstationComponent.Events.View.List );
				} catch ( Throwable e ) {
					Dispatcher.get().setErrorState( e.getMessage() );
				}
			}
		});
		
		window.onFail(new IEventHandler() {
			@Override
			public void handle(AppEvent event) {
				log.info("Connection failed");
				
				JOptionPane.showMessageDialog(
					window, 
					"Cannot establish connection with service", 
					"Connection failed", 
					JOptionPane.OK_OPTION | JOptionPane.WARNING_MESSAGE
				);
			}
		});
		
		windowsManager.open(window);
	}
	
	@Action( eventType = "DeployAgentConnector.Event.Reconnect" ) 
	public void reconnectAction( AppEvent event ) {
		// disconnecting from DeployAgent
		try {
			this.getConnector().stop();
		} catch ( DaemonException e  ) {
			Dispatcher.get().forwardEvent( UIEvents.Core.Error, e );
		}
		
		Dispatcher.get().forwardEvent( DeployAgentConnector.Event.Action.Connect );
	}
	
	@Action( eventType = "DeployAgentConnector.Event.Provided" )
	public void connectionProvidedAction( AppEvent event ) {
		DeployAgentConnector connector = this.getConnector();
		connector.setThreadExceptionsHandler( new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				log.info("Exception in thread:" + t.getId() );
				log.error( e.getMessage(), e );
				Dispatcher.get().forwardEvent( DeployAgentConnector.Event.Fail, e.getMessage() );
			}
		});
		
		connector.setHost( event.<String>getArg(0) );
		connector.setPort( event.<Integer>getArg(1) );
		connector.setPath( event.<String>getArg(2) );
		
		connector.addEventListener( IDaemonEvent.class, new IEventListener<IDaemonEvent>() {
			private static final long serialVersionUID = 3311908492484905860L;

			@Override
			public void handleEvent( IDaemonEvent event ) {
				if ( event instanceof ServiceBindedEvent ) {
					Dispatcher.get().forwardEvent( DeployAgentConnector.Event.Connected );
				}
			}
		});
		
		try {
			connector.start();
		} catch ( DaemonException e ) {
			log.error( e.getMessage(), e );
			Dispatcher.get().forwardEvent( DeployAgentConnector.Event.Fail, e.getMessage() );
			return;
		}
	}
	
	@Action( eventType = "DeployAgentConnector.Event.Action.Shutdown" )
	public void shutdownAction( AppEvent event ) {
		Workstation subject = event.getArg(0);
		Integer delay = event.getArg(1);
		
		IShutdownJob job = new ShutdownJob( subject.getId() );
		job.setDelay( delay );
		
		try {
			this.getConnector().executeJob( job );
		} catch ( RemoteException e ) {
			throw new UnhandledUIException("Job scheduling exception", e );
		}
	}
	
}
