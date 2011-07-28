package com.api.deployer.notifications;

import java.util.Date;

/**
 * @author nikelin
 * @date 21/04/11
 * @package com.api.deployer.notifications
 */
public class Notification implements INotification {
	private NotificationType type;
	private String subject;
	private String message;
	private Date date;

	public Notification() {
		this( NotificationType.INFO );
	}

	public Notification( NotificationType type ) {
		this(type, null, null );
	}

    public Notification( NotificationType type, String subject, String message ) {
        this.type = type;
        this.subject = subject;
        this.message = message;
        this.date = new Date();
    }

	@Override
	public void setType( NotificationType type ) {
		this.type = type;
	}

	@Override
	public NotificationType getType() {
		return this.type;
	}

	@Override
	public void setSubject( String value ) {
		this.subject = value;
	}

	@Override
	public String getSubject() {
		return this.subject;
	}

	@Override
	public void setMessage( String message ) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public void setDate( Date date ) {
		this.date = date;
	}

	@Override
	public Date getDate() {
		return this.date;
	}
}
