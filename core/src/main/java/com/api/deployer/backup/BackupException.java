package com.api.deployer.backup;

public class BackupException extends Exception {
	private static final long serialVersionUID = -455418769900271733L;

	public BackupException() {
		super();
	}
	
	public BackupException( String message ) {
		super(message);
	}
	
	public BackupException( String message, Throwable e ) {
		super(message, e);
	}
	
}
