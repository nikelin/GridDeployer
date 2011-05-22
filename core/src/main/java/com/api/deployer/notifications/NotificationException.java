package com.api.deployer.notifications;

/**
 * @author nikelin
 * @date 21/04/11
 * @package com.api.deployer.notifications
 */
public class NotificationException extends Exception {

	public NotificationException() {
		this(null);
	}

	public NotificationException( String message ) {
		this(message, null);
	}

	public NotificationException( String message, Throwable e ) {
		super(message, e);
	}

}
