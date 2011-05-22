package com.api.deployer.artifactory;

import com.api.commons.config.ConfigException;
import com.api.commons.config.IConfig;
import com.api.daemon.AbstractRMIDaemon;
import com.api.daemon.DaemonException;
import com.api.daemon.IDaemonAttributes;
import com.api.deployer.artifactory.services.ArtifactoryService;
import com.api.deployer.backup.artifactory.IArtifactoryFacade;
import com.api.deployer.services.ClientsFactory;
import com.api.deployer.services.ServerFactory;
import org.apache.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author nikelin
 * @date 13/04/11
 * @package com.api.deployer.artifactory
 */
public class ArtifactoryServer extends AbstractRMIDaemon<ArtifactoryServer.Attributes> {
	private final static Logger log = Logger.getLogger(ArtifactoryServer.class);

	public enum Attributes implements IDaemonAttributes {
		HOST,
		PORT,
		MAX_ATTEMPTS,
		SERVICE_NAME,
		MAX_CONNECTIONS,
		INDEX_PATH
	}

	public ArtifactoryServer( String context ) throws DaemonException, ConfigException {
		super(context);

		this.setClientsFactory( new ClientsFactory( this.getHost() ) );
		this.setServerFactory( new ServerFactory( this.getHost(), this.getMaxConnections() ) );
	}

	@Override
	protected void onStarted() throws DaemonException {
		try {
			log.info("Exporting artifactory service...");
			this.exportService( new ArtifactoryService(
				this.getPath(),
				this.<String>getAttribute( Attributes.INDEX_PATH ),
				this.getContext()
			) );
		} catch ( Throwable e ) {
			throw new DaemonException( e.getMessage(), e );
		}
	}

	@Override
	protected ExecutorService createThreadExecutor() {
		return Executors.newFixedThreadPool(10);
	}

	@Override
	public Integer getMaxAttempts() {
		return this.getAttribute( Attributes.MAX_ATTEMPTS );
	}

	@Override
	public String getHost() {
		return this.getAttribute( Attributes.HOST );
	}

	@Override
	public Integer getPort() {
		return this.getAttribute( Attributes.PORT );
	}

	@Override
	public String getPath() {
		return this.getAttribute( Attributes.SERVICE_NAME );
	}

	@Override
	public Integer getMaxConnections() {
		return this.getAttribute( Attributes.MAX_CONNECTIONS );
	}

	@Override
	public void loadConfiguration(IConfig config) throws DaemonException, ConfigException {
		this.setAttribute( Attributes.INDEX_PATH, config.get("artifactory").get("indexPath").value() );

		this.setAttribute( Attributes.HOST, config.get("host").value() );
		this.setAttribute( Attributes.PORT, Integer.valueOf(config.get("port").value()) );
		this.setAttribute( Attributes.MAX_CONNECTIONS, Integer.valueOf( config.get("maxConnections").value() ) );
		this.setAttribute( Attributes.SERVICE_NAME, config.get("serviceName").value() );
	}

	public static void main( String[] args ) {
		if ( args.length < 0 ) {
			throw new IllegalArgumentException("Spring context path not given");
		}

		try {
			ArtifactoryServer server = new ArtifactoryServer( args[0] );

			server.start();
		} catch ( DaemonException e ) {
			log.error("Failed to start daemon", e );
		} catch ( ConfigException e ) {
			log.error("Configuration reading exception", e);
		}

	}

}
