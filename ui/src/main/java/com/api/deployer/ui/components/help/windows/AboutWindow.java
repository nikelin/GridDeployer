package com.api.deployer.ui.components.help.windows;

import javax.swing.*;

import com.redshape.ui.actions.InteractionAction;
import com.redshape.ui.events.AppEvent;
import com.redshape.ui.events.IEventHandler;

import java.awt.*;

public class AboutWindow extends JFrame {
	private static final long serialVersionUID = 311901829335810582L;

	public AboutWindow() {
		super();
		
		this.buildUI();
		this.configUI();
	}
	
	protected void buildUI() {
		this.setLayout( new BoxLayout( this.getContentPane(), BoxLayout.Y_AXIS ) );
		
		JLabel appTitle = new JLabel("API© GridDeploy Console");
		appTitle.setAlignmentX( CENTER_ALIGNMENT );
		this.add( appTitle );
		JLabel description = new JLabel("This is a part of a GridDeploy distribution.");
		description.setBorder( BorderFactory.createEmptyBorder(0, 20, 0, 20) );
		description.setAlignmentX( CENTER_ALIGNMENT );
		this.add(description);
		
		Box box = Box.createHorizontalBox();
		JLabel versionLabel = new JLabel("Version: IIIa");
		versionLabel.setAlignmentX(LEFT_ALIGNMENT);
		box.add( versionLabel );
		this.add(box);
		
		this.add( new JButton(
			new InteractionAction(
				"Close",
				new IEventHandler() {
					@Override
					public void handle(AppEvent event) {
						AboutWindow.this.setVisible(false);
					}
				}
			)
		) );
	}
	
	protected void configUI() {
		this.setTitle("GridDeploy Console v.0.1:2");
		this.setSize( 300, 125 );
	}
	
}
