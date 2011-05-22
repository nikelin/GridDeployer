package com.api.deployer.jobs.backup;

import com.redshape.bindings.annotations.Bindable;
import com.redshape.bindings.annotations.BindableConstructor;
import com.redshape.bindings.annotations.BindableReader;
import com.redshape.bindings.annotations.BindableWriter;

@BindableConstructor( type = SystemBackupJob.class )
public interface ISystemBackupJob extends IStorageDriveBackupJob {
	
	@Bindable( name = "Do network backup?" )
	@BindableWriter( name = "doNetworkBackup" )
	public void doNetworkBackup( boolean value );
	
	@BindableReader( name = "doNetworkBackup" )
	public Boolean doNetworkBackup();
	
	@Bindable( name = "Do settings backup?" )
	@BindableWriter( name = "doSettingsBackup" )
	public void doSettingsBackup( boolean value );
	
	@BindableReader( name = "doSettingsBackup" )
	public Boolean doSettingsBackup();
}
