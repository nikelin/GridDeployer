package com.api.deployer.jobs.software;

import com.redshape.daemon.jobs.AbstractJob;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: 4/24/11
 * Time: 4:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewChannelJob extends AbstractJob {

    public NewChannelJob( UUID agentId ) {
        super(agentId);
    }

}
