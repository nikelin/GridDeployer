package com.api.deployer.system.devices;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: semichevskiy
 * Date: 2/17/11
 * Time: 4:01 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IDevice extends Serializable {

	public void setName( String name );
	
	public String getName();
	
	public void setUUID( UUID uuid );
	
	public UUID getUUID();
	
	public void setPath( String path );
	
	public String getPath();

}
