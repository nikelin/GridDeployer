package com.api.deployer.ui.data.notifications;

import com.redshape.ui.data.AbstractModelType;

/**
 * @author nikelin
 * @date 27/04/11
 * @package com.api.deployer.ui.data.notifications
 */
public class NotificationModel extends AbstractModelType {

    public static final String SUBJECT = "subject";
    public static final String TYPE = "type";
    public static final String MESSAGE = "message";
    public static final String DATE = "date";

    public NotificationModel() {
        super( Notification.class );

        this.addField( SUBJECT )
            .setTitle("Subject");

        this.addField( TYPE )
            .setTitle("Type");

        this.addField( MESSAGE )
            .setTitle("Message");

        this.addField( DATE )
            .setTitle("Date");
    }

    @Override
    public Notification createRecord() {
        return new Notification();
    }

}
