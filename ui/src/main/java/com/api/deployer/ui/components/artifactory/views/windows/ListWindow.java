package com.api.deployer.ui.components.artifactory.views.windows;

import com.api.deployer.backup.artifactory.artifacts.IArtifact;
import com.api.deployer.ui.components.artifactory.ArtifactoryComponent;
import com.api.deployer.ui.components.artifactory.data.Artifact;
import com.api.deployer.ui.components.artifactory.data.ArtifactsStore;
import com.api.deployer.ui.components.artifactory.views.panels.ArtifactsTree;
import com.api.deployer.ui.connector.ArtifactoryConnector;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.UnhandledUIException;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.application.events.UIEvents;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.data.loaders.LoaderException;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.utils.config.IConfig;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;

/**
 * @author nikelin
 * @date 21/04/11
 * @package com.api.deployer.ui.components.artifactory.views.windows
 */
public class ListWindow extends JFrame {
	private ArtifactsStore store;
	private ArtifactsTree tree;

	private JPanel detailsPanel;

	public ListWindow() {
		super();

		this.init();
		this.buildUI();
		this.configUI();
	}

	protected ArtifactoryConnector getArtifactoryConnector() {
		return UIRegistry.getContext().getBean( ArtifactoryConnector.class );
	}

	protected void init() {
		this.store = new ArtifactsStore();

		Dispatcher.get().addListener(
			ArtifactoryComponent.Events.Views.ArtifactMeta,
			new IEventHandler() {
				@Override
				public void handle(AppEvent event) {
					ListWindow.this.onDetails();
				}
			}
		);

		Dispatcher.get().addListener(
				ArtifactoryComponent.Events.Connection.Refresh,
				new IEventHandler() {
					@Override
					public void handle(AppEvent event) {
						ListWindow.this.onRefresh();
					}
				}
		);
	}

	protected void buildUI() {
		this.setLayout( new BoxLayout( this.getContentPane(), BoxLayout.Y_AXIS ) );

		this.add( this.createTreePanel() );
		this.add( this.createDetailsPanel() );
		this.add( this.createControlPanel() );
	}

	protected JComponent createDetailsPanel() {
		this.detailsPanel = new JPanel();
		this.detailsPanel.setLayout( new GridLayout(0, 2) );
		this.detailsPanel.setVisible(false);
		this.detailsPanel.setBorder(BorderFactory.createTitledBorder("Details"));
		return this.detailsPanel;
	}

	protected JComponent createTreePanel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS ) );
		panel.add( new JScrollPane( this.tree = new ArtifactsTree( this.store ) ) );
        this.tree.addTreeSelectionListener( new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
                ListWindow.this.onDetails();
            }
        });

		return panel;
	}

	protected JComponent createControlPanel() {
		Box box = Box.createHorizontalBox();
		box.add( new JButton(
			new InteractionAction(
				"Refresh",
				new IEventHandler() {
					@Override
					public void handle(AppEvent event) {
						ListWindow.this.onRefresh();
					}
				}
			)
		) );

		box.add( new JButton(
			new InteractionAction(
				"Remove",
				new IEventHandler() {
					@Override
					public void handle(AppEvent event) {
						ListWindow.this.onRemove();
					}
				}
			)
		) );

		box.add( new JButton(
			new InteractionAction(
				"Details",
				new IEventHandler() {
					@Override
					public void handle(AppEvent event) {
						ListWindow.this.onDetails();
					}
				}
			)
		) );

		return box;
	}

	protected void displayDetails( Artifact artifact ) {
		if ( !this.checkConnection() ) {
			return;
		}

		this.detailsPanel.removeAll();

		IConfig config = artifact.<IArtifact>getRelatedObject().getData();
		for ( IConfig childNode : config.childs() ) {
			this.detailsPanel.add( new JLabel( childNode.name() ) );
			this.detailsPanel.add( new JLabel( childNode.value() ) );
		}

		this.detailsPanel.setVisible(true);

		Dispatcher.get().forwardEvent(UIEvents.Core.Repaint, this.detailsPanel);
	}

	protected void onDetails() {
		Artifact artifact = this.tree.getSelectedRecord() ;
		if ( artifact == null ) {
			return;
		}

		this.displayDetails( artifact );
	}

	protected boolean checkConnection() {
		if ( !this.getArtifactoryConnector().isConnected() ) {
			UIRegistry.getNotificationsManager().info("Connection with artifactory not established!");
			Dispatcher.get().forwardEvent(
				ArtifactoryComponent.Events.Views.Settings.Connection
			);

			return false;
		}

		return true;
	}

	protected void onRefresh() {
		if ( !this.checkConnection() ) {
			return;
		}

		try {
	   		this.store.load();
		} catch ( LoaderException e ) {
			throw new UnhandledUIException("Unable to load artifacts list!");
		}
	}

	protected void onRemove() {
		if ( !this.checkConnection() ) {
			return;
		}

	   	Artifact artifact = this.tree.getSelectedRecord();
		if ( artifact == null ) {
			return;
		}

		this.store.remove(artifact);

		Dispatcher.get().forwardEvent(ArtifactoryComponent.Events.Artifact.Delete, artifact);
	}

	protected void configUI() {
        this.setTitle("Artifactory Overview");
		this.setSize(600, 450);
	}

}
