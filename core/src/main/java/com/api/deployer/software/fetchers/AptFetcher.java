package com.api.deployer.software.fetchers;

import com.api.commons.net.fetch.FetcherException;
import com.api.commons.net.fetch.http.jsoup.HttpFetcher;
import com.api.deployer.software.ISoftwareArtifact;
import com.api.deployer.software.channels.ChannelFetchException;
import com.api.deployer.software.channels.ISoftwareChannel;
import com.api.deployer.software.channels.ISoftwareChannelMeta;
import com.api.deployer.software.channels.SoftwareChannelMeta;
import com.api.deployer.system.ISystemFacade;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;

/**
 * @author nikelin
 * @date 22/04/11
 * @package com.api.deployer.software.fetchers
 */
public class AptFetcher implements IChannelFetcher {
    private ISystemFacade facade;

    public AptFetcher( ISystemFacade facade ) {
        this.facade = facade;
    }

    protected ISystemFacade getSystemFacade() {
        return this.facade;
    }

    @Override
    public ISoftwareChannelMeta fetchMeta(ISoftwareChannel channel) throws ChannelFetchException {
        HttpFetcher fetcher = new HttpFetcher();

        ISoftwareChannelMeta result = new SoftwareChannelMeta(channel);

        try {
            String output = fetcher.fetch( this.prepareBaseURI(channel) + "/Release" );

            String[] lines = output.split("\\.");
            for ( String line : lines ) {

            }

            return result;
        } catch ( IOException e ) {
            throw new ChannelFetchException( e.getMessage(), e );
        } catch ( FetcherException e ) {
            throw new ChannelFetchException( e.getMessage(), e );
        }
    }

    @Override
    public Collection<ISoftwareArtifact> fetchArtifacts(ISoftwareChannel channel) throws ChannelFetchException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected String prepareBaseURI( ISoftwareChannel channel ) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append( channel.getHost() )
               .append( "/" )
               .append( channel.getDistributive() )
               .append( "/" )
               .append( channel.getPackage() );

        switch( this.getSystemFacade().getStationArch() ) {
            case 32:
                builder.append("binary-i386");
            break;
            case 64:
                builder.append("binary-amd64");
            break;
            default:
               throw new RuntimeException("Illegal station architecture");
        }

        return builder.toString();
    }
}
