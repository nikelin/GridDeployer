package com.api.deployer.execution.services;

import com.api.commons.IFilter;
import com.api.daemon.IRemoteService;
import com.api.deployer.backup.artifactory.ArtifactoryVersion;
import com.api.deployer.backup.artifactory.artifacts.IArtifact;
import com.api.deployer.io.transport.IDestination;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.UUID;

/**
 * @author nikelin
 * @date 13/04/11
 * @package com.api.deployer.execution.services
 */
public interface IArtifactoryService extends IRemoteService {

	/**
	 * Return destination path to a physical artifactory location
	 * @return
	 */
	public IDestination getDestination() throws RemoteException;

	/**
	 * Returns list of all available artifacts in index.
	 * @return
	 */
	public Collection<IArtifact> getList() throws RemoteException;

	/**
	 * Filter artifacts present in index through given filter object.
	 * Returns all matched objects.
	 *
	 * @param filter
	 * @return
	 * @throws RemoteException
	 */
	public Collection<IArtifact> findArtifacts( IFilter<IArtifact> filter ) throws RemoteException;

	/**
	 * Filter artifacts which presents in index through
	 * given filter and return first found if founded more than 1.
	 *
	 * @param filter
	 * @return
	 * @throws RemoteException
	 */
	public IArtifact findArtifact( IFilter<IArtifact> filter ) throws RemoteException;

	/**
	 * Add artifact to index
	 * @param artifact
	 * @throws RemoteException
	 */
	public void addArtifact( IArtifact artifact ) throws RemoteException;

	/**
	 * Remove artifact from index
	 * @param artifactId
	 * @throws RemoteException
	 */
	public void removeArtifact( UUID artifactId ) throws RemoteException;

	/**
	 * Returns current index version
	 * @return
	 * @throws RemoteException
	 */
	public ArtifactoryVersion getIndexVersion() throws RemoteException;

	/**
	 * Returns index status of availability
	 * @return
	 * @throws RemoteException
	 */
	public boolean getIndexStatus() throws RemoteException;

}
