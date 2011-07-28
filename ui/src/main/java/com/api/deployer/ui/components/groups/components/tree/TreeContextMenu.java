package com.api.deployer.ui.components.groups.components.tree;

import com.api.deployer.ui.components.groups.GroupsComponent;
import com.api.deployer.ui.components.groups.components.GroupsTree;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.components.InteractionAction;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class TreeContextMenu extends JPopupMenu {
	private static final long serialVersionUID = 7672229708189643272L;

	private DefaultMutableTreeNode nodeContext;
	private GroupsTree context;
	
	public TreeContextMenu( GroupsTree context, DefaultMutableTreeNode nodeContext ) {
		super();
		
		this.context = context;
		this.nodeContext = nodeContext;
		
		this.init();
	}
	
	protected void init() {
		if ( this.nodeContext == null ) {
        	this.addGlobalItems();
        } else {
        	this.addNodeItems();
        }
	}
	
	private void addNodeItems() {
		this.add( new JMenuItem( new InteractionAction( "Add workstation...",
                new AppEvent( GroupsComponent.Events.Station.Add, nodeContext.getUserObject() ) ) ) );
        this.add( new JMenuItem( new InteractionAction( "Rename",
                new AppEvent(GroupsComponent.Events.Rename, nodeContext ) ) ) );
        this.add( new JMenuItem( new InteractionAction( "Remove",
            new IEventHandler() {
				@Override
				public void handle(AppEvent ae) {
					TreeContextMenu.this.context.removeNode(nodeContext );
				}
			}  
        ) ) );
	}
	
	private void addGlobalItems() {
		this.add( new JMenuItem( new InteractionAction( "Create group",
			new IEventHandler() {
				@Override
				public void handle( AppEvent ae ) {
					JOptionPane.showMessageDialog( 
							TreeContextMenu.this.context, "Group adding requested...");
				}
			} )
		) );
	}
}