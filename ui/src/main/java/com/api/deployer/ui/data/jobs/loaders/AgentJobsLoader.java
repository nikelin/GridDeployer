package com.api.deployer.ui.data.jobs.loaders;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import com.api.deployer.ui.connector.DeployAgentConnector;
import com.api.deployer.ui.data.jobs.Job;
import org.springframework.context.ApplicationContext;

import com.api.deployer.execution.IExecutorDescriptor;
import com.api.deployer.jobs.IJob;
import com.redshape.ui.data.loaders.AbstractDataLoader;
import com.redshape.ui.data.loaders.LoaderException;
import com.redshape.ui.utils.UIConstants;
import com.redshape.ui.utils.UIRegistry;

public class AgentJobsLoader extends AbstractDataLoader<Job> {

	@Override
	protected List<Job> doLoad() throws LoaderException {
		try {
			DeployAgentConnector connector = this.getConnector();
			Map<UUID, IExecutorDescriptor> executors = connector.getConnectedExecutors();
			
			List<Job> result = new ArrayList<Job>();
			for ( UUID agentId : executors.keySet() ) {
				Iterator<IJob> jobsIterator = connector.getJobs(agentId).values().iterator();
				while( jobsIterator.hasNext() ) {
					IJob nativeJob = jobsIterator.next();
					if ( nativeJob != null ) {
						Job job = new Job();
						job.setId( nativeJob.getId() );
						result.add(job);
					}
				}
			}
			
			return result;
		} catch ( RemoteException e ) {
			throw new LoaderException( e.getMessage(), e );
		}
	}
	
	private DeployAgentConnector getConnector() {
		return UIRegistry.<ApplicationContext>get( 
							UIConstants.System.SPRING_CONTEXT 
						  )
						 .getBean( DeployAgentConnector.class );
	}

}
