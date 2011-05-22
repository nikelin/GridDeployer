package com.api.deployer.backup.diff;

import java.util.Collection;
import java.util.HashSet;

public class DiffMethod {
	private String method;
	
	protected DiffMethod( String method ) {
		this.method = method;
	}
	
	public String name() {
		return this.method;
	}
	
	public static final DiffMethod XDelta = new DiffMethod("xdelta");
	
	public static final DiffMethod XDiff = new DiffMethod("xdiff");
	
	private static final Collection<DiffMethod> METHODS = new HashSet<DiffMethod>();
	
	public static void addDiffMethod( DiffMethod method ) {
		METHODS.add(method);
	}
	
	public static DiffMethod valueOf( String name ) {
		for ( DiffMethod method : METHODS ) {
			if ( method.name().equals( name ) ) {
				return method;
			}
		}
		
		return null;
	}
	
	@Override
	public boolean equals( Object object ) {
		if ( object instanceof DiffMethod && object != null ) {
			return  ( (DiffMethod) object ).name().equals( this.name() );
		}
		
		return super.equals( object );
	}
	
	@Override
	public int hashCode() {
		return this.name().hashCode();
	}
	
}
