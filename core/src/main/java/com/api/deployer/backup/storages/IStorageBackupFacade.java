package com.api.deployer.backup.storages;

import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.api.deployer.backup.BackupException;
import com.api.deployer.backup.IBackupEngine;
import com.api.deployer.backup.result.IBackupResult;
import com.api.deployer.backup.result.storages.IDriveBackupResult;
import com.api.deployer.backup.result.storages.IPartitionBackupResult;
import com.api.deployer.system.devices.storage.IStorageDevicePartition;
import com.api.deployer.system.devices.storage.IStorageDriveDevice;
import com.api.deployer.system.devices.storage.StorageFilesystem;

public interface IStorageBackupFacade {

	// TODO: remove from interface
	public void setContext( ApplicationContext context );
	
	public Map<StorageFilesystem, IBackupEngine<IStorageDevicePartition, IPartitionBackupResult>> getEngines();
	
	public void addEngine( StorageFilesystem filesystem, IBackupEngine<IStorageDevicePartition, IPartitionBackupResult> engine );
	
	public void setEngines( Map<StorageFilesystem, IBackupEngine<IStorageDevicePartition, IPartitionBackupResult>> engines );
	
	public IBackupEngine<IStorageDevicePartition, IBackupResult> selectGrubEngine( IStorageDevicePartition partition );
	
	public IBackupEngine<IStorageDriveDevice, IDriveBackupResult> selectEngine( IStorageDriveDevice partition )
			throws BackupException;
	
	public IBackupEngine<IStorageDevicePartition, IPartitionBackupResult> selectEngine( IStorageDevicePartition partition )
			throws BackupException;
	
	/**
	 * Ignore exceptions mode (only for tests purpouses!!!)
	 */
	public void makeSilent( boolean value );
	
	/**
	 * Is current engine work in sinlent mode
	 */
	public boolean isSilent();
}
