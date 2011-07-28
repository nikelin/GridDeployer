package com.api.deployer.backup.compression;

import java.util.Collection;
import java.util.HashSet;

import com.redshape.utils.IEnum;

public class CompressionMethod implements IEnum<CompressionMethod> {
	private String method;
	
	protected CompressionMethod( String method ) {
		this.method = method;
	}
	
	public String name() {
		return this.method;
	}
	
	public static final CompressionMethod Bzip2 = new CompressionMethod("bzip2");
	
	public static final CompressionMethod Gzip = new CompressionMethod("gzip");
	
	public static final CompressionMethod Zip = new CompressionMethod("zip");
	
	public static final CompressionMethod Tar = new CompressionMethod("tar");
	
	public static final CompressionMethod Lzo = new CompressionMethod("lzo");
	
	private static final Collection<CompressionMethod> METHODS = new HashSet<CompressionMethod>();
	
	public static void addCompressionMethod( CompressionMethod method ) {
		METHODS.add( method );
	}
	
	static {
		METHODS.add( Bzip2 );
		METHODS.add( Gzip );
		METHODS.add( Zip );
		METHODS.add( Tar );
		METHODS.add( Lzo );
	}
	
	public static CompressionMethod valueOf( String method ) {
		for( CompressionMethod registeredMethod : METHODS ) {
			if ( registeredMethod.name().equals(method) ) {
				return registeredMethod;
			}
		}
		
		return null;
	}
	
	public static CompressionMethod[] values() {
		return METHODS.toArray( new CompressionMethod[METHODS.size()] );
	}
	
	@Override
	public boolean equals( Object object ) {
		if ( object instanceof CompressionMethod && object != null ) {
			return ( (CompressionMethod) object ).name().equals( this.name() );
		}
		
		return super.equals( object );
	}
	
	@Override
	public int hashCode() {
		return this.name().hashCode();
	}
	
}
