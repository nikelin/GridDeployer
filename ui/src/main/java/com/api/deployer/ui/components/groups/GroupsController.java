/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.api.deployer.ui.components.groups;

import com.api.deployer.ui.components.groups.windows.AddWindow;
import com.api.deployer.ui.components.groups.windows.GroupsWindow;
import com.api.deployer.ui.data.workstations.groups.StationGroupsStore;
import com.api.deployer.ui.data.workstations.groups.WorkstationGroup;
import com.redshape.ui.application.AbstractController;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.windows.swing.ISwingWindowsManager;

import javax.swing.*;

import com.redshape.ui.application.annotations.Action;

/**
 *
 * @author nikelin
 */
public class GroupsController extends AbstractController {

	@Override
	protected void initEvents() {
		this.registerEvent( GroupsComponent.Events.Station.Add );
		this.registerEvent( GroupsComponent.Events.Station.Remove );
		this.registerEvent( GroupsComponent.Events.Views.List );
		this.registerEvent( GroupsComponent.Events.Add );
		this.registerEvent( GroupsComponent.Events.Remove );
		this.registerEvent( GroupsComponent.Events.Rename );
	}

	@Override
	protected void initViews() {
	}

	@Action( eventType = "GroupsComponent.Events.Station.Add")
	public void addStationAction( AppEvent event ) throws InstantiationException {
		AddWindow window = UIRegistry.<ISwingWindowsManager>getWindowsManager().open(AddWindow.class);
		window.setRecord( event.<WorkstationGroup>getArg(0) );
	}
	
	@Action( eventType = "GroupsComponent.Events.Remove")
	public void removeGroupAction( AppEvent event ) throws InstantiationException {
		WorkstationGroup group = event.<WorkstationGroup>getArg(0);
		if ( JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(
				UIRegistry.getRootContext(),
				"Do you really " +
				"want to delete group `"
				+ group.getName() + "?" ) ) {
			UIRegistry.getStoresManager().getStore( StationGroupsStore.class )
										 .remove( group );
		}
	}

	@Action( eventType = "GroupsComponent.Events.Views.List" )
	public void groupsView( AppEvent event ) {
		UIRegistry.<ISwingWindowsManager>getWindowsManager().open( GroupsWindow.class );
	}

}
