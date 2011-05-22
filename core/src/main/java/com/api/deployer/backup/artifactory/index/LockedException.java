package com.api.deployer.backup.artifactory.index;

public class LockedException extends IndexException {
	private static final long serialVersionUID = 7317665422980486274L;

	public LockedException() {
		this(null);
	}
	
	public LockedException( String message ) {
		this(message, null);
	}
	
	public LockedException( String message, Throwable e ) {
		super(message, e);
	}
	
}
