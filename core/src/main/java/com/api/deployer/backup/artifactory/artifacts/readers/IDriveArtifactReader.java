package com.api.deployer.backup.artifactory.artifacts.readers;

import java.util.Collection;
import java.util.UUID;

import com.api.deployer.backup.artifactory.ReaderException;


/**
 * Drive artifacts reader
 * 
 * @author nikelin
 */
public interface IDriveArtifactReader extends IArtifactReader {

	/**
	 * Read id's for related partitions artifacts
	 * @return
	 * @throws ReaderException
	 */
	public Collection<UUID> readPartitions() throws ReaderException;
	
	/**
	 * Read drive device model
	 * @return
	 * @throws ReaderException
	 */
	public String readModel() throws ReaderException;
	
	/**
	 * Read UUID of underlying drive device
	 * @return
	 * @throws ReaderException
	 */
	public UUID readUUID() throws ReaderException;
	
	/**
	 * Reader path to underlying drive 
	 * 
	 * @return
	 * @throws ReaderException
	 */
	public String readPath() throws ReaderException;
	
}
