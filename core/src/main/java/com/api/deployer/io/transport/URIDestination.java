package com.api.deployer.io.transport;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author nikelin
 * @date 21/04/11
 * @package com.api.deployer.io.transport
 */
public class URIDestination implements IDestination {
	private URI uri;

	public URIDestination() {
		this(null);
	}

	public URIDestination( String scheme, String userInfo,
						   String host, Integer port, String path )
			throws URISyntaxException {
		this( prepareURI( scheme, userInfo, host, port, path ) );
	}

    protected static URI prepareURI( String scheme, String userInfo, String host,
                                        Integer port, String path )
        throws URISyntaxException {
        StringBuilder builder = new StringBuilder();
        if ( scheme != null && !scheme.isEmpty() ) {
            builder.append( scheme )
                   .append("://");
        }

        if ( userInfo != null && !userInfo.isEmpty() ) {
            builder.append( userInfo )
                   .append( "@" );
        }

        builder.append( host );

        if ( port != null ) {
            builder.append(":")
                   .append( port );
        }

        if ( path != null && !path.isEmpty() ) {
            builder.append("/")
                   .append( path );
        }

        return new URI( builder.toString() );
    }

	public URIDestination( URI uri ) {
		this.uri = uri;
	}

	@Override
	public void setURI(URI uri) {
		this.uri = uri;
	}

	@Override
	public URI getURI() throws URISyntaxException {
		return this.uri;
	}
}
