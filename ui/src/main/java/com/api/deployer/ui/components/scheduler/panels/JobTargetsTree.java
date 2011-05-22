package com.api.deployer.ui.components.scheduler.panels;

import com.api.deployer.ui.data.workstations.IDeploySubject;
import com.api.deployer.ui.data.workstations.Workstation;
import com.api.deployer.ui.data.workstations.groups.WorkstationGroup;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.events.UIEvents;
import com.redshape.ui.tree.AbstractTree;
import com.redshape.utils.IFilter;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.Collection;

/**
 * @author nikelin
 * @date 12/04/11
 * @package com.api.deployer.ui.components.scheduler.panels
 */
public class JobTargetsTree extends AbstractTree {

	public JobTargetsTree() {
		super();

		this.configUI();
	}

	public void removeRecord( IDeploySubject subject ) {

	}

	public DefaultMutableTreeNode addRecord( IDeploySubject subject ) {
		if ( subject instanceof Workstation ) {
			return this.addStation( (Workstation) subject );
		} else {
			return this.addGroup( (WorkstationGroup) subject );
		}
	}

	public DefaultMutableTreeNode addGroup( WorkstationGroup group ) {
		DefaultMutableTreeNode node = this.findNode(group);
		if ( node != null ) {
			return node;
		}

		this.insertNode( node = this.createNode(group) );
		Dispatcher.get().forwardEvent(UIEvents.Core.Repaint, this );

		return node;
	}

	public DefaultMutableTreeNode addStation( Workstation station ) {
		DefaultMutableTreeNode parentNode = this.getRoot();
		if ( station.getGroup() != null ) {
			parentNode = this.addGroup( station.getGroup() );
		}

		DefaultMutableTreeNode stationNode = this.findNode( parentNode, station );
		if ( stationNode != null ) {
			return stationNode;
		}

		this.insertNode( parentNode, stationNode = this.createNode(station) );

		Dispatcher.get().forwardEvent(UIEvents.Core.Repaint, this );

		return stationNode;
	}

	public Collection<IDeploySubject> getTargets() {
		return this.collect( new IFilter<DefaultMutableTreeNode>() {
			@Override
			public boolean filter( DefaultMutableTreeNode subject ) {
				if ( subject.getUserObject() instanceof IDeploySubject) {
					return true;
				}

				return false;
			}
		});
	}

	@Override
	protected JPopupMenu createContextMenu(DefaultMutableTreeNode path) {
		return new JPopupMenu();
	}

	protected void configUI() {
		this.setPreferredSize( new Dimension(250, 120));
	}
}
