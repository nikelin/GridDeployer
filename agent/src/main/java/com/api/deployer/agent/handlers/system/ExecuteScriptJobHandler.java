package com.api.deployer.agent.handlers.system;

import com.api.deployer.jobs.handlers.AbstractJobHandler;
import com.api.deployer.jobs.handlers.HandlingException;
import com.api.deployer.jobs.result.IJobResult;
import com.api.deployer.jobs.result.JobResult;
import com.api.deployer.jobs.system.ExecuteScriptJob;
import com.api.deployer.system.ISystemFacade;
import com.api.deployer.system.scripts.IScriptExecutor;

import java.io.IOException;
import java.util.UUID;

/**
 * @author nikelin
 * @date 26/04/11
 * @package com.api.deployer.agent.handlers.system
 */
public class ExecuteScriptJobHandler extends AbstractJobHandler<ExecuteScriptJob, IJobResult> {

    public ExecuteScriptJobHandler( ISystemFacade facade ) {
        super(facade);
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

        IScriptExecutor executor = this.getSystem().getConsole().createExecutor( job.getCommand() );

        try {
            IJobResult result = this.createJobResult(job.getId());;
            result.setAttribute( ExecuteScriptJob.Attributes.Result, executor.execute() );

            return result;
        } catch ( IOException e ) {
            throw new HandlingException( e.getMessage(), e );
        }
    }

    @Override
    public void cancel() throws HandlingException {
        this.getSystem().getConsole().stopScripts(this);
    }

    @Override
    public Integer getProgress() throws HandlingException {
        return 0;
    }
}
