package com.api.deployer.jobs.backup;

import com.api.deployer.system.devices.storage.IStorageDriveDevice;
import com.redshape.bindings.annotations.Bindable;
import com.redshape.bindings.annotations.BindableReader;
import com.redshape.bindings.annotations.BindableWriter;
import com.redshape.bindings.annotations.ElementType;
import com.redshape.bindings.types.BindableType;
import com.redshape.bindings.types.CollectionType;

import java.util.UUID;

/**
 * @author nikelin
 * @date 14/04/11
 * @package com.api.deployer.jobs.backup
 */
public class StorageDriveBackupJob extends AbstractBackupJob implements IStorageDriveBackupJob {

	@Bindable( type = BindableType.LIST )
	@ElementType( value = IStorageDriveDevice.class, type = CollectionType.CHOICE )
	private IStorageDriveDevice device;

	@Bindable
	private Boolean doMBRBackup;

	@Bindable
	private Boolean doGrubBackup;

	public StorageDriveBackupJob() {
		this(null);
	}

	public StorageDriveBackupJob( UUID agentId ) {
		super(agentId);
	}

	@Override
	public void setDevice(IStorageDriveDevice device) {
		this.device = device;
	}

	@Override
	public IStorageDriveDevice getDevice() {
		return this.device;
	}

	@Override
	@BindableReader( name = "doGrubBackup" )
	public Boolean doGrubBackup() {
		return this.doGrubBackup;
	}

	@Override
	@BindableWriter( name = "doGrubBackup" )
	public void doGrubBackup(Boolean value) {
		this.doGrubBackup = value;
	}

	@Override
	@BindableReader( name = "doMBRBackup" )
	public Boolean doMBRBackup() {
		return this.doMBRBackup;
	}

	@Override
	@BindableWriter( name = "doMBRBackup" )
	public void doMBRBackup(Boolean value) {
		this.doMBRBackup = value;
	}
}
