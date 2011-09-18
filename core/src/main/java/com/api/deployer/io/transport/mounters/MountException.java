package com.api.deployer.io.transport.mounters;

public class MountException extends Exception {
	private static final long serialVersionUID = -871634202032504295L;

	public MountException() {
		super();
	}
	
	public MountException( String message ) {
		super(message);
	}
	
	public MountException( String message, Throwable exception ) {
		super(message, exception);
	}
	
}
