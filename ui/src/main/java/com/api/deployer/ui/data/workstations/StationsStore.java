package com.api.deployer.ui.data.workstations;

import com.redshape.ui.data.loaders.IDataLoader;
import com.redshape.ui.data.stores.ListStore;

public class StationsStore extends ListStore<Workstation> {

	public StationsStore() {
		this( new DataLoader() );
	}
	
	public StationsStore( IDataLoader<Workstation> loader ) {
		super( new WorkstationModel(), loader);
	}
	
}
