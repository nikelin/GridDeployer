package com.api.deployer.ui.components.workstations.config.panels;

import com.api.deployer.system.devices.IDevice;
import com.api.deployer.system.devices.storage.IStorageDevicePartition;
import com.api.deployer.system.devices.storage.IStorageDriveDevice;
import com.api.deployer.system.scanners.filters.StorageDriveDeviceFilter;
import com.api.deployer.ui.components.workstations.WorkstationComponent;
import com.api.deployer.ui.connector.DeployAgentConnector;
import com.api.deployer.ui.data.workstations.Workstation;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.UnhandledUIException;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.utils.UIRegistry;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.Collection;

public class PartitionsEditorPanel extends JPanel {
	private static final long serialVersionUID = -9025589732257659922L;
	
	private Workstation record;
	
	public PartitionsEditorPanel() {
		this(null);
	}
	
	public PartitionsEditorPanel(Workstation record) {
		super();
		
		this.record = record;
		
		this.buildUI();
	}
	
	public void setRecord( Workstation record ) {
		this.record = record;
	}
	
	public Workstation getRecord() {
		return this.record;
	}
	
	protected DeployAgentConnector getConnector() {
		return UIRegistry.getContext().getBean( DeployAgentConnector.class );
	}
	
	protected Collection<IDevice> getDevices() throws RemoteException {
		return this.getConnector().getDevices( this.getRecord().getId(), new StorageDriveDeviceFilter() );
	}
	
	protected void buildUI() {
		JPanel devicesPane = new JPanel();
		try {
			for ( IDevice device : this.getDevices() ) {
				devicesPane.add( device.getPath(), this.createDevicePane( (IStorageDriveDevice ) device ) );
			}
		} catch ( Throwable e ) {
			throw new UnhandledUIException( e.getMessage(), e );
		}
		
		this.add( devicesPane );
		
		Box box = Box.createHorizontalBox();
		box.add( new JButton(
			new InteractionAction(
				"Save",
				new IEventHandler() {
					@Override
					public void handle(AppEvent event) {
						PartitionsEditorPanel.this.setVisible(false);
					}
				}
			)
		) );
		
		box.add( new JButton(
			new InteractionAction(
				"Close",
				new IEventHandler() {
					@Override
					public void handle(AppEvent event) {
						PartitionsEditorPanel.this.setVisible(false);
					}
				}
			)	
		) );
		this.add(box);
	}
	
	protected JComponent createDevicePane( final IStorageDriveDevice device ) {
		JPanel panel = new JPanel();
		
		Box box = Box.createVerticalBox();
		
		for ( IStorageDevicePartition partition : device.getPartitions() ) {
			box.add( this.createDevicePane(partition) );
		}
		
		JScrollPane boxWrapper = new JScrollPane(box);
		
		panel.add( boxWrapper );
		panel.add( new JButton(
			new InteractionAction(
				"Add",
				new IEventHandler() {
					@Override
					public void handle(AppEvent event) {
						Dispatcher.get().forwardEvent(
							WorkstationComponent.Events.View.Configuration.PartitionsEditor.Add,
							PartitionsEditorPanel.this.getRecord(),
							device
						);
					}
				}
			)	
		) );
		
		return panel;
	}
	
	protected JComponent createDevicePane( final IStorageDevicePartition device ) {
		Box box = Box.createHorizontalBox();
		
		box.add( new JTextField( String.valueOf( device.getNumber() ) ) );
		box.add( new JTextField( device.getPath() ) );
		box.add( new JTextField( device.getFilesystem().getCode() ) );
		box.add( new JTextField( String.valueOf( device.getStart() ) ) );
		box.add( new JTextField( String.valueOf( device.getEnd() ) ) );
		box.add( new JTextField( String.valueOf( device.getSize() ) ) );
		
		box.add( new JButton(
			new InteractionAction(
				"X",
				new IEventHandler() {
					@Override
					public void handle(AppEvent event) {
						Dispatcher.get().forwardEvent(
							new AppEvent(
								WorkstationComponent.Events.View.Configuration.PartitionsEditor.Remove,
								PartitionsEditorPanel.this.getRecord(),
								device
							)
						);
					}
				}
			)
		) );
		
		return box;
	}
	
}
