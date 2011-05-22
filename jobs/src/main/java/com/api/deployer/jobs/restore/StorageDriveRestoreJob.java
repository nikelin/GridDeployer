package com.api.deployer.jobs.restore;

import java.util.UUID;

import com.api.deployer.io.transport.IDestination;
import com.redshape.bindings.annotations.Bindable;
import com.redshape.bindings.annotations.BindableReader;
import com.redshape.bindings.annotations.BindableWriter;
import com.redshape.bindings.types.BindableType;

public class StorageDriveRestoreJob extends AbstractRestoreJob implements IStorageDriveRestoreJob {
	private static final long serialVersionUID = -1392012561409470297L;

	private UUID device;
	private Boolean doGrubRestore;
	private Boolean doMBRRestore;
	private IDestination destination;

	public StorageDriveRestoreJob(UUID agentId) {
		super(agentId);
	}

	@Override
	@Bindable( name = "Device", type = BindableType.LIST )
	public void setDevice( UUID device ) {
		this.device = device;
	}

	@Override
	public UUID getDevice() {
		return this.device;
	}

	@Override
	@Bindable( name = "Do grub restore?" )
	@BindableReader( name = "doGrubRestore" )
	public Boolean doGrubRestore() {
		return this.doGrubRestore;
	}

	@Override
	@BindableWriter( name = "doGrubRestore" )
	public void doGrubRestore(Boolean value) {
		this.doGrubRestore = value;
	}

	@Override
	@Bindable( name = "Do main record restore?")
	@BindableReader( name = "doMBRRestore" )
	public Boolean doMBRRestore() {
		return this.doMBRRestore;
	}

	@Override
	@BindableWriter( name = "doMBRRestore" )
	public void doMBRRestore(Boolean value) {
		this.doMBRRestore = value;
	}

}
