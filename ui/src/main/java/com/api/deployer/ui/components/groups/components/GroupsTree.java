package com.api.deployer.ui.components.groups.components;

import com.api.deployer.ui.components.groups.GroupsComponent;
import com.api.deployer.ui.components.groups.components.tree.TreeContextMenu;
import com.api.deployer.ui.data.workstations.Workstation;
import com.api.deployer.ui.data.workstations.groups.StationGroupsStore;
import com.api.deployer.ui.data.workstations.groups.WorkstationGroup;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.application.events.UIEvents;
import com.redshape.ui.data.adapters.swing.AbstractDataTree;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Tree to display workstation groups & it's content
 *
 * @author nikelin
 */
public class GroupsTree extends AbstractDataTree<WorkstationGroup, StationGroupsStore> {
	private static final long serialVersionUID = -2009617809112385029L;
	private static final Logger log = Logger.getLogger( GroupsTree.class );
	
	public GroupsTree( StationGroupsStore store ) {
		super(store);
	}

	@Override
	protected void init() {
		super.init();

		Dispatcher.get().addListener(
			GroupsComponent.Events.Station.Added,
			new IEventHandler() {
				@Override
				public void handle(AppEvent event) {
					GroupsTree.this.onStationAdded(event);
				}
			}
		);
	}

	protected void onStationAdded( AppEvent event ) {
		this.insertNode( this.findNode( event.getArg(0) ), this.createNode(event.getArg(1) ) );
	}

	@Override
	protected JPopupMenu createContextMenu( DefaultMutableTreeNode node ) {
		return new TreeContextMenu(this, node);
	}

	@Override
	public void removeRecord( WorkstationGroup group ) {
    	this.removeNode( this.findNode(group) );

		Dispatcher.get().forwardEvent( new AppEvent( UIEvents.Core.Repaint, this ) );
    }

	public void addStation( WorkstationGroup group, Workstation station ) {
		this.insertNode( this.findNode(group), this.createNode(station) );
	}

	@Override
	public DefaultMutableTreeNode addRecord( WorkstationGroup group ) {
        DefaultMutableTreeNode node;
		log.info("Group added!");
		if ( ( node = this.findNode(group) ) == null ) {
			this.insertNode( node = this.createNode(group) );

			Dispatcher.get().forwardEvent( new AppEvent( UIEvents.Core.Repaint, this ) );
		}

        return node;
	}

	@Override
	protected void invalidateRecord( WorkstationGroup group ) {
		DefaultMutableTreeNode node = this.findNode(group);
		if ( node == null ) {
			throw new IllegalArgumentException("Unrelated group invalidation");
		}
		
		for ( Workstation station : group.getWorkstations() ) {
			DefaultMutableTreeNode stationNode = this.findNode( this.findNode( group ), station );
			if ( stationNode == null ) {
				this.insertNode( node, this.createNode(station) );
			}
		}
	}

}
