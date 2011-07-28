package com.api.deployer.io.transport.mounters;

import java.util.Collection;
import java.util.HashSet;

import com.api.deployer.io.transport.IDestination;

public class DestinationMountingFacade implements IDestinationMountingFacade {
	private Collection<IDestinationMounter<?>> mounters = new HashSet<IDestinationMounter<?>>();
	
	public void setMounters( Collection<IDestinationMounter<?>> mounters ) {
		this.mounters = mounters;
	}
	
	public Collection<IDestinationMounter<?>> getMounters() {
		return this.mounters;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends IDestination> IDestinationMounter<T> selectMounter( T destination ) {
		try {
			for ( IDestinationMounter<?> mounter : this.mounters ) {
				if ( mounter.isSupportedURI( destination.getURI() ) ) {
					return (IDestinationMounter<T>) mounter;
				}
			}
			
			return null;
		} catch ( Throwable e ) {
			throw new RuntimeException( e.getMessage(), e );
		}
	}

	@Override
	public <T extends IDestination> void addMounter( IDestinationMounter<T> mounter ) {
		this.mounters.add( mounter );
	}
	
}
