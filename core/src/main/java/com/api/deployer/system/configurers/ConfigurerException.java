package com.api.deployer.system.configurers;

/**
 * @author nikelin
 * @date 25/04/11
 * @package com.api.deployer.system.configurers
 */
public class ConfigurerException extends Exception {

    public ConfigurerException() {
        this(null);
    }

    public ConfigurerException( String message ) {
        this(message, null);
    }

    public ConfigurerException( String message, Throwable e ) {
        super(message, e);
    }

}
