package com.api.deployer.ui;

import com.api.deployer.ui.components.settings.views.LogConsoleWindow;
import com.api.deployer.ui.connector.DeployAgentConnector;
import com.api.deployer.ui.utils.ILogsWaiter;
import com.redshape.bindings.BeanInfo;
import com.redshape.bindings.types.BindableType;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.AbstractApplication;
import com.redshape.ui.application.ApplicationException;
import com.redshape.ui.application.UnhandledUIException;
import com.redshape.ui.application.events.UIEvents;
import com.redshape.ui.utils.UIConstants;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.windows.swing.ISwingWindowsManager;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;

public class App extends AbstractApplication {
	private static final Logger log = Logger.getLogger( App.class );
	
	static {
		BeanInfo.addTypeMapping( URI.class, BindableType.STRING );
	}

	/**
	 * 	System tray instance ( creates during start )
	 */
	private SystemTray tray;
	
	public App( ApplicationContext context ) throws ApplicationException {
		super( context, new DeployerWindow() );

		this.init();
	}
	
	protected void init() throws ApplicationException {
//		try {
//	       UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
//	   } catch (Exception e) {}
	   
	   super.init();
	}
	
	protected static ApplicationContext loadContext( String contextPath ) {
        File file = new File(contextPath);
        if (file.exists()) {
            return new FileSystemXmlApplicationContext(contextPath);
        } else {
            return new ClassPathXmlApplicationContext(contextPath);
        }
    }
	
	@Override
	public void start() throws ApplicationException {
		App.super.start();
		
		UIRegistry.getContext().getBean(ILogsWaiter.class).start();
		Dispatcher.get().forwardEvent( DeployAgentConnector.Event.Action.Connect );
	}
	
	@Override
	protected void initTrayIcon() {
		if (SystemTray.isSupported()) {
		    this.tray = SystemTray.getSystemTray();
		    Image image = Toolkit.getDefaultToolkit().getImage("tray.gif");

		    PopupMenu popup = new PopupMenu();
		    MenuItem showWindow = new MenuItem("Show window...");
		    showWindow.addActionListener( new ActionListener() {
		    	@Override
		    	public void actionPerformed( ActionEvent e ) {
		    		UIRegistry.getRootContext().setVisible(true);
		    		UIRegistry.getRootContext().toFront();
		    	}
		    } );
		    popup.add( showWindow );
		    
		    MenuItem logsWindow = new MenuItem("Log");
		    showWindow.addActionListener( new ActionListener() {
		    	@Override
		    	public void actionPerformed( ActionEvent e ) {
		    		UIRegistry.<ISwingWindowsManager>getWindowsManager()
		    				  .open( LogConsoleWindow.class );
		    	}
		    } );
		    popup.add( logsWindow );
		    
		    MenuItem defaultItem = new MenuItem("Exit");
		    defaultItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Dispatcher.get().forwardEvent( UIEvents.Core.Exit );
				}
			});
		    popup.add(defaultItem);

		    final TrayIcon trayIcon = new TrayIcon(image, "GridDeploy Console", popup);

		    ActionListener actionListener = new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		            trayIcon.displayMessage("GridDeploy Console", 
		                "Ready. ver.: 1.0:2",
		                TrayIcon.MessageType.INFO);
		        }
		    };
		            
		    trayIcon.setImageAutoSize(true);
		    trayIcon.addActionListener(actionListener);

		    try {
		        tray.add(trayIcon);
		    } catch (AWTException e) {
		    	throw new UnhandledUIException("Error while adding tray icon...", e );
		    }

		} else {
			log.info("System tray supports does not provides...");
		}
	}
	
	public static void main( String[] args ) throws ApplicationException {
		if ( args.length < 1 ) {
			throw new ApplicationException("Spring context not given");
		}
		
		ApplicationContext context = loadContext(args[0]);
		
		UIRegistry.set( UIConstants.System.SPRING_CONTEXT, context );
		
		App app = new App(context);
		app.start();
	}
	
}
