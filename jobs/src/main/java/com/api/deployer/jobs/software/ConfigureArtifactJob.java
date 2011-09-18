package com.api.deployer.jobs.software;

import com.redshape.daemon.jobs.AbstractJob;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: 4/24/11
 * Time: 4:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConfigureArtifactJob extends AbstractJob {

    public ConfigureArtifactJob( UUID agentId ) {
        super( agentId );
    }

}
