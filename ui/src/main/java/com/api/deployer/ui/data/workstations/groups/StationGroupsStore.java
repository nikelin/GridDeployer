package com.api.deployer.ui.data.workstations.groups;

import com.redshape.ui.data.loaders.IDataLoader;
import com.redshape.ui.data.stores.ListStore;

public class StationGroupsStore extends ListStore<WorkstationGroup> {

	public StationGroupsStore() {
		this(null);
	}
	
	public StationGroupsStore( IDataLoader<WorkstationGroup> loader ) {
		super( new WorkstationGroupModel(), loader);
	}
	
}
