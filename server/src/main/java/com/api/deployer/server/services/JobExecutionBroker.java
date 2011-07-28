package com.api.deployer.server.services;

import com.api.deployer.execution.services.IDeployServerService;
import com.api.deployer.jobs.IJob;
import com.api.deployer.jobs.JobException;
import com.api.deployer.jobs.JobState;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.rmi.RemoteException;
import java.util.UUID;

/**
 * @author nikelin
 * @date 25/04/11
 * @package com.api.deployer.server.services
 */
public class JobExecutionBroker implements Job {
    private static final Logger log = Logger.getLogger( JobExecutionBroker.class );

    public JobExecutionBroker() {
        super();
    }

    @Override
    public void execute(JobExecutionContext context)
        throws JobExecutionException {
        try {
            IJob job = (IJob) context.getJobDetail().getJobDataMap().get( DeployServerService.ACTUAL_JOB ) ;
            if ( job == null ) {
                return;
            }

            IDeployServerService executor = (IDeployServerService)
                    context.getJobDetail().getJobDataMap().get( DeployServerService.EXECUTION_SERVICE );

            IJob executorJob = executor.getJob( job.getId() );
            executorJob.setState( JobState.PROCESSING );

            if ( executor == null ) {
                throw new JobExecutionException("Executor service referrence not found");
            }

            log.info("Job with ID: " + job.getId() + " being to be activated.");

            try {
                executor.executeJob( job);

                executorJob.setState( JobState.WAITING );
            } catch ( RemoteException e  ) {
                executor.fail( job.getAgentId(), job.getId(), new JobException( e.getMessage() ) );
                log.error( e.getMessage(), e );
                executorJob.setState( JobState.FAILED );
            }

        } catch ( RemoteException e ) {
            throw new JobExecutionException( e.getMessage(), e );
        }
    }

}
