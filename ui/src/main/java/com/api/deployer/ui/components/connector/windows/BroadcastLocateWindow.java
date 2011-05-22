package com.api.deployer.ui.components.connector.windows;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.api.deployer.ui.connector.DeployAgentConnector;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.FormPanel;
import com.redshape.ui.actions.InteractionAction;
import com.redshape.ui.events.AppEvent;
import com.redshape.ui.events.IEventHandler;

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