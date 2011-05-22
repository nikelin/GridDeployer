package com.api.deployer.io.transport.ssh;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

import com.api.deployer.io.transport.IDestination;
import com.redshape.bindings.annotations.Bindable;

/**
 * SSH destination entity
 * 
 * @author nikelin
 */
public class SSHDestination implements IDestination, Serializable {
	private static final long serialVersionUID = -65637330151225739L;
	
	private String host;
	private Integer port;
	private String user;
	private String password;
	private String path;

	public SSHDestination() {
		this(null);
	}
	
	public SSHDestination( URI url ) {
		if ( url != null ) {
			this.setURI(url);
		}
	}
	
	@Bindable( name = "Host" )
	public void setHost( String host ) {
		this.host = host;
	}
	
	public String getHost() {
		return this.host;
	}
	
	@Bindable( name = "Port" )
	public void setPort( Integer port ) {
		this.port = port;
	}
	
	public Integer getPort() {
		return this.port;
	}
	
	@Bindable( name = "User" )
	public void setUser( String user ) {
		this.user = user;
	}
	
	public String getUser() {
		return this.user;
	}
	
	@Bindable( name = "Password" )
	public void setPassword( String password ) {
		this.password = password;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	@Bindable( name = "Path" )
	public void setPath( String path ) {
		this.path = path;
	}
	
	public String getPath() {
		return this.path;
	}

	@Override
	public URI getURI() throws URISyntaxException {
		return new URI("ssh", this.getUserInfo(), this.getHost(), this.getPort(), this.getPath(), null, null );
	}
	
	protected String getUserInfo() {
		if ( this.getUser() != null && !this.getUser().isEmpty() ) {
			return this.getUser() + ":" + this.getPassword();
		}
		
		return "";
	}
	
	@Override
	public String toString() {
		return this.getUser() + "@" + this.getHost() + ":" + this.getPath();
	}

	@Override
	public void setURI(URI url) {
		String host = url.getHost();
		int userInfoSepartor = host.indexOf("@") ;
		this.setHost( host.substring( userInfoSepartor == -1 ? 0 : userInfoSepartor + 1  ) );
		this.setPort( url.getPort() );
		this.setPath( url.getPath() );
		
		if ( url.getHost().contains("@") ) {
			String[] userInfo = host.substring( 0, userInfoSepartor ).split(":");
			this.setUser( userInfo[0] );
			
			if ( userInfo.length > 1 ) {
				this.setPassword( userInfo[1] );
			}
		}
	}
}
