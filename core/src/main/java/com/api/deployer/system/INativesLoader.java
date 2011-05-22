package com.api.deployer.system;

import java.util.Collection;

public interface INativesLoader {

	public Collection<String> getNatives();
	
	public void setNatives( Collection<String> libraries );
	
	public void reloadNatives();
	
	public void loadNatives();
	
}
