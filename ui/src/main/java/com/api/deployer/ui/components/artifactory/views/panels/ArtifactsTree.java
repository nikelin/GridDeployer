package com.api.deployer.ui.components.artifactory.views.panels;

import com.api.deployer.ui.components.artifactory.ArtifactoryComponent;
import com.api.deployer.ui.components.artifactory.data.Artifact;
import com.api.deployer.ui.components.artifactory.data.ArtifactsStore;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.data.adapters.swing.AbstractDataTree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

/**
 * @author nikelin
 * @date 21/04/11
 * @package com.api.deployer.ui.components.artifactory.views.panels
 */
public class ArtifactsTree extends AbstractDataTree<Artifact, ArtifactsStore> {

	public ArtifactsTree( ArtifactsStore store ) {
		super( store );

		this.configUI();
	}

	@Override
	public DefaultMutableTreeNode addRecord(Artifact record) {
        DefaultMutableTreeNode node;
		if ( null != ( node = this.findNode(record) ) ) {
			return node;
		}

		this.insertNode( node = this.createNode( record ) );

		this.processChildren( node, record );

        return node;
	}

	protected void processChildren( DefaultMutableTreeNode node, Artifact record ) {
		if ( !record.hasChilds() ) {
			return;
		}

		for ( Artifact child : record.getChildren() ) {
			this.processChildren( this.addRecord(child), child );
		}
	}

	@Override
	public void removeRecord(Artifact record) {
		this.removeNode( this.findNode(record) );
	}

	@Override
	protected void invalidateRecord(Artifact record) {

	}

	@Override
	protected JPopupMenu createContextMenu(DefaultMutableTreeNode context) {
		return new TreeContext( this, context );
	}

	protected void configUI() {
		this.setPreferredSize( new Dimension(250, 120) );
	}

	public static class TreeContext extends JPopupMenu {
		private DefaultMutableTreeNode context;
		private ArtifactsTree treeContext;

		public TreeContext( ArtifactsTree tree, DefaultMutableTreeNode context) {
			this.context = context;
			this.treeContext = tree;

			this.init();
		}

		protected void init() {
			if ( this.context != null ) {
				this.initNodeContext();
			} else {
				this.initGlobalContext();
			}
		}

		protected void initNodeContext() {
			this.add( new JMenuItem(
				new InteractionAction(
					"Remove",
					new IEventHandler() {
						@Override
						public void handle(AppEvent event) {
							TreeContext.this.treeContext.removeNode(
								(DefaultMutableTreeNode) TreeContext.this.context.getParent(),
								TreeContext.this.context
							);
						}
					}
				)
			) );

			this.add( new JMenuItem(
				new InteractionAction(
					"Details",
					new IEventHandler() {
						@Override
						public void handle(AppEvent event) {
							Dispatcher.get().forwardEvent(
								ArtifactoryComponent.Events.Views.ArtifactMeta,
								(Artifact) TreeContext.this.context.getUserObject()
							);
						}
					}
				)
			) );
		}

		protected void initGlobalContext() {
			this.add( new JMenuItem(
				new InteractionAction(
					"Refresh",
					new IEventHandler() {
						@Override
						public void handle(AppEvent event) {
							Dispatcher.get().forwardEvent(
								ArtifactoryComponent.Events.Connection.Refresh
							);
						}
					}
				)
			) );

			this.add( Box.createHorizontalGlue() );

			this.add( new JMenuItem(
				new InteractionAction(
					"Artifactory version",
					new IEventHandler() {
						@Override
						public void handle(AppEvent event) {
							Dispatcher.get().forwardEvent( ArtifactoryComponent.Events.Views.Version );
						}
					}
				)
			) );

            JMenu settingsItem = new JMenu("Artifactory settings");

            settingsItem.add( new JMenuItem(
				new InteractionAction(
					"Environment",
					new IEventHandler() {
						@Override
						public void handle(AppEvent event) {
							Dispatcher.get().forwardEvent(
								ArtifactoryComponent.Events.Views.Settings.Application
							);
						}
					}
				)
			) );

            settingsItem.add( new JMenuItem(
                new InteractionAction(
                    "Connection",
                    new IEventHandler() {
                        @Override
                        public void handle(AppEvent event) {
                            Dispatcher.get().forwardEvent(
                                ArtifactoryComponent.Events.Views.Settings.Connection
                            );
                        }
                    }
                )
            ) );

			this.add( settingsItem );
		}

	}
}
