package com.api.deployer.ui.widgets;

import com.api.deployer.jobs.backup.SystemBackupJob;
import com.api.deployer.system.devices.storage.StorageDiskDrive;
import com.api.deployer.ui.data.devices.storage.StorageDevice;
import com.api.deployer.ui.data.devices.storage.StorageDevicesStore;
import com.redshape.ui.application.UnhandledUIException;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.data.bindings.render.ISwingRenderer;
import com.redshape.ui.data.bindings.render.components.ObjectUI;
import com.redshape.ui.data.loaders.AbstractDataLoader;
import com.redshape.ui.data.loaders.LoaderException;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.views.widgets.AbstractWidget;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ObjectUITest extends AbstractWidget {
	private static final Logger log = Logger.getLogger( ObjectUITest.class );
	
	private JComponent ui;
	
	@Override
	public void init() {
		try {
			this.ui = new JPanel();
			this.ui.setLayout( new BoxLayout( this.ui, BoxLayout.Y_AXIS ) );
			this.ui.setMinimumSize( new Dimension(500, 200) );
			
			ISwingRenderer renderer = UIRegistry.<ISwingRenderer>getViewRendererFacade()
												.createRenderer( SystemBackupJob.class );
			
			UIRegistry.getProvidersFactory().registerLoader(renderer, StorageDevicesStore.class, new DevicesLoader() );
			
			final ObjectUI ui = renderer.render( this.ui, SystemBackupJob.class );
			ui.addButton( new JButton(
				new InteractionAction(
					"Save",
					new IEventHandler() {
						@Override
						public void handle(AppEvent event) {
							try {
								@SuppressWarnings("unused")
								SystemBackupJob object = ui.createInstance();
								JOptionPane.showMessageDialog( ui, "Information saved!");
							} catch ( Throwable e ) {
								throw new UnhandledUIException( e.getMessage(), e);
							}
						}
					}
				)
			) );
			
		} catch ( InstantiationException e ) {
			log.error( e.getMessage() );
			throw new UnhandledUIException( "Renderer construction exception", e );
		} catch ( Throwable e  ) {
			throw new UnhandledUIException( e.getMessage(), e );
		}
	}

	@Override
	public void unload(Container component) {
		component.remove( this.ui );
	}

	@Override
	public void render(Container component) {
		component.add( this.ui );
	}

	public static class DevicesLoader extends AbstractDataLoader<StorageDevice> {
		private static final long serialVersionUID = -5502252229938576937L;

		@Override
		public List<StorageDevice> doLoad() throws LoaderException {
			List<StorageDevice> result = new ArrayList<StorageDevice>();
			result.add( this.createDevice(0) );
			result.add( this.createDevice(1) );
			
			return result;
		}
		
		protected StorageDevice createDevice( int index ) {
			StorageDiskDrive drive = new StorageDiskDrive();
			drive.setPath("/dev/sda/" + index );
			drive.setUUID( UUID.randomUUID() );
			
			StorageDevice device = new StorageDevice();
			device.setPath( drive.getPath() );
			device.setUUID( drive.getUUID() );
			device.setRelatedObject( drive );
			
			return device;
		}
		
	}
	
}
