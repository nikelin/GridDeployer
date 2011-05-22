package com.api.deployer.ui.data.devices.storage.partitions;

import com.api.deployer.ui.data.devices.AbstractDeviceModel;

/**
 * @author nikelin
 * @date 26/04/11
 * @package com.api.deployer.ui.data.devices.storage.partitions
 */
public class StoragePartitionModel extends AbstractDeviceModel {

    public static final String PATH = "path";

    public StoragePartitionModel() {
        super();

        this.addField( PATH )
            .setTitle("Path");
    }

    @Override
    public StoragePartition createRecord() {
        return new StoragePartition();
    }

}
