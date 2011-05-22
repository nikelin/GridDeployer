package com.api.deployer.ui.data.notifications;

import com.api.deployer.notifications.NotificationType;
import com.redshape.ui.data.AbstractModelData;

import java.util.Date;

/**
 * @author nikelin
 * @date 27/04/11
 * @package com.api.deployer.ui.data.notifications
 */
public class Notification extends AbstractModelData {

    public Notification() {
        super();
    }

    public Date getDate() {
        return this.get( NotificationModel.DATE );
    }

    public void setDate( Date date ) {
        this.set( NotificationModel.DATE, date );
    }

    public String getSubject() {
        return this.get( NotificationModel.SUBJECT );
    }

    public void setSubject( String subject ) {
        this.set(NotificationModel.SUBJECT, subject);
    }

    public String getMessage() {
        return this.get( NotificationModel.MESSAGE );
    }

    public void setMessage( String message ) {
        this.set( NotificationModel.MESSAGE, message );
    }

    public void setType( NotificationType type ) {
        this.set( NotificationModel.TYPE, type );
    }

    public NotificationType getType() {
        return this.get( NotificationModel.TYPE );
    }

}
