package com.api.deployer.ui.components.artifactory.views.panels;

import com.api.deployer.backup.artifactory.artifacts.ArtifactType;
import com.api.deployer.ui.components.artifactory.ArtifactoryComponent;
import com.api.deployer.ui.components.artifactory.data.Artifact;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.panels.FormPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.HashSet;

public class ArtifactDetailsPanel extends JPanel {
	private static final long serialVersionUID = 230043736417829011L;
	
	private Artifact record;
	
	/****
	 *	Context dependent panels
	 */
	private FormPanel currentContext;
	private FormPanel driveContextPanel;
	private FormPanel systemContextPanel;
	private FormPanel settingsContextPanel;
	private FormPanel partitionContextPanel;
	
	private Collection<FormPanel> contextPanels = new HashSet<FormPanel>();
	
	public ArtifactDetailsPanel() {
		this(null);
	}
	
	public ArtifactDetailsPanel( Artifact artifact ) {
		super();
		
		this.record = artifact;
		
		this.buildUI();
		this.configUI();
	}
	
	public void setRecord( Artifact record ) {
		this.record = record;
		this.switchContext(record);
	}
	
	protected void switchContext( Artifact record ) {
		for ( FormPanel panel : this.contextPanels ) {
			panel.setVisible(false);
		}
		
		ArtifactType type = record.getType();
		if ( type == null ) {
			type = ArtifactType.DRIVE;
		}
		
		if ( this.currentContext != null ) {
			this.currentContext.setVisible(false);
			this.currentContext.repaint();
		}
		
		switch ( type ) {
		case DRIVE:
			this.currentContext = this.driveContextPanel;
			this.driveContextPanel.<String>getField("name")
								  .setValue( record.getName() );
			this.driveContextPanel.setVisible(true);
		break;
		case PARTITION:
			this.currentContext = this.partitionContextPanel;
			this.partitionContextPanel.setVisible(true);
		break;
		case SYSTEM:
			this.currentContext = this.systemContextPanel;
			this.systemContextPanel.setVisible(true);
		break;
		case SETTINGS:
			this.currentContext = this.settingsContextPanel;
			this.settingsContextPanel.setVisible(true);
		break;
		}
	}
	
	protected Artifact getRecord() {
		return this.record;
	}
	
	protected void configUI() {
		
	}
	
	protected void buildUI() {
		this.setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
		
		this.add( this.createInfoPanel() );
		this.createContextPanels( this );
		this.add( this.createActionsPanel() );
	}

	protected FormPanel createInfoPanel() {
		FormPanel panel = new FormPanel();
		panel.addField("id", "UUID", new JLabel() );
		panel.addField("name", "Name", new JLabel() );
		panel.addField("created", "Created", new JLabel() ); 
		panel.addField("size", "Size", new JLabel() );
		
		JTextArea description = new JTextArea();
		description.setEditable(false);
		panel.addField("description", "Description", description );
		
		return panel;
	}
	
	protected void createContextPanels( Container context ) {
		context.add( this.driveContextPanel = this.createDriveContextPanel() );
		context.add( this.partitionContextPanel = this.createPartitionContextPanel() );
		context.add( this.settingsContextPanel = this.createSettingsContextPanel() );
		context.add( this.systemContextPanel = this.createSystemContextPanel() );
		
		this.contextPanels.add( this.driveContextPanel );
		this.contextPanels.add( this.partitionContextPanel );
		this.contextPanels.add( this.settingsContextPanel );
		this.contextPanels.add( this.systemContextPanel );
	}
	
	private FormPanel createSystemContextPanel() {
		FormPanel panel = new FormPanel();
		panel.setVisible(false);
		panel.addField("os", "OS", new JLabel() );
		panel.addField("kernel", "Kernel", new JLabel() );
		panel.addField("drives_present", "Drive images", new JCheckBox() );
		panel.addField("mbr_present", "MBR", new JCheckBox() );
		panel.addField("grub_image", "Bootloader", new JCheckBox() );
		panel.addField("bootloader", "Bootloader type", new JLabel() );
		panel.addField("bootloader_version", "Bootloader version", new JLabel() );
		
		return panel;
	}

	private FormPanel createSettingsContextPanel() {
		FormPanel panel = new FormPanel();
		// TODO
		panel.setVisible(false);
		return panel;
	}

	private FormPanel createPartitionContextPanel() {
		FormPanel panel = new FormPanel();
		panel.setVisible(false);
		panel.addField("filesystem", "Filesystem", new JLabel() );
		panel.addField("total", "Total", new JLabel() );
		panel.addField("used", "Used", new JLabel() );
		panel.addField("num", "Number", new JLabel() );
		
		return panel;
	}

	private FormPanel createDriveContextPanel() {
		FormPanel panel = new FormPanel();
		panel.setVisible(false);
		panel.addField("name", "Name", new JLabel() );
		panel.addField("model", "Model", new JLabel() );
		panel.addField("capacity", "Capacity", new JLabel() );
		panel.addField("partitions", "Partitions", new JLabel() );
		
		return panel;
	}

	protected Component createActionsPanel() {
		JLayeredPane pane = new JLayeredPane();
		pane.setLayout( new BoxLayout( pane, BoxLayout.X_AXIS ) );
		
		pane.add( new JButton(
			new InteractionAction(
				"Remove",
				new IEventHandler() {
					@Override
					public void handle(AppEvent event) {
						ArtifactDetailsPanel.this.onRemove( event );
					}
				}
			)
		) );

		return pane;
	}

	protected void onRemove( AppEvent event ) {
		Dispatcher.get().forwardEvent(
			ArtifactoryComponent.Events.Artifact.Delete,
			this.getRecord()
		);

		this.getRecord().remove();
	}

}
