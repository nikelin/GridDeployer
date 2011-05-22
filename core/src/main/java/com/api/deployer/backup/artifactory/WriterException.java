package com.api.deployer.backup.artifactory;

public class WriterException extends Exception {
	private static final long serialVersionUID = -6164106425239334795L;

	public WriterException() {
		this(null);
	}
	
	public WriterException( String message ) {
		this(message, null);
	}
	
	public WriterException( String message, Throwable e ) {
		super(message, e);
	}
	
}
