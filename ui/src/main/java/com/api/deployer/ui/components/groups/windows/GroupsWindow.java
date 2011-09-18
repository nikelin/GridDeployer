package com.api.deployer.ui.components.groups.windows;

import com.api.deployer.ui.components.groups.components.GroupsTree;
import com.api.deployer.ui.data.workstations.groups.StationGroupsStore;
import com.api.deployer.ui.data.workstations.groups.WorkstationGroup;
import com.redshape.ui.application.UIException;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.handlers.WindowCloseHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.panels.FormPanel;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.utils.Function;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * @author nikelin
 */
public class GroupsWindow extends JFrame {
	private static final long serialVersionUID = -604450997521863645L;
	
	private FormPanel additionPanel;
	private GroupsTree tree;
	private GroupsWindow.EventHandlers eventsHandler;
	
	public GroupsWindow() throws UIException {
		super();
		
		this.eventsHandler = new EventHandlers();
		
		this.buildUI();
		this.configUI();
	}
	
	protected StationGroupsStore createStore() throws UIException {
		try {
			return UIRegistry.getStoresManager().getStore( StationGroupsStore.class );
		} catch ( InstantiationException e ) {
			throw new UIException( e.getMessage(), e );
		}
	}

	protected void buildUI() throws UIException {
		this.setLayout( new BoxLayout( this.getContentPane(), BoxLayout.Y_AXIS ) );
		
		this.add( this.createTreePane() );
		this.add( this.createAdditionPanel() );
		this.add( this.createButtonsPanel() );
	}
	
	protected JComponent createTreePane() throws UIException {
		 return new JScrollPane(
			this.tree = new GroupsTree( this.createStore() )
		 ); 
	}
	
	protected FormPanel createAdditionPanel() {
		FormPanel panel = this.additionPanel = new FormPanel();
		panel.addField( "name", "Group name", new JTextField() );
		
		return panel;
	}
	
	protected JComponent createButtonsPanel() throws UIException {
		try {
			JPanel panel = new JPanel();
			
			panel.add( new JButton(
				InteractionAction.createAction(
					"Create", 
					this.eventsHandler,
					new Function<EventHandlers, Void>(
						"onGroupCreate", EventHandlers.class, AppEvent.class ) 
					)
				) 
			);
			
			panel.add( new JButton(
				InteractionAction.createAction(
					"Remove", 
					this.eventsHandler,
					new Function<EventHandlers, Void> (
						"onGroupRemove", EventHandlers.class, AppEvent.class
					) 
				)	
			));
			panel.add( new JButton(new InteractionAction(
				"Close", new WindowCloseHandler(this)
			) ) );
			
			return panel;
		} catch ( NoSuchMethodException e ) {
			throw new UIException( e.getMessage(), e );
		}
	}

	protected void configUI() {
		this.setTitle("Workstation groups");
		this.setSize( 250, 500 );
	}
	
	public class EventHandlers {
		
		public void onGroupCreate( AppEvent event ) {
			GroupsWindow.this.tree.addRecord(
				new WorkstationGroup( 
					GroupsWindow.this.additionPanel
						.<String>getField("name").getValue() ) );
			
			GroupsWindow.this.additionPanel.getField("name").setValue("");
		}
		
		public void onGroupRemove( AppEvent event ) {
			TreePath path = GroupsWindow.this.tree.getSelectionModel().getSelectionPath();
			if ( path.getPathCount() == 0 ) {
				JOptionPane.showMessageDialog( GroupsWindow.this, "Select at least one group!");
			}
			
			GroupsWindow.this.tree.removeNode(
					((DefaultMutableTreeNode) path.getLastPathComponent() ) );
		}
	}
	
}
