package com.api.deployer.artifactory.services;

import com.api.commons.IFilter;

import com.api.deployer.backup.artifactory.ArtifactoryVersion;
import com.api.deployer.backup.artifactory.IArtifactoryFacade;
import com.api.deployer.backup.artifactory.ReaderException;
import com.api.deployer.backup.artifactory.WriterException;
import com.api.deployer.backup.artifactory.artifacts.IArtifact;
import com.api.deployer.backup.artifactory.filters.UUIDFilter;
import com.api.deployer.backup.artifactory.index.IArtifactoryIndex;
import com.api.deployer.backup.artifactory.index.IndexException;

import com.api.deployer.backup.artifactory.index.LockedException;
import com.api.deployer.execution.services.IArtifactoryService;
import com.api.deployer.io.transport.IDestination;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * @author nikelin
 * @date 13/04/11
 * @package com.api.deployer.artifactory.services
 */
public class ArtifactoryService implements IArtifactoryService {
    private static final Logger log = Logger.getLogger(ArtifactoryService.class);

    private static Object lock = new Object();
	private String serviceName;
	private IArtifactoryIndex artifactoryIndex;
	private ApplicationContext context;
	private String indexPath;
    private Timer timer;

	public ArtifactoryService( String serviceName, String indexPath, ApplicationContext context )
			throws ArtifactoryServiceException {
		this.serviceName = serviceName;
		this.context = context;
        this.timer = new Timer();
		this.indexPath = indexPath;

		this.init();
	}

    protected Timer getTimer() {
        return this.timer;
    }

	protected ApplicationContext getContext() {
		return this.context;
	}

	protected void init() throws ArtifactoryServiceException {
        this.timer.scheduleAtFixedRate(
            new TimerTask() {
                @Override
                public void run() {
                    try {
                        synchronized (lock) {
                            ArtifactoryService.this.getFacade().flushIndex(
                                ArtifactoryService.this.indexPath,
                                ArtifactoryService.this.artifactoryIndex
                            );

                            log.info("Index flushed!");
                        }
                    } catch ( WriterException e ) {
                        throw new RuntimeException("Unable to flush index changes!", e );
                    } catch ( LockedException e ) {
                        throw new RuntimeException("Unable to access writing scope!", e );
                    }
                }
            },
            0,
            2000
        );

		try {
			this.artifactoryIndex = this.getFacade().getIndexReader().readIndex( this.getIndexPath() );
		} catch ( ReaderException e ) {
			throw new ArtifactoryServiceException("Index reading exception", e );
		} catch ( IndexException e ) {
			throw new ArtifactoryServiceException("Index corrupted exception", e );
		}
	}

	protected IArtifactoryFacade getFacade() {
		return this.getContext().getBean( IArtifactoryFacade.class );
	}

	protected IArtifactoryIndex getArtifactoryIndex() {
		return this.artifactoryIndex;
	}

	protected String getIndexPath() {
		return this.indexPath;
	}

	@Override
	public IDestination getDestination() {
		return this.getContext().getBean( IDestination.class );
	}

	@Override
	public Collection<IArtifact> getList() throws ArtifactoryServiceException {
		return this.getArtifactoryIndex().getArtifacts();
	}

	@Override
	public Collection<IArtifact> findArtifacts(IFilter<IArtifact> filter) throws ArtifactoryServiceException {
		return this.getArtifactoryIndex().getArtifacts( filter );
	}

	@Override
	public IArtifact findArtifact(IFilter<IArtifact> filter) throws ArtifactoryServiceException {
		return this.getArtifactoryIndex().getArtifact( filter );
	}

	@Override
	public synchronized void addArtifact(IArtifact artifact) throws ArtifactoryServiceException {
		try {
			this.getArtifactoryIndex().addArtifact( artifact );
		} catch ( IndexException e ) {
			throw new ArtifactoryServiceException( e.getMessage(), e );
		}
	}

	@Override
	public synchronized void removeArtifact(UUID artifactId) throws ArtifactoryServiceException {
		try {
			this.getArtifactoryIndex().removeArtifact( new UUIDFilter(artifactId) );
		} catch ( IndexException e ) {
			throw new ArtifactoryServiceException( e.getMessage(), e );
		}
	}

	@Override
	public ArtifactoryVersion getIndexVersion() throws ArtifactoryServiceException {
		return this.getArtifactoryIndex().getVersion();
	}

	@Override
	public boolean getIndexStatus() throws ArtifactoryServiceException {
		return true;
	}

	@Override
	public String getServiceName() throws ArtifactoryServiceException {
		return this.serviceName;
	}
}
