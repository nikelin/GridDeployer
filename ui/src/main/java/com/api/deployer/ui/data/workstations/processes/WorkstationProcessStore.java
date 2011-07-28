package com.api.deployer.ui.data.workstations.processes;

import com.redshape.ui.data.loaders.IDataLoader;
import com.redshape.ui.data.stores.ListStore;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: 4/24/11
 * Time: 1:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkstationProcessStore extends ListStore<WorkstationProcess> {

    public WorkstationProcessStore( IDataLoader<WorkstationProcess> loader ) {
        super( new WorkstationProcessModel(), loader );
    }

}
