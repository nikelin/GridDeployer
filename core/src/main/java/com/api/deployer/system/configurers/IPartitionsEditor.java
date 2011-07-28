package com.api.deployer.system.configurers;

import java.io.IOException;

import com.api.deployer.system.devices.storage.IStorageDevicePartition;
import com.api.deployer.system.devices.storage.PartitionType;
import com.api.deployer.system.devices.storage.StorageFilesystem;

public interface IPartitionsEditor {

	/**
	 * Create partition with `number` identifier in table with
	 * offset `start` and size `end` - `start`. 
	 * 
	 * @param number
	 * @param start
	 * @param end
	 */
	public IStorageDevicePartition createPartition( Integer number, 
			StorageFilesystem fs, PartitionType type, 
			Long start, Long end )  
		throws IOException;
	
	/**
	 * Remove partition from partitions table.
	 * 
	 * @param number
	 */
	public void deletePartition( Integer number )  
		throws IOException;
	
	/**
	 * Delete all partitions from table
	 */
	public void deletePartitions() throws IOException;
	
	/**
	 * Format partition specified by `number` to given filesystem.
	 * 
	 * @param number
	 * @param system
	 */
	public void makeFilesystem( Integer number, StorageFilesystem format )
	 	throws IOException;
	
	public void unmountPartition( IStorageDevicePartition partition ) throws IOException;
	
}
