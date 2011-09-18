package com.api.deployer.ui.components.workstations.panels;

import com.api.deployer.ui.components.workstations.WorkstationComponent;
import com.api.deployer.ui.data.workstations.Workstation;
import com.api.deployer.ui.data.workstations.groups.StationGroupsStore;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.data.stores.IStoresManager;
import com.redshape.ui.data.stores.StoreEvents;
import com.redshape.ui.panels.FormPanel;
import com.redshape.ui.utils.UIRegistry;
import org.apache.log4j.Logger;

import javax.swing.*;

public class WorkstationControlPanel extends FormPanel {
	private static final long serialVersionUID = 2685068574204257073L;
	private static final Logger log = Logger.getLogger( WorkstationControlPanel.class );
	
	private Workstation record;
	
	public WorkstationControlPanel() {
		super();
		
		this.enableButtons(false);
	}
	
	public void changeActiveRecord( Workstation station ) {
		this.record = station;
		this.updateFields();
	}
	
	protected Workstation getActiveRecord() {
		return this.record;
	}

	protected void updateFields() {
		this.getField("name").setValue( this.record.getName() );
		this.getField("hostname").setValue( this.record.getHostname() );
		
		if ( this.record.getGroup() != null ) {
			this.getField("group").setValue( this.record.getGroup().getName() );
		}
		
		this.enableButtons(true);
		
		this.revalidate();
		this.repaint();
	}
	
	@Override
	protected void configUI() {
		super.configUI();
		
	}
	
	@Override
	protected void buildUI() {
		super.buildUI();
		
		JTextField nameField = new JTextField();
		nameField.setSize(100, 25);
		this.addField("name", "Name", nameField );
		
		JTextField hostName = new JTextField();
		hostName.setSize(100, 25);
		
		this.addField("hostname", "Hostname", hostName );
		
		try {
			this.addField("group", "Group", this.createGroupSelector() );
		} catch ( InstantiationException e ) {
			log.info( e.getMessage(), e );
		}
		
		this.addButton( new JButton(
			new InteractionAction(
				"Console",
				new IEventHandler() {
					
					@Override
					public void handle(AppEvent event) {
						Dispatcher.get().forwardEvent(
							new AppEvent(
								WorkstationComponent.Events.View.Console,
								WorkstationControlPanel.this.getActiveRecord()
							)
						);
					}
				}
			)	
		) );
		
		this.addButton( new JButton(
			new InteractionAction(
				"Config", 
				new IEventHandler() {
					@Override
					public void handle( AppEvent event ) {
						Dispatcher.get().forwardEvent(
							new AppEvent(
								WorkstationComponent.Events.View.Config, 
								WorkstationControlPanel.this.getActiveRecord()
							)
						);
					}
				}
			)
		));
		
		this.addButton( new JButton(
			new InteractionAction(
				"Restore", 
				new IEventHandler() {
					@Override
					public void handle( AppEvent event ) {
						Dispatcher.get().forwardEvent(new AppEvent(
							WorkstationComponent.Events.View.Restore,
							WorkstationControlPanel.this.getActiveRecord()
						) );
					}
				}
			)
		) );
		
		this.addButton( new JButton(
			new InteractionAction(
				"Backup",
				new IEventHandler() {
					@Override
					public void handle( AppEvent event ) {
						Dispatcher.get().forwardEvent( new AppEvent(
							WorkstationComponent.Events.View.Backup,
							WorkstationControlPanel.this.getActiveRecord()
						) );
					}
				}
			) 
		) );
		
		this.addButton( new JButton(
			new InteractionAction(
				"Reboot",
				new IEventHandler() {
					@Override
					public void handle(AppEvent event) {
						Dispatcher.get().forwardEvent(new AppEvent(
							WorkstationComponent.Events.View.Reboot,
							WorkstationControlPanel.this.getActiveRecord()
						) );
					}
				}
			) 
		) );
		
		this.addButton( new JButton(
			new InteractionAction(
				"Shutdown",
				new IEventHandler() {
					@Override
					public void handle(AppEvent event) {
						Dispatcher.get().forwardEvent(new AppEvent(
							WorkstationComponent.Events.View.Shutdown,
							WorkstationControlPanel.this.getActiveRecord()
						) );
					}
				}
			)
		) );
	}
	
	protected JComboBox createGroupSelector() throws InstantiationException {
		StationGroupsStore store = UIRegistry.getContext().getBean( IStoresManager.class )
														  .getStore( StationGroupsStore.class );
		
		final JComboBox box = new JComboBox( store.getList().toArray() );
		
		store.addListener( StoreEvents.Added, new IEventHandler() {
			@Override
			public void handle(AppEvent event) {
				box.addItem( event.getArg(0) );
				box.revalidate();
				box.repaint();
			}
		});
		
		store.addListener( StoreEvents.Removed, new IEventHandler() {
			@Override
			public void handle(AppEvent event) {
				box.removeItem( event.getArg(0) );
				box.revalidate();
				box.repaint();
			}
		});
		
		return box;
	}
}
