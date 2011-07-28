package com.api.deployer.services;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.rmi.server.RMIServerSocketFactory;

import javax.net.ServerSocketFactory;

import org.apache.log4j.Logger;

public class ServerFactory implements RMIServerSocketFactory, Serializable {
	private static final long serialVersionUID = 1430493102430287284L;

	private static final Logger log = Logger.getLogger( ServerFactory.class );
	
	//TODO
	private String host;
	private Integer maxConnections;
	
	public ServerFactory( String host, Integer maxConnections ) {
		this.host = host;
		this.maxConnections = maxConnections;
	}
	
	@Override
	public ServerSocket createServerSocket(int port) throws IOException {
		log.info("Starting repository on host: " + this.host + ":" + port + ":m(" + this.maxConnections + ")" );
		InetAddress address = InetAddress.getByName( this.host );
		return ServerSocketFactory.getDefault()
		  .createServerSocket( 
			  	port, 
			  	this.maxConnections,
			  	address
		  	);
	}
	
}
