package com.api.deployer.ui.components.connector.windows;

import java.awt.Dialog.ModalExclusionType;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.api.deployer.ui.connector.DeployAgentConnector;
import org.apache.log4j.Logger;

import com.api.commons.config.ConfigException;
import com.api.commons.config.IConfig;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.FormPanel;
import com.redshape.ui.actions.InteractionAction;
import com.redshape.ui.events.AppEvent;
import com.redshape.ui.events.IEventHandler;
import com.redshape.ui.events.UIEvents;
import com.redshape.ui.utils.UIRegistry;

import com.redshape.validators.NotEmptyValidator;
import com.redshape.validators.NumericStringValidator;


public class ConfigWindow extends JFrame {
	public static String DEFAULT_HOST = "localhost";
	public static String DEFAULT_PORT = "55456";
	public static String DEFAULT_SERVICE = "DeployServer";
	
	private static final Logger log = Logger.getLogger( ConfigWindow.class );
	private static final long serialVersionUID = 2315174085292072382L;

	private FormPanel panel;
	private IEventHandler onSuccessHandler;
	private IEventHandler onFailHandler;
	
	public ConfigWindow() {
		super();
		
		this.init();
		this.buildUI();
		this.configUI();
	}
	
	protected void init() {		
		Dispatcher.get().addListener( DeployAgentConnector.Event.Connected, new IEventHandler() {
			@Override
			public void handle(AppEvent event) {
				if ( ConfigWindow.this.onSuccessHandler != null ) {
					ConfigWindow.this.onSuccessHandler.handle(event);
				}
			}
		});
		
		Dispatcher.get().addListener( DeployAgentConnector.Event.Fail, new IEventHandler() {
			@Override
			public void handle(AppEvent event) {
				log.info("Connection failed...");
				ConfigWindow.this.panel.markDataInvalid();
				
				if ( ConfigWindow.this.onFailHandler != null ) {
					ConfigWindow.this.onFailHandler.handle(event);
				}
				
				Dispatcher.get().forwardEvent( new AppEvent( UIEvents.Core.Error, "Remote server unreachable!" ) );
			}
		});
	}
	
	protected IConfig getConfig() {
		return UIRegistry.getContext().getBean("nativeConfig", IConfig.class );
	}
	
	public void onSuccess( IEventHandler handler ) {
		this.onSuccessHandler = handler;
	}
	
	public void onFail( IEventHandler handler ) {
		this.onFailHandler = handler;
	}
	
	protected void buildUI() {
		this.setLayout( new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS ) );
		
		JLabel titleLabel = new JLabel("Connector settings");
		titleLabel.setHorizontalAlignment( SwingConstants.LEFT );
		this.add( titleLabel );
		
		String host = DEFAULT_HOST;
		String port = DEFAULT_PORT;
		String service = DEFAULT_SERVICE;
		try {
			IConfig serverDefaults = this.getConfig().get("defaults").get("server");
			host = serverDefaults.get("host").value();
			port = serverDefaults.get("port").value();
			service = serverDefaults.get("service").value();
		} catch ( ConfigException e ) {}
		
		this.panel = new FormPanel();
		this.panel.addField("host", "Host", new  JTextField() );
		this.panel.<String>getField("host").setValidator( new NotEmptyValidator() );
		this.panel.<String>getField("host").setValue( host );
		this.panel.addField("port", "Port", new JTextField() );
		this.panel.<String>getField("port").setValidator( new NumericStringValidator() );
		this.panel.<String>getField("port").setValue( port );
		this.panel.addField("service", "Service name", new JTextField() );
		this.panel.<String>getField("service").setValidator( new NotEmptyValidator() );
		this.panel.<String>getField("service").setValue( service );
		
		this.panel.addButton( new JButton(
			new InteractionAction(
				"Connect", 
				new IEventHandler() {
					@Override
					public void handle(AppEvent event) {
						log.info("Connection requested...");
						
						if ( ConfigWindow.this.panel.isDataValid() ) {
							log.info("Connection credentials validated...");
							Dispatcher.get().forwardEvent( 
								DeployAgentConnector.Event.Provided,
								new Object[] { 
									ConfigWindow.this.panel.<String>getField("host").getValue(), 
									Integer.valueOf( 
										ConfigWindow.this.panel.<String>getField("port").getValue() 
									), 
									ConfigWindow.this.panel.<String>getField("service").getValue() 
								}
							);
						}
					}
				}
			) 
		) );	
		
		JButton button = new JButton(new InteractionAction("Search", DeployAgentConnector.Event.Broadcast ) );
		button.setEnabled(false);
		
		this.panel.addButton( button );
		this.panel.addButton( new JButton(new InteractionAction("Cancel", UIEvents.Core.Exit ) ) );
		this.add(this.panel);
	}
	
	protected void configUI() {
		this.setModalExclusionType( ModalExclusionType.NO_EXCLUDE );
		this.setAlwaysOnTop(true);
		this.setTitle("API Deployer Console");
		this.setSize( 350, 150 );
		
		this.addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				Dispatcher.get().forwardEvent( UIEvents.Core.Exit );
			}
		});
	}
	
}
