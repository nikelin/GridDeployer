package com.api.deployer.ui.components.workstations.views;

import com.api.deployer.ui.components.workstations.panels.WorkstationControlPanel;
import com.api.deployer.ui.connector.DeployAgentConnector;
import com.api.deployer.ui.data.workstations.DataLoader;
import com.api.deployer.ui.data.workstations.StationsStore;
import com.api.deployer.ui.data.workstations.Workstation;
import com.redshape.ui.application.UnhandledUIException;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.data.IStore;
import com.redshape.ui.data.adapters.swing.TableAdapter;
import com.redshape.ui.data.loaders.LoaderException;
import com.redshape.ui.data.loaders.policies.RefreshPolicy;
import com.redshape.ui.data.stores.StoreEvents;
import com.redshape.ui.utils.UIConstants;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.views.IView;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class ListView implements IView {
	private static final long serialVersionUID = -8617542420943851444L;
	
	private static final Logger log = Logger.getLogger( ListView.class );
	private Component component;
	private IStore<Workstation> store;
	private JButton refreshButton;
	
	public ListView() {
		try {
			this.store = UIRegistry.getStoresManager().getStore(StationsStore.class);
		} catch ( Throwable e ) {
			throw new UnhandledUIException("Workstations list load exception", e );
		}
	}
	
	protected DeployAgentConnector getConnector() {
		return this.getContext().getBean( DeployAgentConnector.class );
	}
	
	protected ApplicationContext getContext() {
		ApplicationContext context = UIRegistry.get( UIConstants.System.SPRING_CONTEXT );
		if ( context == null ) {
			throw new RuntimeException("Spring context not initialized");
		}
		
		return context;
	}
	
	@Override
	public void init() {
		try {
			JPanel rootComponent = new JPanel();
			rootComponent.setLayout( new BoxLayout(rootComponent, BoxLayout.Y_AXIS ) );
			
			JPanel tablePanel = new JPanel();
			tablePanel.setLayout( new BoxLayout( tablePanel, BoxLayout.Y_AXIS ) );
			
			JTable table = new TableAdapter<Workstation>(this.store);
			table.setSelectionBackground( new Color( 125, 98, 12 ) );
			table.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
			
			JScrollPane scrollPane = new JScrollPane(table);
			table.setFillsViewportHeight(true);
			table.setRowSelectionAllowed(true);
			
			tablePanel.add(scrollPane);
			
			tablePanel.add( this.refreshButton = new JButton(
				new InteractionAction(
					"Refresh", 
					new IEventHandler() {
						@Override
						public void handle(AppEvent event) {
							try {
								ListView.this.refreshButton.setEnabled(false);
								ListView.this.store.addListener(
									StoreEvents.Loaded,
									new IEventHandler() {
										@Override
										public void handle(AppEvent event) {
											ListView.this.refreshButton.setEnabled(true);
											ListView.this.refreshButton.repaint();
										}
									}
								);
								ListView.this.store.load();
							} catch ( LoaderException e ) {
								throw new UnhandledUIException( "Cannot refresh data store", e );
							}
						}
					})
			) );
			
			rootComponent.add(tablePanel);
			rootComponent.add( this.createControlPanel(table) );
			
			this.component = rootComponent;
		} catch ( Throwable e ) {
			log.error( e.getMessage(), e );
		}
	}
	
	protected JPanel createControlPanel( JTable table ) {
		final WorkstationControlPanel panel = new WorkstationControlPanel();
		
		table.getSelectionModel().addListSelectionListener( new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				panel.changeActiveRecord( ListView.this.store.getAt( e.getLastIndex() ) );
			}
		});
		
		return panel;
	}
	
	@Override
	public void handle( AppEvent event ) {
	}
	
	@Override
	public void render( Container component ) {
		try {
            this.store.setLoader( new RefreshPolicy( new DataLoader(), 4000 ) );
			this.store.load();

			component.add( this.component );
		} catch ( Throwable e ) {
			throw new UnhandledUIException("Workstations list rendering exception", e );
		}
	}
	
	@Override
	public void unload( Container component ) {
		component.remove( this.component );
	}

}
