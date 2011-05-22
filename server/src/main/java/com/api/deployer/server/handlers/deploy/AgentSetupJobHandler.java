package com.api.deployer.server.handlers.deploy;

import com.api.commons.XMLHelper;
import com.api.commons.config.ConfigException;
import com.api.commons.config.IConfig;
import com.api.commons.config.IWritableConfig;
import com.api.commons.config.XMLConfig;
import com.api.commons.net.auth.AuthenticatorException;
import com.api.commons.net.auth.impl.SimpleCredentials;
import com.api.commons.net.io.IInteractorsFactory;
import com.api.commons.net.io.INetworkInteractor;
import com.api.commons.net.io.NetworkInteractionException;
import com.api.commons.net.io.NetworkNode;
import com.api.deployer.jobs.deploy.AgentSetupJob;
import com.api.deployer.jobs.handlers.AbstractAwareJobHandler;
import com.api.deployer.jobs.handlers.HandlingException;
import com.api.deployer.jobs.result.IJobResult;
import com.api.deployer.jobs.result.JobResult;
import com.api.deployer.system.ISystemFacade;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.ConnectionException;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.TransportException;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * @author nikelin
 * @date 27/04/11
 * @package com.api.deployer.server.handlers.deploy
 */
public class AgentSetupJobHandler extends AbstractAwareJobHandler<AgentSetupJob, IJobResult> {

    public AgentSetupJobHandler() {
        this(null);
    }

    public AgentSetupJobHandler( ISystemFacade facade ) {
        super(facade);
    }

    @Override
    protected IJobResult createJobResult(UUID jobId) {
        return new JobResult(jobId);
    }

    protected IInteractorsFactory getInteractorsFactory() {
        return this.getContext().getBean( IInteractorsFactory.class );
    }

    protected IConfig getConfig() {
        return this.getContext().getBean( IConfig.class );
    }

    @Override
    public IJobResult handle(AgentSetupJob job) throws HandlingException {
        try {
            String agentSetupPath = this.getConfig().get("system")
                                                       .get("setup")
                                                       .get("agentPath")
                                                       .value();
            if ( agentSetupPath == null
                    || agentSetupPath.isEmpty() ) {
                throw new HandlingException("Path to agent setup packages not defined!");
            }

            File agentSetup = new File( agentSetupPath );
            if ( !agentSetup.exists() || !agentSetup.canRead() ) {
                throw new HandlingException("Setup path not exists or read access denied");
            }

            SSHClient client = this.createConnection( job );

            String path = job.getUri().getPath();
            if ( path == null || path.isEmpty() ) {
                throw new HandlingException("Setup path must be defined!");
            }

            this.configureAgent(job, agentSetupPath);

            this.processUpload( client, agentSetup, path );

            String afterSetupScript = job.getAfterSetupScript();
            if ( afterSetupScript != null && !afterSetupScript.isEmpty() ) {
                client.startSession().exec( afterSetupScript );
            }

            this.startRemoteAgent( path, job, client );

            return this.createJobResult( job.getId() );
        } catch ( UnknownHostException e ) {
            throw new HandlingException( "Specified workstation not reachable", e );
        } catch ( InstantiationException e ) {
            throw new HandlingException( "Unexpected exception", e );
        } catch ( AuthenticatorException e ) {
            throw new HandlingException( "Authentication failed", e );
        } catch ( NetworkInteractionException e ) {
            throw new HandlingException( "Interaction with remote station failed", e );
        } catch ( ConnectionException e ) {
            throw new HandlingException( "SSH connection failed", e );
        } catch ( TransportException e ) {
            throw new HandlingException( "Unknown SSH transport exception", e );
        } catch ( IOException e ) {
            throw new HandlingException("Unknown I/O exception", e );
        } catch ( ConfigException e ) {
            throw new HandlingException("Unable to retrieve configuration attribute", e );
        }
    }

    protected void configureAgent( AgentSetupJob job, String path )
            throws IOException, ConfigException, HandlingException {
        File config = new File( path + "/resources/config.xml");
        if ( !config.exists() || !config.canWrite() || !config.canRead() ) {
            throw new HandlingException("Unable to configure agent before setup");
        }

        URI deployServerURI = job.getDeployServerURI();
        if ( deployServerURI == null ) {
            throw new HandlingException("Server destination not provided!");
        }

        XMLConfig configData = new XMLConfig( this.getContext().getBean( XMLHelper.class),
                                          config );

        final IWritableConfig serverNode = (IWritableConfig) configData.get("server");
        final IWritableConfig agentNode = (IWritableConfig) configData.get("agent");

        ( (IWritableConfig) serverNode.get("host") ).set( deployServerURI.getHost() );
        ( (IWritableConfig) serverNode.get("port") ).set(
                String.valueOf(deployServerURI.getPort()) );
        ( (IWritableConfig) serverNode.get("service") ).set(deployServerURI.getPath());
        ( (IWritableConfig) agentNode.get("host") ).set( job.getUri().getHost() );
        ( (IWritableConfig) agentNode.get("port") ).set(String.valueOf(job.getAgentPort()));
        ( (IWritableConfig) agentNode.get("service") ).set(job.getAgentServiceName());

        XMLConfig.writeConfig( config, configData );
    }

    protected SSHClient createConnection( AgentSetupJob job )
        throws UnknownHostException,
               InstantiationException,
               HandlingException, NetworkInteractionException, AuthenticatorException {
        URI connectionURI = job.getUri();

        NetworkNode networkNode = new NetworkNode( InetAddress.getByName( connectionURI.getHost() ) );

        INetworkInteractor<?> interactor = this.getInteractorsFactory()
                .findInteractor("ssh", networkNode );

        interactor.connect( new SimpleCredentials("ssh", job.getUser(), job.getPassword() ) );

        return (SSHClient) interactor.getRawConnection();
    }

    protected void processUpload( SSHClient client, File agentSetup, String targetPath )
            throws IOException {
        SFTPClient sftp = client.newSFTPClient();

        for ( String part : agentSetup.list() ) {
            File partFile = new File( agentSetup.getCanonicalPath() + File.separator + part );
            if ( partFile.isDirectory() ) {
                sftp.mkdir( targetPath + File.separator + part );
                this.processUpload( client, partFile, targetPath + File.separator + part );
            } else {
                sftp.put( agentSetup.getCanonicalPath() + File.separator + part,
                    targetPath + File.separator + part );
            }
        }

        sftp.close();
    }

    protected void startRemoteAgent( String targetPath, AgentSetupJob job, SSHClient client )
        throws IOException {
        Session session = client.startSession();
        session.exec("cd " + targetPath );
        session.exec("screen -dmS agent java -jar " +
                "agent.jar -Djava.security.policy=resources/rmi.policy " +
                "resources/context.xml config");
        session.close();
    }

    @Override
    public void cancel() throws HandlingException {
        this.getSystem().getConsole().stopScripts(this);
    }

    @Override
    public Integer getProgress() throws HandlingException {
        throw new UnsupportedOperationException("Operation not implemented");
    }
}
