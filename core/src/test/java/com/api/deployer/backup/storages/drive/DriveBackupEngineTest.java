package com.api.deployer.backup.storages.drive;

import com.api.deployer.AbstractContextAwareTest;
import com.api.deployer.backup.BackupException;
import com.api.deployer.backup.IBackupEngine;
import com.api.deployer.backup.result.storages.IDriveBackupResult;
import com.api.deployer.backup.storages.IStorageBackupFacade;
import com.api.deployer.backup.storages.drive.DriveBackupEngine.Operations;
import com.api.deployer.io.transport.IDestination;
import com.api.deployer.io.transport.local.LocalDestination;
import com.api.deployer.io.transport.mounters.IDestinationMounter;
import com.api.deployer.io.transport.mounters.IDestinationMountingFacade;
import com.api.deployer.io.transport.mounters.MountException;
import com.api.deployer.io.transport.ssh.SSHDestination;
import com.api.deployer.system.ISystemFacade;
import com.api.deployer.system.devices.IDevice;
import com.api.deployer.system.devices.storage.IStorageDriveDevice;
import com.api.deployer.system.devices.storage.StorageFilesystem;
import com.api.deployer.system.scanners.ScannerException;
import com.api.deployer.system.scanners.filters.StorageDriveDeviceFilter;
import com.redshape.daemon.jobs.handlers.HandlingException;
import com.redshape.utils.config.ConfigException;
import com.redshape.utils.config.IConfig;
import com.redshape.utils.config.IWritableConfig;
import jcifs.dcerpc.UUID;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertNotNull;

public class DriveBackupEngineTest extends AbstractContextAwareTest<Object> {
	private IStorageBackupFacade backupFacade;

	public DriveBackupEngineTest() {
		super("src/test/resources/context.xml");
	}

	protected IStorageBackupFacade getBackupFacade() {
		IStorageBackupFacade facade = this.backupFacade;
		if ( facade == null ) {
			this.backupFacade = facade = this.getContext().getBean(IStorageBackupFacade.class);
			facade.setContext( this.getContext() );
			facade.makeSilent(true);
		}

		return facade;
	}

	protected ISystemFacade getSystemFacade() {
		return this.getContext().getBean(ISystemFacade.class);
	}

	protected IDestinationMountingFacade getMountingFacade() {
		return this.getContext().getBean(IDestinationMountingFacade.class);
	}

	@Test
	public void testBackup() throws ScannerException, BackupException,
			MountException,
			URISyntaxException, ConfigException {
		for ( IDevice filteredDevice : this.getSystemFacade()
				.getDevices(
						new StorageDriveDeviceFilter() {
							/**
							 *
							 */
							private static final long serialVersionUID = 5903226724477864824L;

							@Override
							public boolean filter( IDevice device ) {
								return super.filter(device) && device.getName() != "sda";
							}
						}
				) ) {
			final IStorageDriveDevice device = (IStorageDriveDevice) filteredDevice;

			IBackupEngine<IStorageDriveDevice, IDriveBackupResult> engine = this.getBackupFacade()
					.selectEngine(device);
			assertNotNull(engine);

			engine.makeSilent(true);
			engine.exclude(Operations.GRUB_BACKUP);

			engine.exclude( new UUID("c8b98008-858a-4d55-a65f-8869873b515d") );
			engine.exclude( new UUID("91c11b0c-2608-436b-969e-f45d54b003ef") );
			engine.exclude( StorageFilesystem.NTFS );
			engine.exclude( StorageFilesystem.VFAT );

			IDestination destination = this.createLocalDestination("/tmp");
			IDestinationMounter<IDestination> mounter = this.getMountingFacade().selectMounter(destination);
			mounter.setMountingPoint("/tmp");

			IDriveBackupResult result = engine.save( device, mounter.mount(destination) );
			assertNotNull(result);

			//TODO
//			DiskMetaGenerator metaGenerator = new DiskMetaGenerator();
//			IConfig driveBackupMeta = metaGenerator.generate( XMLConfig.createEmpty("meta"), result);
//			assertNotNull(driveBackupMeta);
		}
	}

	@Test
	public void testRestore() throws ScannerException, BackupException,
			MountException,
			URISyntaxException, ConfigException, HandlingException {
		IDestination destination = this.createLocalDestination("/tmp");

		IDestinationMounter<IDestination> mounter = this.getMountingFacade().selectMounter(destination);
		mounter.setMountingPoint("/tmp");

		UUID artifactId = new UUID( "00773239-7a74-4285-b889-f21e0152bec3" );

		String path = mounter.mount(destination) + File.separator + artifactId;


		IWritableConfig artifactoryIndex = this.getContext().getBean("testRepositoryIndex", IWritableConfig.class);
		IConfig artifactMeta = null;
		for ( IConfig artifactNode : artifactoryIndex.get("artifacts").childs() ) {
			if ( artifactNode.attribute("uuid").equals( artifactId.toString().toLowerCase() ) ) {
				artifactMeta = artifactNode;
				break;
			}
		}

		if ( artifactMeta == null ) {
			throw new HandlingException("Index does not contains metadata for specified artifact");
		}

		final String artifactPath = artifactMeta.get("drive").get("path").value();
		IStorageDriveDevice device = (IStorageDriveDevice) this.getSystemFacade()
				.getDevice(
						new StorageDriveDeviceFilter() {
							private static final long serialVersionUID = -1499118074166974434L;

							@Override
							public boolean filter( IDevice device ) {
								return super.filter(device) && device.getPath().equals( artifactPath );
							}
						}
				);

		if ( device == null ) {
			throw new HandlingException("Device with " +
					"uuid(" + device + ") does not exists!");
		}

		IBackupEngine<IStorageDriveDevice, IDriveBackupResult>
				engine = this.getBackupFacade().selectEngine(device);

		engine.restore( path, device, artifactMeta );
	}

	protected IDestination createLocalDestination( String path ) throws URISyntaxException {
		return new LocalDestination( new URI("file://" + path ) );
	}

	protected IDestination createSSHDestination( String uri ) throws URISyntaxException {
		return new SSHDestination( new URI("ssh://" + uri) );
	}

}
