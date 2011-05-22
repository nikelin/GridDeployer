package com.api.deployer.ui.components.connector;

import com.api.deployer.ui.connector.DeployAgentConnector;
import org.apache.log4j.Logger;

import com.redshape.ui.components.AbstractComponent;

public class ConnectorComponent extends AbstractComponent {
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ConnectorComponent.class);
	
	private DeployAgentConnector connector;
	
	public ConnectorComponent() {
		this("component", "Connector" );
	}
	
	public ConnectorComponent(String name, String title) {
		super(name, title, false);
	}

	protected DeployAgentConnector getConnector() {
		return this.connector;
	}
	
	@Override
	public void init() {
		this.addController( new ConnectorController() );
	}

}
