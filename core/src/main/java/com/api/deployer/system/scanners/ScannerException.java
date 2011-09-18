package com.api.deployer.system.scanners;

public class ScannerException extends Exception {
	private static final long serialVersionUID = -1294356243314009669L;

	public ScannerException() {
		super();
	}
	
	public ScannerException( String message ) {
		super(message);
	}
	
	public ScannerException( String message, Throwable e ) {
		super(message, e );
	}
	
}
