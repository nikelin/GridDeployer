package com.api.deployer.jobs.backup;

import java.util.UUID;

import com.api.deployer.system.devices.storage.IStorageDriveDevice;
import com.redshape.bindings.annotations.Bindable;
import com.redshape.bindings.annotations.BindableReader;
import com.redshape.bindings.annotations.BindableWriter;
import com.redshape.bindings.annotations.ElementType;
import com.redshape.bindings.types.BindableType;
import com.redshape.bindings.types.CollectionType;

public class SystemBackupJob extends AbstractBackupJob implements ISystemBackupJob {
	private static final long serialVersionUID = -1017891211966899939L;

	private IStorageDriveDevice deviceId;
	
	private boolean doGrubBackup;
	private boolean doNetworkBackup;
	private boolean doSettingsBackup;
	private boolean doMBRBackup;
	
	@Bindable( name = "Parent job" )
	public SystemBackupJob parentJob;
	
	public SystemBackupJob() {
		this(null);
	}
	
	public SystemBackupJob( UUID agentId ) {
		super( agentId );
	}

	@Bindable( name = "device", type = BindableType.LIST )
	@ElementType( value = IStorageDriveDevice.class, type = CollectionType.CHOICE )
	public IStorageDriveDevice getDevice() {
		return this.deviceId;
	} 
	
	@Override
	public void setDevice( IStorageDriveDevice uuid ) {
		this.deviceId = uuid;
	}
	
	@Override
	@BindableWriter( name = "doNetworkBackup" )
	public void doNetworkBackup( boolean value ) {
		this.doNetworkBackup = value;
	}
	
	@Override
	@Bindable( name = "Do network settings backup?" )
	@BindableReader( name = "doNetworkBackup" )
	public Boolean doNetworkBackup() {
		return this.doNetworkBackup;
	}
	
	@Bindable( name = "Do grub backup?" )
	@BindableReader( name = "doGrubBackup" )
	public Boolean doGrubBackup() {
		return this.doGrubBackup;
	}
	
	@BindableWriter( name = "doGrubBackup" )
	public void doGrubBackup( Boolean value ) {
		this.doGrubBackup = value;
	}
	
	@Override
	public void doSettingsBackup( boolean value ) {
		this.doSettingsBackup = value;
	}

	@Override
	public Boolean doSettingsBackup() {
		return this.doSettingsBackup;
	}

	@Bindable( name = "Do MBR backup?" )
	@BindableReader( name = "doGrubBackup" )
	public Boolean doMBRBackup() {
		return this.doMBRBackup;
	}
	
	@BindableWriter( name = "doGrubBackup" )
	public void doMBRBackup( Boolean value ) {
		this.doMBRBackup = value;
	}
	
}
