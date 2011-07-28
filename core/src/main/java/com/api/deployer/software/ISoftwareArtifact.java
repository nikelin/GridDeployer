package com.api.deployer.software;

import com.api.deployer.software.channels.ISoftwareChannel;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

/**
 * @author nikelin
 * @date 22/04/11
 * @package com.api.deployer.software
 */
public interface ISoftwareArtifact {

    public UUID getId();

    public String getName();

    public String getDescription();

    public Date getDate();

    public Collection<String> getVersions();

    public Collection<ISoftwareArtifactDependency> getDependencies();

    public ISoftwareChannel getChannel();

}
