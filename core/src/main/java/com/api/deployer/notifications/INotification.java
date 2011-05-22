package com.api.deployer.notifications;

import java.io.Serializable;
import java.util.Date;

/**
 * @author nikelin
 * @date 21/04/11
 * @package com.api.deployer.notifications
 */
public interface INotification extends Serializable {

	public void setType( NotificationType type );

	public NotificationType getType();

	public void setSubject( String value );

	public String getSubject();

	public void setMessage( String value );

	public String getMessage();

	public void setDate( Date date );

	public Date getDate();

}
