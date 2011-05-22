package com.api.deployer.services;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;

import javax.net.SocketFactory;

public class ClientsFactory implements RMIClientSocketFactory, Serializable {
	private static final Logger log = Logger.getLogger(ClientsFactory.class);
	private static final long serialVersionUID = 2437093004204957760L;

	private String host;
	
	public ClientsFactory( String host ) {
		this.host = host;
	}
	
	@Override
	public Socket createSocket(String host, int port) throws IOException {
		log.info("Client connection: " + host + ":" + port );
		return SocketFactory.getDefault().createSocket( this.host, port );
	}
}
