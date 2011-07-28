package com.api.deployer.software.channels;

import java.net.URI;
import java.util.Date;
import java.util.UUID;

/**
 * @author nikelin
 * @date 22/04/11
 * @package com.api.deployer.software
 */
public interface ISoftwareChannel {

    public UUID getId();

    public String getName();

    public String getHost();

    public String getDistributive();

    public String getPackage();

    public Date getUpdated();

}
