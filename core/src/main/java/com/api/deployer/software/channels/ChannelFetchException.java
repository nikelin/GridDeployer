package com.api.deployer.software.channels;

/**
 * @author nikelin
 * @date 22/04/11
 * @package com.api.deployer.software
 */
public class ChannelFetchException extends Exception {

    public ChannelFetchException() {
        this(null);
    }

    public ChannelFetchException( String message ) {
        this(message, null);
    }

    public ChannelFetchException( String message, Throwable e ) {
        super(message, e);
    }

}
