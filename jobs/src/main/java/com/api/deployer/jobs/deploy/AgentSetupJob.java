package com.api.deployer.jobs.deploy;

import com.api.deployer.jobs.AbstractJob;
import com.api.deployer.jobs.JobScope;
import com.redshape.bindings.annotations.Bindable;
import com.redshape.bindings.annotations.BindableAttributes;

import java.net.URI;
import java.util.UUID;

/**
 * @author nikelin
 * @date 27/04/11
 * @package com.api.deployer.jobs.deploy
 */
public class AgentSetupJob extends AbstractJob {
    @Bindable( name = "Connection URI" )
    private URI uri;

    @Bindable( name = "User" )
    private String user;

    @Bindable( name = "Password" )
    private String password;

    @Bindable( name = "Deploy server URI" )
    private URI deployServerURI;

    @Bindable( name = "Port" )
    private Integer agentPort;

    @Bindable( name = "Service name" )
    private String agentServiceName;

    @Bindable( name = "Execute after setup (ssh)", attributes = {BindableAttributes.LONGTEXT} )
    private String afterSetupScript;

    public AgentSetupJob() {
        this(null);
    }

    public AgentSetupJob(UUID agentId) {
        super(agentId, JobScope.SERVER);
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAfterSetupScript() {
        return afterSetupScript;
    }

    public void setAfterSetupScript(String afterSetupScript) {
        this.afterSetupScript = afterSetupScript;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public URI getDeployServerURI() {
        return deployServerURI;
    }

    public void setDeployServerURI(URI deployServerURI) {
        this.deployServerURI = deployServerURI;
    }

    public Integer getAgentPort() {
        return agentPort;
    }

    public void setAgentPort(Integer agentPort) {
        this.agentPort = agentPort;
    }

    public String getAgentServiceName() {
        return agentServiceName;
    }

    public void setAgentServiceName(String agentServiceName) {
        this.agentServiceName = agentServiceName;
    }
}
