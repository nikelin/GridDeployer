package com.api.deployer.ui.data.devices.storage.partitions;

import com.api.deployer.ui.data.devices.AbstractDeviceStorage;
import com.redshape.ui.data.loaders.IDataLoader;

/**
 * @author nikelin
 * @date 26/04/11
 * @package com.api.deployer.ui.data.devices.storage.partitions
 */
public class StoragePartitionsStore extends AbstractDeviceStorage<StoragePartition> {

    public StoragePartitionsStore() {
        this(null);
    }

    public StoragePartitionsStore( IDataLoader<StoragePartition> loader ) {
        super( new StoragePartitionModel(), loader);
    }

}
