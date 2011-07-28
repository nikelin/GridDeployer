package com.api.deployer.io.transport.mounters;

import com.api.deployer.io.transport.IDestination;

public interface IDestinationMountingFacade {

	public <T extends IDestination> void addMounter( IDestinationMounter<T> mounter );

	public <T extends IDestination> IDestinationMounter<T> selectMounter( T destination )
		throws MountException;
	
}
