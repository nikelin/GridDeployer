package com.api.deployer.agent.handlers.restore;

import java.io.File;
import java.rmi.RemoteException;
import java.util.UUID;

import com.api.commons.XMLHelper;
import com.api.commons.config.ConfigException;
import com.api.commons.config.IConfig;
import com.api.commons.config.IWritableConfig;
import com.api.commons.config.XMLConfig;
import com.api.deployer.backup.BackupException;
import com.api.deployer.backup.IBackupEngine;
import com.api.deployer.backup.result.storages.IDriveBackupResult;
import com.api.deployer.backup.storages.IStorageBackupFacade;
import com.api.deployer.system.devices.IDevice;
import com.api.deployer.system.devices.storage.IStorageDriveDevice;
import com.api.deployer.execution.services.IArtifactoryService;
import com.api.deployer.io.transport.IDestination;
import com.api.deployer.io.transport.mounters.IDestinationMounter;
import com.api.deployer.io.transport.mounters.IDestinationMountingFacade;
import com.api.deployer.io.transport.mounters.MountException;
import com.api.deployer.jobs.handlers.AbstractAwareJobHandler;
import com.api.deployer.jobs.handlers.HandlingException;
import com.api.deployer.jobs.restore.IStorageDriveRestoreJob;
import com.api.deployer.jobs.result.IJobResult;
import com.api.deployer.system.scanners.ScannerException;
import com.api.deployer.system.scanners.filters.StorageDriveDeviceFilter;
import com.api.deployer.system.ISystemFacade;

public class StorageRestoreJobHandler extends AbstractAwareJobHandler<IStorageDriveRestoreJob, IJobResult> {

	public StorageRestoreJobHandler(ISystemFacade facade) {
		super(facade);
	}
	
	protected IStorageBackupFacade getBackupFacade() {
		IStorageBackupFacade facade = this.getContext().getBean( IStorageBackupFacade.class );
		facade.setContext( this.getContext() );
		return facade;
	}
	
	protected IDestinationMountingFacade getMountingFacade() {
		return this.getContext().getBean( IDestinationMountingFacade.class );
	}

	@Override
	public Integer getProgress() {
		throw new UnsupportedOperationException("Operation not implemented");
	}

	@Override
	public void cancel() {
		throw new UnsupportedOperationException("Operation not implemented");
	}

	@Override
	public IJobResult handle( final IStorageDriveRestoreJob job )
			throws HandlingException {
		try {
			IArtifactoryService artifactoryService = this.getManagerContext().getArtifactoryService();

			IDestination destination = artifactoryService.getDestination();

			IDestinationMounter<IDestination> mounter = this.getMountingFacade().selectMounter( destination );
			if ( mounter == null ) {
				throw new HandlingException("Unknown destination type");
			}
			
			mounter.setMountingPoint( job.getMountingPoint() );
			
			String path = mounter.mount( destination, true ) + File.separator + job.getArtifactId();
			
			IStorageDriveDevice device = (IStorageDriveDevice) this.getSystem()
				 .getDevice( 
				 	new StorageDriveDeviceFilter() {
						private static final long serialVersionUID = 6011858507398632782L;

						@Override
				 		public boolean filter( IDevice device ) {
				 			return super.filter(device)
				 				&& device.getPath().equals( job.getDevice() );
				 		}
				 	}
		 	);
			if ( device == null ) {
				throw new HandlingException("Device with " +
						"uuid(" + job.getDevice() + ") does not exists!");
			}

			// Rewrite with new Artifactory API
			IWritableConfig artifactoryIndex = new XMLConfig( new XMLHelper(),
					new File( job.getMountingPoint() + "/meta.xml" ) );
			IConfig artifactMeta = null;
			for ( IConfig artifactNode : artifactoryIndex.get("artifacts").childs() ) {
				if ( artifactNode.attribute("uuid").equals( job.getArtifactId().toString() ) ) {
					artifactMeta = artifactNode;
					break;
				}
			}

			if ( artifactMeta == null ) {
				throw new HandlingException("Index does not contains metadata for specified artifact");
			}
			
			IBackupEngine<IStorageDriveDevice, IDriveBackupResult> 
					engine = this.getBackupFacade().selectEngine(device);
			engine.makeSilent( true );
			
			engine.restore( path, device, artifactMeta );
			
			return this.createJobResult( job.getId() );
		} catch ( MountException e ) {
			throw new HandlingException("Cannot mount destination source", e);
		} catch ( ConfigException e ) {
			throw new HandlingException("Meta-data reading exception", e );
		} catch ( ScannerException e  ) {
			throw new HandlingException("Device lookup failed", e);
		} catch ( BackupException e ) {
			throw new HandlingException("Error while attempting to proceed restoration", e);
		} catch ( RemoteException e ) {
			throw new HandlingException("Unexpected interaction exception", e );
		}
	}

	@Override
	protected IJobResult createJobResult(UUID jobId) {
		return null;
	}

}
