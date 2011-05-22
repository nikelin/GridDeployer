package com.api.deployer.agent.handlers.backup;

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
import com.api.deployer.execution.services.IArtifactoryService;
import com.api.deployer.io.transport.IDestination;
import com.api.deployer.io.transport.mounters.IDestinationMounter;
import com.api.deployer.io.transport.mounters.IDestinationMountingFacade;
import com.api.deployer.io.transport.mounters.MountException;
import com.api.deployer.jobs.backup.IPartitionsBackupJob;
import com.api.deployer.jobs.backup.result.BackupJobResult;
import com.api.deployer.jobs.backup.result.IBackupJobResult;
import com.api.deployer.jobs.handlers.AbstractAwareJobHandler;
import com.api.deployer.jobs.handlers.AbstractJobHandler;
import com.api.deployer.jobs.handlers.HandlingException;
import com.api.deployer.system.ISystemFacade;
import com.api.deployer.system.devices.IDevice;
import com.api.deployer.system.devices.storage.IStorageDevicePartition;
import com.api.deployer.system.devices.storage.IStorageDriveDevice;
import com.api.deployer.system.devices.storage.PartitionFlag;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.UUID;

/**
 * @author nikelin
 * @date 26/04/11
 * @package com.api.deployer.agent.handlers.backup
 */
public class PartitionsBackupJobHandler extends AbstractAwareJobHandler<IPartitionsBackupJob, IBackupJobResult> {
    private IBackupEngine<IStorageDriveDevice, IDriveBackupResult> engine;
    private Object lock = new Object();

    public PartitionsBackupJobHandler( ISystemFacade facade ) {
        super(facade);
    }

    @Override
    protected IBackupJobResult createJobResult(UUID jobId) {
        return new BackupJobResult( jobId );
    }

    protected IDestinationMountingFacade getMountingFacade() {
        return this.getContext().getBean( IDestinationMountingFacade.class );
    }

    protected IStorageBackupFacade getBackupFacade() {
        return this.getContext().getBean( IStorageBackupFacade.class );
    }

    protected IArtifactoryFacade getArtifactoryFacade() {
        return this.getContext().getBean( IArtifactoryFacade.class );
    }

    @Override
    public IBackupJobResult handle(IPartitionsBackupJob job) throws HandlingException {
        synchronized (lock) {
            try {
                IArtifactoryService artifactoryService = this.getManagerContext().getArtifactoryService();
                if ( artifactoryService == null ) {
                    throw new HandlingException("Artifactory not binded");
                }

                IStorageDriveDevice device = job.getDevice();
                if ( device == null ) {
                    throw new HandlingException("Requested device does not exists");
                }

                IDestination destination = artifactoryService.getDestination();

                IDestinationMounter<IDestination> mounter = this.getMountingFacade()
                        .selectMounter(destination);
                if ( mounter == null ) {
                    throw new HandlingException("Unknown destination type");
                }

                String mountPath = DeployAgent.Attributes.Accessor.get(
                        DeployAgent.Attributes.MOUNTING_PATH ) ;
                mounter.setMountingPoint(mountPath);

                this.engine = this.getBackupFacade()
                                  .selectEngine( device );
                this.engine.makeSilent(true);

                for ( IStorageDevicePartition partition : device.getPartitions() ) {
                    if ( !job.getPartitions().contains(partition) ) {
                        this.engine.exclude( partition );
                    }
                }

                IBackupJobResult<IBackupResult> result = this.createJobResult( job.getId() );

                IDriveBackupResult engineResult = this.engine.save(
                    device, mounter.mount( destination )
                );

                engineResult.setImageName( job.getImageName() );
                engineResult.setImageDescription( job.getImageDescription() );

                artifactoryService.addArtifact( this.createArtifact( engineResult ) );

                result.setBackupInfo( engineResult );

                return result;
            } catch ( RemoteException e ) {
                throw new HandlingException("Network service interaction exception", e );
            } catch ( MountException e ) {
                throw new HandlingException("Destination mouting exception", e);
            } catch ( BackupException e ) {
                throw new HandlingException("Backup operation failed", e );
            } catch ( WriterException e ) {
                throw new HandlingException("Artifact writing exception", e );
            } catch ( InstantiationException e ) {
                throw new HandlingException("Unexpected exception been throws");
            }
        }
    }

    protected IArtifact createArtifact( IDriveBackupResult result ) throws InstantiationException, WriterException {
        IArtifact artifact = this.getArtifactoryFacade()
              .createArtifactBuilder()
                .createArtifact(ArtifactType.DRIVE);
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
                .createArtifact(ArtifactType.PARTITION);
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
    public void cancel() throws HandlingException {
        synchronized ( lock ) {
            try {
                this.engine.stop();
            } catch ( BackupException e ) {
                throw new HandlingException();
            }
        }
    }

    @Override
    public Integer getProgress() throws HandlingException {
        return 0;
    }
}
