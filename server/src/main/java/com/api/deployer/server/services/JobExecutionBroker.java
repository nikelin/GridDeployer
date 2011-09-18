package com.api.deployer.server.services;

import com.api.deployer.execution.services.IDeployServerService;
import com.api.deployer.jobs.JobScope;
import com.redshape.daemon.jobs.IJob;
import com.redshape.daemon.jobs.JobException;
import com.redshape.daemon.jobs.JobStatus;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.rmi.RemoteException;

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

            IJob executorJob = executor.getJob( job.getJobId() );
            executorJob.setState( JobStatus.PROCESSING );

            if ( executor == null ) {
                throw new JobExecutionException("Executor service referrence not found");
            }

            log.info("Job with ID: " + job.getJobId() + " being to be activated.");

            try {
                executor.executeJob(JobScope.SERVER, null, job);

                executorJob.setState( JobStatus.WAITING );
            } catch ( RemoteException e  ) {
                executor.fail( null, job.getJobId(), new JobException( e.getMessage() ) );
                log.error( e.getMessage(), e );
                executorJob.setState( JobStatus.FAILED );
            }

        } catch ( RemoteException e ) {
            throw new JobExecutionException( e.getMessage(), e );
        }
    }

}
