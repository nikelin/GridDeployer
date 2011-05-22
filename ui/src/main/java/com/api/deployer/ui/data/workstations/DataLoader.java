package com.api.deployer.ui.data.workstations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.ApplicationContext;

import com.api.deployer.execution.IExecutorDescriptor;
import com.api.deployer.ui.connector.DeployAgentConnector;
import com.redshape.ui.data.loaders.AbstractDataLoader;
import com.redshape.ui.data.loaders.LoaderException;
import com.redshape.ui.utils.UIConstants;
import com.redshape.ui.utils.UIRegistry;

public class DataLoader extends AbstractDataLoader<Workstation> {
	private DeployAgentConnector connector;
	
	public DataLoader() {
		this.init();
	}
	
	protected void init() {
		this.connector = this.getConnector();
	}
	
	private DeployAgentConnector getConnector() {
		return UIRegistry.<ApplicationContext>get( 
								UIConstants.System.SPRING_CONTEXT 
						  )
						 .getBean( DeployAgentConnector.class );
	}
	
	@Override
	protected List<Workstation> doLoad() throws LoaderException {
		try {
			Map<UUID, IExecutorDescriptor> executors = this.connector.getConnectedExecutors();
			if ( executors == null ) {
				return new ArrayList<Workstation>();
			}
			
			List<Workstation> result = new ArrayList<Workstation>();
			for ( UUID executorId : executors.keySet() ) {
				IExecutorDescriptor description = executors.get(executorId);
				
				String hostname = this.connector.getHostname(executorId);
				if ( hostname == null ) {
					hostname = "Unknown";
				}
				
				Workstation station = new Workstation( executorId.toString() );
				station.setId( executorId );
				station.set( WorkstationModel.DEVICES, this.connector.getDevices( executorId ) );
				station.set( WorkstationModel.NAME, executorId );
				station.set( WorkstationModel.HOSTNAME, hostname );
				station.set( WorkstationModel.DESCRIPTOR, description );
				
				result.add(station);
			}
			
			return result;
		} catch ( Throwable e ) {
			throw new LoaderException("Load exception", e );
		}
	}

}
