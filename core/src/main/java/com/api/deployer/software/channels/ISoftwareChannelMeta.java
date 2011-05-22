package com.api.deployer.software.channels;

import java.util.Date;
import java.util.Map;

/**
 * @author nikelin
 * @date 22/04/11
 * @package com.api.deployer.software.channels
 */
public interface ISoftwareChannelMeta {

    public String getOrigin();

    public String getLabel();

    public String getVersion();

    public String getDescription();

    public String[] getArchitectures();

    public Date getDate();

    public Map<String, String> getMD5Checksum();

    public Map<String, String> getSHA1Checksum();

}
