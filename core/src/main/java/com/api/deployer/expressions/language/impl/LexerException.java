package com.api.deployer.expressions.language.impl;

public class LexerException extends Exception {
	private static final long serialVersionUID = -1200873924481630727L;

	public LexerException() {
		this(null);
	}
	
	public LexerException( String message ) {
		this(message, null);
	}
	
	public LexerException( String message, Throwable e ) {
		super(message, e);
	}
	
}
