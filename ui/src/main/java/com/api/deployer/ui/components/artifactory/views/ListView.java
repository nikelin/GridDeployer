package com.api.deployer.ui.components.artifactory.views;

import java.awt.Component;
import java.awt.Container;
import java.rmi.RemoteException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.api.deployer.ui.components.artifactory.data.ArtifactsStore;
import com.api.deployer.ui.connector.ArtifactoryConnector;
import com.api.deployer.ui.connector.DeployAgentConnector;
import org.springframework.context.ApplicationContext;

import com.api.deployer.ui.components.artifactory.ArtifactoryComponent;
import com.api.deployer.ui.components.artifactory.data.Artifact;
import com.api.deployer.ui.components.artifactory.data.ArtifactModel;
import com.api.deployer.ui.components.artifactory.data.loaders.ArtifactsLoader;
import com.api.deployer.ui.components.artifactory.views.panels.ArtifactDetailsPanel;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.UnhandledUIException;
import com.redshape.ui.actions.InteractionAction;
import com.redshape.ui.data.IStore;
import com.redshape.ui.data.adapters.swing.TableAdapter;
import com.redshape.ui.data.loaders.LoaderException;
import com.redshape.ui.data.stores.ListStore;
import com.redshape.ui.events.AppEvent;
import com.redshape.ui.events.IEventHandler;
import com.redshape.ui.utils.UIConstants;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.views.IView;

public class ListView implements IView {
	private static final long serialVersionUID = -7779300503913949974L;
	
	private Component component;
	private ApplicationContext context;
	private IStore<Artifact> store;
	
	public ListView() {
		this( UIRegistry.<ApplicationContext>get( UIConstants.System.SPRING_CONTEXT ) );
	}
	
	public ListView( ApplicationContext context ) {
		this.context = context;
	}

	protected ApplicationContext getContext() {
		return this.context;
	}

	protected ArtifactoryConnector getArtifactoryConnector() {
		return this.getContext().getBean( ArtifactoryConnector.class );
	}

	protected DeployAgentConnector getConnector() {
		return this.getContext().getBean( DeployAgentConnector.class );
	}
	
	@Override
	public void handle(AppEvent event) {
	}

	@Override
	public void init() {
		try {
			this.store = this.createStore();
		} catch ( InstantiationException e ) {
			throw new UnhandledUIException("Store creation failed", e);
		}
		
		JPanel pane = new JPanel();
		
		final ArtifactDetailsPanel detailsPanel = new ArtifactDetailsPanel();
		
		JPanel tableWrapper = new JPanel();
		tableWrapper.setLayout( new BoxLayout( tableWrapper, BoxLayout.Y_AXIS ) );
		
		JTable table = new TableAdapter<Artifact>( this.store );
		table.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		table.getSelectionModel().addListSelectionListener( new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				detailsPanel.setRecord( ListView.this.store.getAt( e.getLastIndex() ) );
			}
		});
		
		JScrollPane scrollHandler = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		
		tableWrapper.add( scrollHandler );
		tableWrapper.add( new JButton(
			new InteractionAction(
				"Refresh",
				new IEventHandler() {
					@Override
					public void handle( AppEvent event ) {
						ListView.this.onRefresh( event );
					}
				}
			)
		));
		
		pane.add( tableWrapper );
		pane.add( detailsPanel );
		
		this.component = pane;
	}

	protected void onRefresh( AppEvent event ) {
		try {
			ArtifactoryConnector connector = this.getArtifactoryConnector();
			if ( connector == null || !connector.isConnected() ) {
				JOptionPane.showMessageDialog(
					UIRegistry.getRootContext(),
					"Artifactory not configured!"
				);

				Dispatcher.get().forwardEvent( ArtifactoryComponent.Events.Views.Settings.Connection );

				return;
			}

			ListView.this.store.load();
		} catch ( LoaderException e ) {
			throw new UnhandledUIException( e.getMessage(), e );
		}
	}
	
	protected IStore<Artifact> createStore() throws InstantiationException {
		return UIRegistry.getStoresManager().getStore(ArtifactsStore.class);
	}

	@Override
	public void render(Container component) {
		component.add( this.component );
	}
	
	@Override
	public void unload(Container component) {
		component.remove(this.component);
	}

}
