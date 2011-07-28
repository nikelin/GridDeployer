package com.api.deployer.jobs;

import java.rmi.RemoteException;

public class JobException extends RemoteException {
	private static final long serialVersionUID = -5891904691227534638L;

	public JobException() {
		super();
	}
	
	public JobException( String message ) {
		super(message);
	}
	
	public JobException( String message, Throwable cause ) {
		super(message, cause);
	}
	
}
