package com.api.deployer.ui.components.connector.windows;

import com.api.deployer.ui.connector.DeployAgentConnector;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.panels.FormPanel;

import javax.swing.*;

public class BroadcastLocateWindow extends JFrame {
	private static final long serialVersionUID = -8695059688202100319L;

	public BroadcastLocateWindow() {
		super();
		
		this.buildUI();
		this.configUI();
		this.initListeners();
	}
	
	protected void initListeners() {
		Dispatcher.get().addListener(
			DeployAgentConnector.Event.Connected,
			new IEventHandler() {
				@Override
				public void handle(AppEvent event) {
					JOptionPane.showMessageDialog( BroadcastLocateWindow.this, "Server connection established!");
					BroadcastLocateWindow.this.setVisible(false);
				}
			}
		);
	}
	
	protected void buildUI() {
		this.setLayout( new BoxLayout( this.getContentPane(), BoxLayout.Y_AXIS ) );
		
		FormPanel panel = new FormPanel();
		panel.addField("port", "Server port", new JTextField() );
		panel.<String>getField("port").setValue( "55457" );
		
		panel.addButton( new JButton(
			new InteractionAction(
				"Search",
				DeployAgentConnector.Event.Action.Discovery
			)
		) );
	}
	
	protected void configUI() {
		this.setTitle("Broadcast discovery");
		this.setSize( 250, 80 );
	}
}