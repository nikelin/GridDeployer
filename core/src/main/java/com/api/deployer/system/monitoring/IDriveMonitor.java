package com.api.deployer.system.monitoring;

import com.api.deployer.system.devices.storage.IStorageDriveDevice;

/**
 * @author nikelin
 * @date 22/04/11
 * @package com.api.deployer.system.monitoring
 */
public interface IDriveMonitor extends IMonitor {

	public Float getReadSpeed( IStorageDriveDevice device );

	public Integer getReadAmount( IStorageDriveDevice device );

	public Float getWriteSpeed( IStorageDriveDevice device );

	public Integer getWrittenAmount( IStorageDriveDevice device );

}
