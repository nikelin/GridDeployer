package com.api.deployer.software;

import java.util.UUID;

/**
 * @author nikelin
 * @date 22/04/11
 * @package com.api.deployer.software
 */
public interface ISoftwareArtifactDependency {

    public UUID getSourceId();

    public String getSourceVersion();

    public UUID getTargetId();

    public String getTargetVersion();

}
