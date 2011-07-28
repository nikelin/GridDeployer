package com.api.deployer.ui.data.notifications;

import com.redshape.ui.data.loaders.IDataLoader;
import com.redshape.ui.data.stores.ListStore;

/**
 * @author nikelin
 * @date 27/04/11
 * @package com.api.deployer.ui.data.notifications
 */
public class NotificationsStore extends ListStore<Notification> {

    public NotificationsStore() {
        this(null);
    }

    public NotificationsStore( IDataLoader<Notification> loader ) {
        super( new NotificationModel(), loader );
    }

}
