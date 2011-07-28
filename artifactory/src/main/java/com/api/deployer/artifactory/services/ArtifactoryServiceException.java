package com.api.deployer.artifactory.services;

import java.rmi.RemoteException;

/**
 * @author nikelin
 * @date 13/04/11
 * @package com.api.deployer.artifactory.services
 */
public class ArtifactoryServiceException extends RemoteException {

	public ArtifactoryServiceException() {
		this(null);
	}

	public ArtifactoryServiceException( String message ) {
		this(message, null);
	}

	public ArtifactoryServiceException( String message, Throwable e ) {
		super(message, e);
	}

}
