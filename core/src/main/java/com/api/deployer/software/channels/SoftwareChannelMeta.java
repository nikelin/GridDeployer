package com.api.deployer.software.channels;

import java.util.Date;
import java.util.Map;

/**
 * @author nikelin
 * @date 22/04/11
 * @package com.api.deployer.software.channels
 */
public class SoftwareChannelMeta implements ISoftwareChannelMeta {
    private String origin;
    private String label;
    private String version;
    private String description;
    private String[] architectures;
    private Date date;
    private Map<String, String> md5checksums;
    private Map<String, String> sha1checksums;

    private ISoftwareChannel channel;

    public SoftwareChannelMeta( ISoftwareChannel channel ) {
        this.channel = channel;
    }

    public ISoftwareChannel getChannel() {
        return this.channel;
    }

    @Override
    public String getOrigin() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getLabel() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getVersion() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getDescription() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String[] getArchitectures() {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Date getDate() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<String, String> getMD5Checksum() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<String, String> getSHA1Checksum() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
