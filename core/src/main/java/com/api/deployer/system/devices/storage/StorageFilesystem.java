package com.api.deployer.system.devices.storage;

import java.io.Serializable;

public class StorageFilesystem implements Serializable {
	private static final long serialVersionUID = -7195996196888345962L;
	
	private String code;
	
	protected StorageFilesystem( String code ) {
		this.code = code;
	}
	
	public final static StorageFilesystem EXT2 = new StorageFilesystem("StorageFilesystem.EXT2");
	public final static StorageFilesystem EXT3 = new StorageFilesystem("StorageFilesystem.EXT3");
	public final static StorageFilesystem EXT4 = new StorageFilesystem("StorageFilesystem.EXT4");
	public final static StorageFilesystem NTFS = new StorageFilesystem("StorageFilesystem.NTFS");
	public final static StorageFilesystem SWAP = new StorageFilesystem("StorageFilesystem.SWAP");
	public final static StorageFilesystem GENERIC = new StorageFilesystem("StorageFilesystem.GENERIC");
	public final static StorageFilesystem VFAT = new StorageFilesystem("StorageFilesystem.VFAT");
	
	public static final StorageFilesystem[] SYSTEMS = new StorageFilesystem[] {
		EXT2, EXT3, EXT4, NTFS, SWAP, GENERIC, VFAT
	};
	
	public String getCode() {
		return this.code;
	}
	
	public static StorageFilesystem valueOf( String value ) {
		for ( StorageFilesystem system : SYSTEMS ) {
			if ( system.code.equals( value ) ) {
				return system;
			}
		}
		
		return null;
	}
	
	@Override
	public boolean equals( Object obj ) {
		if ( obj != null && ( obj instanceof StorageFilesystem) ) {
			return ( ( StorageFilesystem) obj ).getCode().equals( this.getCode() ); 
		}
		
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return this.getCode().hashCode();
	}
	
}
