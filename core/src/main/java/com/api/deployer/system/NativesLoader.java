package com.api.deployer.system;

import java.util.Collection;
import java.util.HashSet;

public class NativesLoader implements INativesLoader {
	private Collection<String> libraries = new HashSet<String>();
	
	public NativesLoader() {}
	
	@Override
	public Collection<String> getNatives() {
		return this.libraries;
	}
	
	@Override
	public void setNatives( Collection<String> libraries ) {
		this.libraries = libraries;
	}
	
	@Override
	public void reloadNatives() {
		
	}
	
	@Override
	public void loadNatives() {
		for ( String library : this.getNatives() ){
			Runtime.getRuntime().loadLibrary(library);
		}
	}
	
}
