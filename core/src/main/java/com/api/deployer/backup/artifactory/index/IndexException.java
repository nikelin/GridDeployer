package com.api.deployer.backup.artifactory.index;

public class IndexException extends Exception {
	private static final long serialVersionUID = 1334754871071500664L;

	public IndexException() {
		this(null);
	}
	
	public IndexException( String message ) {
		this(message, null);
	}
	
	public IndexException( String message, Throwable e ) {
		super(message, e);
	}
	
}
