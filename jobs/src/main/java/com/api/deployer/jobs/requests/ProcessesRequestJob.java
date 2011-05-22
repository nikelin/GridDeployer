package com.api.deployer.jobs.requests;

import com.api.deployer.jobs.AbstractJob;
import com.api.deployer.jobs.result.JobResultAttribute;
import com.redshape.bindings.annotations.Bindable;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: 4/24/11
 * Time: 2:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProcessesRequestJob extends AbstractJob {

    public static class Attribute extends JobResultAttribute {

        protected Attribute( String code) {
            super(code);
        }

        public static final Attribute LIST = new Attribute("ProcessesRequestJob.Attribute.LIST");

    }

    @Bindable( name = "User" )
    private String user;

    public ProcessesRequestJob( UUID agentId ) {
        super( agentId );
    }

    public void setUser( String user ) {
        this.user = user;
    }

    public String getUser() {
        return this.user;
    }

}
