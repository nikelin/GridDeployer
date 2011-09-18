package com.api.deployer.jobs.requests;

import com.redshape.daemon.jobs.AbstractJob;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: 4/24/11
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class PerformanceRequestJob extends AbstractJob {

    public PerformanceRequestJob( UUID agentId ) {
        super( agentId );
    }

}
