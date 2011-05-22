package com.api.deployer.ui.components.artifactory.views.windows.settings;
 
import java.awt.Component;
import javax.swing.*;

import com.api.deployer.ui.components.artifactory.ArtifactoryComponent;

import com.api.deployer.ui.connector.ArtifactoryConnector;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.UnhandledUIException;
import com.redshape.ui.events.UIEvents;
import com.redshape.ui.events.handlers.WindowCloseHandler;

import com.redshape.ui.utils.Settings;
import com.redshape.validators.NotEmptyValidator;
import com.redshape.utils.config.ConfigException;
import com.redshape.utils.config.IConfig;
import com.redshape.utils.config.IWritableConfig;
import org.springframework.context.ApplicationContext;

import com.api.deployer.ui.connector.DeployAgentConnector;

import com.redshape.ui.FormPanel;
import com.redshape.ui.actions.InteractionAction;
import com.redshape.ui.events.AppEvent;
import com.redshape.ui.events.IEventHandler;
import com.redshape.ui.utils.UIConstants;
import com.redshape.ui.utils.UIRegistry;

public class ConnectionWindow extends JFrame {
	private static final long serialVersionUID = 1988314470240709182L;

	private FormPanel credentialsForm;
	private FormPanel connectionForm;
	
	public ConnectionWindow() throws ConfigException {
		super();

		this.init();
		this.buildUI();
		this.configUI();
	}

	protected void init() throws ConfigException {
		Dispatcher.get().addListener(
			UIEvents.Core.Refresh.Settings,
			new IEventHandler() {
				@Override
				public void handle(AppEvent event) {
					ConnectionWindow.this.updateSettings();
				}
			}
		);

		Dispatcher.get().addListener(
			ArtifactoryConnector.Event.Failed,
			new IEventHandler() {
				@Override
				public void handle(AppEvent event) {
					JOptionPane.showMessageDialog(
							ConnectionWindow.this,
							"Artifactory service not accessible or wrong connection data provided.",
							"Error!",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		);

		Dispatcher.get().addListener(
			ArtifactoryConnector.Event.Connected,
			new IEventHandler() {
				@Override
				public void handle(AppEvent event) {
					JOptionPane.showMessageDialog( ConnectionWindow.this, "Connection established!" );
				}
			}
		);
	}

	protected void saveSettings() {
		try {
			final Settings settings = UIRegistry.getSettings();
			IWritableConfig artifactoryNode = (IWritableConfig) settings.createChild("artifactory");
			artifactoryNode.createChild("host").set( this.connectionForm.<String>getField("host").getValue() );
			artifactoryNode.createChild("service").set( this.connectionForm.<String>getField("service").getValue() );
			artifactoryNode.createChild("port").set( this.connectionForm.<String>getField("port").getValue() );
			UIRegistry.setSettings(settings);
		} catch ( ConfigException e ) {
			throw new UnhandledUIException( e.getMessage(), e );
		}
	}

	protected void updateSettings() {
		try {
			IConfig artifactoryNode = UIRegistry.getSettings().get("artifactory");
			if ( artifactoryNode == null ) {
				return;
			}

			this.connectionForm.<String>getField("host").setValue(
				artifactoryNode.get("host").value()
			);

			this.connectionForm.<String>getField("port").setValue(
				artifactoryNode.get("port").value()
			);

			this.connectionForm.<String>getField("service").setValue(
				artifactoryNode.get("service").value()
			);

			Dispatcher.get().forwardEvent( UIEvents.Core.Repaint, this.connectionForm );
		} catch ( ConfigException e ) {
			throw new UnhandledUIException( e.getMessage(), e );
		}
	}
	
	protected void configUI() {
		this.setTitle("Artifactory settings");
		this.setSize( 600, 180 );
	}
	
	protected ApplicationContext getContext() {
		return UIRegistry.get( UIConstants.System.SPRING_CONTEXT );
	}
	
	public DeployAgentConnector getConnector() {
		return this.getContext().getBean( DeployAgentConnector.class );
	}
	
	protected void buildUI() throws ConfigException {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS ) );
		
		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("Network address", this.createAddressPanel() );
		tabs.addTab("Credentials", this.createCredentialsPanel() );
		panel.add( tabs );

		panel.add( this.createButtons() );
		
		this.add(panel);

		this.updateSettings();
	}
	
	protected JLayeredPane createButtons() {
		JLayeredPane pane = new JLayeredPane();
		pane.setLayout( new BoxLayout( pane, BoxLayout.X_AXIS ) );

		pane.add(new JButton(
			new InteractionAction( "Save", new IEventHandler() {
				@Override
				public void handle(AppEvent event) {
					final FormPanel form = ConnectionWindow.this.connectionForm;

					if ( form.isDataValid() ) {
						ConnectionWindow.this.saveSettings();

						Dispatcher.get().forwardEvent(
							ArtifactoryComponent.Events.Connection.Provided,
							form.<String>getField("host").getValue(),
							Integer.valueOf(
								form.<String>getField("port").getValue()
							),
							form.<String>getField("service").getValue()
						);
					}
				}
			} )
		) );
		
		pane.add( new JButton(
			new InteractionAction( "Cancel", new WindowCloseHandler(this) )
		) );
		
		return pane;
	}
	
	protected Component createAddressPanel() throws ConfigException {
		this.connectionForm = new FormPanel();
		this.connectionForm.<String>addField("host", "Host", new JTextField())
						   .setValidator(new NotEmptyValidator());
		this.connectionForm.<String>addField("port", "Port", new JTextField())
						   .setValidator(new NotEmptyValidator());
		this.connectionForm.<String>addField("service", "Service", new JTextField())
						   .setValidator(new NotEmptyValidator());
		
		return this.connectionForm;
	}

	protected Component createCredentialsPanel() {
		this.credentialsForm = new FormPanel();
		this.credentialsForm.addField("user", "Login", new JTextField());
		this.credentialsForm.addField("password", "Password", new JPasswordField() );

		return this.credentialsForm;
	}
	
}
