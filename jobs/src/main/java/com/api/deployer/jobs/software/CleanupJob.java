package com.api.deployer.jobs.software;

import com.api.deployer.jobs.AbstractJob;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: 4/24/11
 * Time: 4:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class CleanupJob extends AbstractJob {

    public CleanupJob( UUID agentId ) {
        super(agentId);
    }
}
