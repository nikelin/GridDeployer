package com.api.deployer.backup.artifactory;

public class ReaderException extends Exception {
	private static final long serialVersionUID = -6416176496503948874L;

	public ReaderException() {
		this(null);
	}
	
	public ReaderException( String message ) {
		this(message, null);
	}
	
	public ReaderException( String message, Throwable exception ) {
		super(message, exception);
	}
	
}
