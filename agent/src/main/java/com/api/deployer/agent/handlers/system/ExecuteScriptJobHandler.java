package com.api.deployer.agent.handlers.system;

import com.api.deployer.jobs.system.ExecuteScriptJob;
import com.api.deployer.system.ISystemFacade;
import com.api.deployer.system.scripts.IScriptExecutor;
import com.redshape.daemon.jobs.handlers.AbstractJobHandler;
import com.redshape.daemon.jobs.handlers.HandlingException;
import com.redshape.daemon.jobs.result.IJobResult;
import com.redshape.daemon.jobs.result.JobResult;

import java.io.IOException;
import java.util.UUID;

/**
 * @author nikelin
 * @date 26/04/11
 * @package com.api.deployer.agent.handlers.system
 */
public class ExecuteScriptJobHandler extends AbstractJobHandler<ExecuteScriptJob, IJobResult> {
	private ISystemFacade facade;

    public ExecuteScriptJobHandler( ISystemFacade facade ) {
        this.facade = facade;
    }

	public ISystemFacade getFacade() {
		return facade;
	}

	@Override
    protected IJobResult createJobResult(UUID jobId) {
        return new JobResult( jobId );
    }

    @Override
    public IJobResult handle(ExecuteScriptJob job) throws HandlingException {
        String command = job.getCommand();
        if ( command == null || command.isEmpty() ) {
            throw new HandlingException();
        }

        IScriptExecutor executor = this.getFacade().getConsole().createExecutor( job.getCommand() );

        try {
            IJobResult result = this.createJobResult(job.getJobId());;
            result.setAttribute( ExecuteScriptJob.Attributes.Result, executor.execute() );

            return result;
        } catch ( IOException e ) {
            throw new HandlingException( e.getMessage(), e );
        }
    }

    @Override
    public void cancel() throws HandlingException {
        this.getFacade().getConsole().stopScripts(this);
    }

    public Integer getProgress() throws HandlingException {
        return 0;
    }
}
