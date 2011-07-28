package com.api.deployer.backup;

import java.util.UUID;

import com.api.commons.config.IConfig;
import com.api.deployer.backup.result.IBackupResult;

import com.api.deployer.system.devices.IDevice;
import com.api.deployer.system.devices.storage.StorageFilesystem;

/**
 * Interface for backup engines such like partclone, fsarchiver, partimage, etc.
 * 
 * @author nikelin
 */
public interface IBackupEngine<T extends IBackupable, R extends IBackupResult> {
	
	public void stop() throws BackupException;
	
	/**
	 * Backup source and store it to destination
	 * 
	 * @param source
	 * @param destination
	 * @throws BackupException
	 */
	public R save( T source, String destination ) throws BackupException;
	
	/**
	 * Restore destination from source
	 * 
	 * @param source
	 * @param device
	 * @param meta
	 * @throws BackupException
	 */
	public void restore( String source, T device, IConfig meta ) throws BackupException;
	
	/**
	 * Method to notify engine about no needing to process given device
	 * backup.
	 * @param device
	 */
	public void exclude( IDevice device );
	
	/**
	 * Method to check need of processing backup on the specified device.
	 * @param device
	 * @return
	 */
	public boolean isExcluded( IDevice device );
	
	/**
	 * Method to exclude device by UUID
	 * 
	 * @param uuid
	 * @return
	 */
	public void exclude( UUID uuid );
	
	/**
	 * Method to check need of processing backup on specified 
	 * by UUID device.
	 * 
	 * @param uuid
	 * @return
	 */
	public boolean isExcluded( UUID uuid );
	
	/**
	 * Method to check need of proceeding specified operation.
	 * @param operation
	 * @return
	 */
	public boolean isExcluded( Object operation );
	
	/**
	 * Method to notify engine about no needing to proceed specified
	 * operation.
	 * 
	 * @param operation
	 */
	public void exclude( Object operation );
	
	/**
	 * Method to notify engine to not process devices with given files system
	 *
	 * @param filesystem
	 */
	public void exclude( StorageFilesystem filesystem );
	
	/**
	 * Method to check that specified files system not excluded.
	 * 
	 * @param system
	 * @return
	 */
	public boolean isExcluded( StorageFilesystem system );
	
	/**
	 * Ignore exceptions mode (only for tests purpouses!!!)
	 */
	public void makeSilent( boolean value );
	
	/**
	 * Is current engine work in sinlent mode
	 */
	public boolean isSilent();

}
