package com.api.deployer.backup;

import com.api.deployer.backup.result.IBackupResult;
import com.api.deployer.system.ISystemFacade;
import com.api.deployer.system.devices.IDevice;
import com.api.deployer.system.devices.storage.IStorageDevicePartition;
import com.api.deployer.system.devices.storage.StorageFilesystem;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public abstract class AbstractBackupEngine<T extends IBackupable, V extends IBackupResult>
									implements IBackupEngine<T, V> {
	private ISystemFacade system;
	private boolean silentMode;
	private Collection<IDevice> excludedDevices = new HashSet<IDevice>();
	private Collection<Object> excludedOperations = new HashSet<Object>();
	private Collection<UUID> excludedUUIDS = new HashSet<UUID>();
	private Collection<StorageFilesystem> excludedFilesystems = new HashSet<StorageFilesystem>();
	
	public AbstractBackupEngine( ISystemFacade system ) {
		this.system = system;
	}
	
	protected ISystemFacade getSystem() {
		return this.system;
	}
	
	protected abstract V createBackupResult( T source, String finalName );
	
	@Override
	public boolean isExcluded( Object operation ) {
		return this.excludedOperations.contains(operation);
	}
	
	@Override
	public void exclude( Object operation ) {
		this.excludedOperations.add(operation);
	}
	
	@Override
	public boolean isExcluded( IDevice device ) {
		return this.excludedDevices.contains(device);
	}
	
	@Override
	public void exclude( IDevice device ) {
		this.excludedDevices.add(device);
	}
	
	@Override
	public void exclude( UUID uuid ) {
		this.excludedUUIDS.add(uuid);
	}
	
	@Override
	public boolean isExcluded( UUID uuid ) {
		return this.excludedUUIDS.contains(uuid);
	}
	
	//TODO: Refactor
	@Override
	public void exclude( StorageFilesystem filesystem ) {
		this.excludedFilesystems.add(filesystem);
	}
	
	@Override
	public boolean isExcluded( StorageFilesystem system ) {
		return this.excludedFilesystems.contains(system);
	}
	
	@Override
	public boolean isSilent() {
		return this.silentMode;
	}
	
	@Override
	public void makeSilent( boolean value ) {
		this.silentMode = value;
	}
	
	protected String prepareFinalName( IStorageDevicePartition source ) {
		return String.format(
			"%s.%d.aa",
			source.getName(), 
			source.getNumber() 
		);
	}
	
	@Override
	public void stop() throws BackupException {
		this.getSystem().getConsole().stopScripts(this);
	}
}