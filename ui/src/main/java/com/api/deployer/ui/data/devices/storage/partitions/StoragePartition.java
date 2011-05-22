package com.api.deployer.ui.data.devices.storage.partitions;

import com.api.deployer.ui.data.devices.AbstractDevice;

/**
 * @author nikelin
 * @date 26/04/11
 * @package com.api.deployer.ui.data.devices.storage.partitions
 */
public class StoragePartition extends AbstractDevice {

    public StoragePartition() {
        super();
    }

    public String getPath() {
        return this.get( StoragePartitionModel.PATH );
    }

    public void setPath( String path ) {
        this.set( StoragePartitionModel.PATH, path );
    }

    @Override
    public String toString() {
        return this.getPath();
    }

}
