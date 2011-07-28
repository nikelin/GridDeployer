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
import com.api.deployer.system.ISystemFacade;

public class StorageBackupFacade implements IStorageBackupFacade {
	private ISystemFacade system;
	private Map<StorageFilesystem, IBackupEngine<IStorageDevicePartition, IPartitionBackupResult>> engines;
	private IBackupEngine<IStorageDriveDevice, IDriveBackupResult> driveBackupEngine;
	private IBackupEngine<IStorageDevicePartition, IBackupResult> grubBackupEngine;
	private IBackupEngine<IStorageDevicePartition, IPartitionBackupResult> genericBackupEngine;
	private ApplicationContext context;
	private boolean silent;
	
	public StorageBackupFacade( ISystemFacade facade ) {
		this.system = facade;
	}
	
	public void setGrubBackupEngine( IBackupEngine<IStorageDevicePartition, IBackupResult> engine ) {
		this.grubBackupEngine = engine;
	}
	
	@SuppressWarnings("unchecked")
	public IBackupEngine<IStorageDevicePartition, IBackupResult> getGrubBackupEngine() {
		if ( this.grubBackupEngine != null ) {
			return this.grubBackupEngine;
		}
		
		if ( this.getContext().containsBean("grubBackupEngine") ) {
			this.grubBackupEngine = this.getContext().getBean("grubBackupEngine", IBackupEngine.class );
			this.grubBackupEngine.makeSilent( this.isSilent() );
			return this.grubBackupEngine;
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public IBackupEngine<IStorageDriveDevice, IDriveBackupResult> getDriveBackupEngine() {
		if ( this.driveBackupEngine != null ) {
			return this.driveBackupEngine;
		}
		
		this.driveBackupEngine = this.getContext().getBean("driveBackupEngine", IBackupEngine.class );
		this.driveBackupEngine.makeSilent(this.isSilent());
		
		return this.driveBackupEngine;
	}
	
	@SuppressWarnings("unchecked")
	public IBackupEngine<IStorageDevicePartition, IPartitionBackupResult> getGenericBackupEngine() {
		if ( this.genericBackupEngine != null ) {
			return this.genericBackupEngine;
		}
		
		if ( this.getContext().containsBean("genericBackupEngine") ) {
			this.genericBackupEngine = this.getContext().getBean("genericBackupEngine", IBackupEngine.class );
			this.genericBackupEngine.makeSilent(this.isSilent());
			return this.genericBackupEngine;
		}
		
		return null;
	}
	
	public void setDriveBackupEngine( IBackupEngine<IStorageDriveDevice, IDriveBackupResult> driveBackupEngine ) {
		this.driveBackupEngine = driveBackupEngine;
	}
	
	protected ISystemFacade getSystem() {
		return this.system;
	}
	
	@Override
	public void addEngine( StorageFilesystem filesystem, IBackupEngine<IStorageDevicePartition, IPartitionBackupResult> engine ) {
		this.engines.put( filesystem, engine );
	}
	
	@Override
	public void setEngines( Map<StorageFilesystem, IBackupEngine<IStorageDevicePartition, IPartitionBackupResult>> engines ) {
		this.engines = engines;
	}
	
	@Override
	public Map<StorageFilesystem, IBackupEngine<IStorageDevicePartition, IPartitionBackupResult>> getEngines() {
		return this.engines;
	}
	
	@Override
	public IBackupEngine<IStorageDevicePartition, IBackupResult> selectGrubEngine( IStorageDevicePartition partition ) {
		return this.getGrubBackupEngine();
	}
	
	@Override
	public IBackupEngine<IStorageDriveDevice, IDriveBackupResult> selectEngine( IStorageDriveDevice device ) 
				throws BackupException {
		return this.getDriveBackupEngine();
	}
	
	@Override
	public IBackupEngine<IStorageDevicePartition, IPartitionBackupResult> selectEngine( IStorageDevicePartition partition )
				throws BackupException {
		IBackupEngine<IStorageDevicePartition, IPartitionBackupResult> engine = this.getEngines().get( partition.getFilesystem() );
		if ( engine == null ) {
			return this.getGenericBackupEngine();
		}
		
		engine.makeSilent( this.isSilent() );
		
		return engine;
	}

	protected ApplicationContext getContext() {
		return this.context;
	}
	
	@Override
	public void setContext(ApplicationContext context) {
		this.context = context;
	}
	
	public void makeSilent( boolean value ) {
		this.silent = value;
	}
	
	public boolean isSilent() {
		return this.silent;
	}

}
