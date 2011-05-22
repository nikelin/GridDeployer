package com.api.deployer.jobs.restore;

import java.util.Date;
import java.util.UUID;

import com.api.deployer.io.transport.IDestination;

public class SystemRestoreJob extends AbstractRestoreJob implements ISystemRestoreJob {
	private static final long serialVersionUID = -4921476926367694043L;

	private IDestination destination;
	private Boolean doNetworkRestore;
	private Boolean doSettingsRestore;
	private Boolean doStorageRestore;
	private String deviceId;

	public SystemRestoreJob( UUID agentId) {
		super(agentId);
	}

	public void setDevice( String device ) {
		this.deviceId = device;
	}

	public String getDevice() {
		return this.deviceId;
	}

	public IDestination getDestination() {
		return destination;
	}
	
	public void setDestination(IDestination destination) {
		this.destination = destination;
	}
	
	public Boolean doNetworkRestore() {
		return doNetworkRestore;
	}
	
	public void doNetworkRestore(Boolean doNetworkRestore) {
		this.doNetworkRestore = doNetworkRestore;
	}
	
	public Boolean doSettingsRestore() {
		return doSettingsRestore;
	}
	
	public void doSettingsRestore(Boolean doSettingsRestore) {
		this.doSettingsRestore = doSettingsRestore;
	}
	
	public Boolean doStorageRestore() {
		return doStorageRestore;
	}
	
	public void doStorageRestore(Boolean doStorageRestore) {
		this.doStorageRestore = doStorageRestore;
	}

}
