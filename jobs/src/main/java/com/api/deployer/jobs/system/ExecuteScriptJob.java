package com.api.deployer.jobs.system;

import com.redshape.bindings.annotations.Bindable;
import com.redshape.bindings.annotations.BindableAttributes;
import com.redshape.daemon.jobs.AbstractJob;
import com.redshape.daemon.jobs.result.JobResultAttribute;

import java.util.UUID;

/**
 * @author nikelin
 * @date 25/04/11
 * @package com.api.deployer.jobs.system
 */
public class ExecuteScriptJob extends AbstractJob {

    public static class Attributes extends JobResultAttribute {

        protected Attributes( String code ) {
            super(code);
        }

        public static final Attributes Result = new Attributes("ExecuteScriptJob.Attributes.Result");
    }

    @Bindable( name = "Script body", attributes = { BindableAttributes.LONGTEXT } )
    private String command;

    public ExecuteScriptJob() {
        this(null);
    }

    public ExecuteScriptJob( UUID agentId ) {
        super(agentId);
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand( String command ) {
        this.command = command;
    }

}
