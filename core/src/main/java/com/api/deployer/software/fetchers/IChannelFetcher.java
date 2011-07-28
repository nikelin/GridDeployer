package com.api.deployer.software.fetchers;

import com.api.deployer.software.ISoftwareArtifact;
import com.api.deployer.software.channels.ChannelFetchException;
import com.api.deployer.software.channels.ISoftwareChannel;
import com.api.deployer.software.channels.ISoftwareChannelMeta;

import java.util.Collection;

/**
 * @author nikelin
 * @date 22/04/11
 * @package com.api.deployer.software
 */
public interface IChannelFetcher {

    public ISoftwareChannelMeta fetchMeta( ISoftwareChannel channel ) throws ChannelFetchException;

    public Collection<ISoftwareArtifact> fetchArtifacts( ISoftwareChannel channel ) throws ChannelFetchException;

}
