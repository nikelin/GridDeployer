package com.api.deployer.agent.handlers.backup;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.UUID;

import com.api.deployer.execution.services.IArtifactoryService;

import com.api.deployer.agent.DeployAgent;

import com.api.deployer.backup.BackupException;
import com.api.deployer.backup.IBackupEngine;
import com.api.deployer.backup.artifactory.IArtifactoryFacade;
import com.api.deployer.backup.artifactory.WriterException;
import com.api.deployer.backup.artifactory.artifacts.ArtifactType;
import com.api.deployer.backup.artifactory.artifacts.IArtifact;
import com.api.deployer.backup.artifactory.artifacts.writers.IDriveArtifactWriter;
import com.api.deployer.backup.artifactory.artifacts.writers.IPartitionArtifactWriter;
import com.api.deployer.backup.result.IBackupResult;
import com.api.deployer.backup.result.storages.IDriveBackupResult;
import com.api.deployer.backup.result.storages.IPartitionBackupResult;
import com.api.deployer.backup.storages.IStorageBackupFacade;

import com.api.deployer.system.devices.IDevice;
import com.api.deployer.system.devices.storage.IStorageDriveDevice;
import com.api.deployer.system.devices.storage.PartitionFlag;
import com.api.deployer.io.transport.IDestination;
import com.api.deployer.io.transport.mounters.IDestinationMounter;
import com.api.deployer.io.transport.mounters.IDestinationMountingFacade;
import com.api.deployer.io.transport.mounters.MountException;
import com.api.deployer.jobs.backup.IStorageDriveBackupJob;
import com.api.deployer.jobs.backup.result.BackupJobResult;
import com.api.deployer.jobs.backup.result.IBackupJobResult;
import com.api.deployer.jobs.handlers.AbstractAwareJobHandler;
import com.api.deployer.jobs.handlers.HandlingException;
import com.api.deployer.system.ISystemFacade;

public class StorageBackupJobHandler extends AbstractAwareJobHandler<IStorageDriveBackupJob, IBackupJobResult<IBackupResult>> {
	private IBackupEngine<IStorageDriveDevice, IDriveBackupResult>  engine;

	public StorageBackupJobHandler( ISystemFacade facade ) {
		super(facade);
	}
	
	protected IArtifactoryFacade getArtifactoryFacade() {
		return this.getContext().getBean( IArtifactoryFacade.class );
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
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void cancel() throws HandlingException {
		try {
			this.engine.stop();
		} catch ( BackupException e  ) {
			throw new HandlingException( e.getMessage() );
		}
	}
	
	@Override
	public synchronized IBackupJobResult<IBackupResult> handle( IStorageDriveBackupJob job ) throws HandlingException {
		try {
			IArtifactoryService artifactoryService = this.getManagerContext().getArtifactoryService();
			if ( artifactoryService == null ) {
				throw new HandlingException("Artifactory not binded");
			}

			IDevice device = job.getDevice();
			if ( device == null ) {
				throw new HandlingException("Requested device does not exists");
			} 
			
			if ( !( device instanceof IStorageDriveDevice ) ) {
				throw new HandlingException("Requested device must be storage drive type");
			}
			
			final IStorageDriveDevice storageDevice = (IStorageDriveDevice) device;

			IDestination destination = artifactoryService.getDestination();

			IDestinationMounter<IDestination> mounter = this.getMountingFacade().selectMounter( destination );
			if ( mounter == null ) {
				throw new HandlingException("Unknown destination type");
			}

			String mountPath = DeployAgent.Attributes.Accessor.get(
					DeployAgent.Attributes.MOUNTING_PATH ) ;
			mounter.setMountingPoint(mountPath);
			
			this.engine = this.getBackupFacade()
							  .selectEngine( storageDevice );
			this.engine.makeSilent(true);
			
			IBackupJobResult<IBackupResult> result = this.createJobResult( job.getId() );

			IDriveBackupResult engineResult = this.engine.save(
				storageDevice, mounter.mount( destination )
			);

			engineResult.setImageName( job.getImageName() );
			engineResult.setImageDescription( job.getImageDescription() );

			artifactoryService.addArtifact( this.createArtifact( engineResult ) );

			result.setBackupInfo( engineResult );

			return result;
		} catch ( BackupException e ) {
			throw new HandlingException( "Backup operation failed", e );
		} catch ( MountException e ) {
			throw new HandlingException( "Given destination not accessible " +
						"or is not mountable for some reason", e );
		} catch ( WriterException e  ) {
			throw new HandlingException( "Index update exception", e );
		} catch ( InstantiationException e  ) {
			throw new HandlingException( "Internal exception", e );
		} catch ( RemoteException e ) {
			throw new HandlingException( "Artifactory interaction exception!", e );
		}
	}
	
	protected IArtifact createArtifact( IDriveBackupResult result ) throws InstantiationException, WriterException {
		IArtifact artifact = this.getArtifactoryFacade()
			  .createArtifactBuilder()
		  	  .createArtifact( ArtifactType.DRIVE );
		artifact.setId( UUID.randomUUID() );
		artifact.setName( result.getImageName() );
		artifact.setDescription( result.getImageDescription() );
		artifact.setTimestamp( new Date().getTime() );
		
		IDriveArtifactWriter writer = this.getArtifactoryFacade().createArtifactWriter(artifact);
		writer.writePath( result.getPath() );
		writer.writeUUID( result.getUUID() );
		writer.writeModel( result.getModel() );
		
		for ( IPartitionBackupResult partition : result.getPartitionsResult() ) {
			IArtifact partitionArtifact = this.getArtifactoryFacade()
				.createArtifactBuilder()
				.createArtifact( ArtifactType.PARTITION );
			IPartitionArtifactWriter partitionWriter = this.getArtifactoryFacade().createArtifactWriter(partitionArtifact);
			partitionWriter.writeDevice( artifact.getId() );
			partitionWriter.writeFileSystem( partition.getFilesystem() );
			partitionWriter.writeStart( partition.getStart() );
			partitionWriter.writeEnd( partition.getEnd() );
			partitionWriter.writeFileSystem( partition.getFilesystem() );
			partitionWriter.writeType( partition.getType() );
			
			for ( PartitionFlag flag : partition.getFlags() ) {
				partitionWriter.writeFlag( flag );
			}
		}
		
		return artifact;
	}

	@Override
	protected IBackupJobResult<IBackupResult> createJobResult( UUID jobId ) {
		return new BackupJobResult<IBackupResult>(jobId);
	}
	
}
