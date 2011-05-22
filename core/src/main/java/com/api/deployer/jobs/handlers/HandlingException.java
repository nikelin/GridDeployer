package com.api.deployer.jobs.handlers;

public class HandlingException extends Exception {
	private static final long serialVersionUID = 7392912967697908465L;

	public HandlingException() {
		super();
	}
	
	public HandlingException( String message ) {
		super(message);
	}
	
	public HandlingException( String message, Throwable e ) {
		super(message, e);
	}

}
