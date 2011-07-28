package com.api.deployer.backup;

import java.util.Collection;
import java.util.HashSet;

public class CompressionType {
	public static Collection<CompressionType> types = new HashSet<CompressionType>();
	
	private String type;
	
	protected CompressionType( String type ) {
		this.type = type;
	}
	
	public String type() {
		return this.type;
	}
	
	public static void registerType( CompressionType type ) {
		types.add(type);
	}
	
	public static CompressionType valueOf( String type ) {
		for ( CompressionType registeredType : types ) {
			if ( registeredType.type().equals( type ) ) {
				return registeredType;
			}
		}
		
		throw new IllegalArgumentException("type not founded");
	}
	
}
