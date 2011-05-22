package com.api.deployer.expressions;

public class EvaluationException extends Exception {
	private static final long serialVersionUID = -5973138377531557857L;

	public EvaluationException() {
		this(null);
	}
	
	public EvaluationException( String message ) {
		this(message, null);
	}
	
	public EvaluationException( String message, Throwable e ) {
		super(message, e);
	}
	
}
