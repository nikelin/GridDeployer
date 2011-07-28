package com.api.deployer.ui.components.groups.windows;

import com.api.deployer.ui.components.groups.GroupsComponent;
import com.api.deployer.ui.data.workstations.StationsStore;
import com.api.deployer.ui.data.workstations.Workstation;
import com.api.deployer.ui.data.workstations.groups.WorkstationGroup;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.UIException;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.application.events.handlers.WindowCloseHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.data.adapters.swing.ComboBoxAdapter;
import com.redshape.ui.panels.FormPanel;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.windows.swing.ISwingWindowsManager;
import com.redshape.utils.IFilter;

import javax.swing.*;
import java.awt.*;

public class AddWindow extends JFrame {
	private static final long serialVersionUID = -5099773509497140928L;
	
	private WorkstationGroup context;
	private JComboBox stationsList;
	
	public AddWindow() throws UIException {
		this(null);
	}
	
	public AddWindow( WorkstationGroup context ) throws UIException {
		super();
		
		this.context = context;
		
		this.buildUI();
		this.configUI();
	}
	
	public void setRecord( WorkstationGroup group ) {
		this.context = group;
	}
	
	protected JComboBox createStationsList() throws UIException {
		try {
			return this.stationsList = new ComboBoxAdapter<Workstation>(
				UIRegistry.getStoresManager().getStore( StationsStore.class ),
				new IFilter<Workstation>() {
					@Override
					public boolean filter( Workstation record ) {
						return record.getGroup() == null;
					}
				}
			);
		} catch ( InstantiationException e ) {
			throw new UIException( e.getMessage(), e );
		}
	}
	
	protected void buildUI() throws UIException {
		FormPanel panel = new FormPanel();
		panel.addField("workstation", "Select workstation to be added", this.createStationsList() );
		panel.addButton( new JButton(
			new InteractionAction(
				"Add",
				new IEventHandler() {
					@Override
					public void handle(AppEvent event) {
						Workstation workstation = (Workstation) AddWindow.this.stationsList.getSelectedItem();
						
						AddWindow.this.context.addWorkstation(workstation);
						
						Dispatcher.get().forwardEvent(
							GroupsComponent.Events.Station.Added,
							AddWindow.this.context,
							workstation
						);
						
						UIRegistry.<ISwingWindowsManager>getWindowsManager().close( AddWindow.this );
					}
				}
			)
		));
		
		panel.addButton( new JButton(
			new InteractionAction(
				"Close",
				new WindowCloseHandler(this)
			)
		) );
		
		this.add(panel);
	}
	
	protected void configUI() {
		this.setTitle("Adding workstation to group");
		this.setMinimumSize( new Dimension( 400, 100 ) );
	}

}
