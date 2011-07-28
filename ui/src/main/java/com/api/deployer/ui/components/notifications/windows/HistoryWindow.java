package com.api.deployer.ui.components.notifications.windows;

import com.api.deployer.ui.data.notifications.Notification;
import com.api.deployer.ui.data.notifications.NotificationsStore;
import com.redshape.ui.application.UnhandledUIException;
import com.redshape.ui.application.events.handlers.WindowCloseHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.data.IStore;
import com.redshape.ui.data.adapters.swing.TableAdapter;
import com.redshape.ui.utils.UIRegistry;

import javax.swing.*;

/**
 * @author nikelin
 * @date 25/04/11
 * @package com.api.deployer.ui.components.notifications.windows
 */
public class HistoryWindow extends JFrame {
    private IStore<Notification> store;
    private TableAdapter<Notification> list;

    public HistoryWindow() {
        super();

        this.configUI();
        this.buildUI();
    }

    protected void buildUI() {
        try {
            this.store = UIRegistry.getStoresManager().getStore(NotificationsStore.class);
        } catch ( InstantiationException e ) {
            throw new UnhandledUIException( e.getMessage(), e );
        }

        this.setLayout( new BoxLayout( this.getContentPane(), BoxLayout.Y_AXIS ) );
        this.add( this.createListPane() );
        this.add( this.createButtonsPane() );
    }

    protected JComponent createListPane() {
        TableAdapter<Notification> table =
                this.list = new TableAdapter<Notification>( this.store );
        JScrollPane pane = new JScrollPane( table );
        table.setFillsViewportHeight(true);
        return pane;
    }

    protected JComponent createButtonsPane() {
        Box box = Box.createHorizontalBox();
        box.add( new JButton(
            new InteractionAction(
                "Close",
                new WindowCloseHandler(this)
            )
        ) );

        return box;
    }

    protected void configUI() {
        this.setSize( 600, 400 );
        this.setTitle("Notifications history");
    }

}
