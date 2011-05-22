package com.api.deployer.system.devices.storage;

import com.api.deployer.backup.IBackupable;
import com.api.deployer.system.devices.IDevice;

/**
 * Created by IntelliJ IDEA.
 * User: semichevskiy
 * Date: 2/17/11
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IStorageDevice extends IDevice, IBackupable {
	
    public void setSize( Long size );
    
    public Long getSize();
    
}
