package com.api.deployer.ui.components.scheduler.windows;

import com.api.deployer.ui.components.scheduler.SchedulerComponent;
import com.api.deployer.ui.data.jobs.configurations.JobConfiguration;
import com.api.deployer.ui.data.jobs.configurations.JobConfigurationsStore;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.UnhandledUIException;
import com.redshape.ui.actions.InteractionAction;
import com.redshape.ui.data.adapters.swing.TableAdapter;
import com.redshape.ui.events.AppEvent;
import com.redshape.ui.events.IEventHandler;
import com.redshape.ui.events.UIEvents;
import com.redshape.ui.events.handlers.WindowCloseHandler;
import com.redshape.ui.utils.UIRegistry;

import javax.swing.*;
import java.util.Date;

/**
 * @author nikelin
 * @date 27/04/11
 * @package com.api.deployer.ui.components.scheduler.windows
 */
public class ConfigurationsWindow extends JFrame {
    private TableAdapter<JobConfiguration> list;
    private JobConfigurationsStore store;

    public ConfigurationsWindow() {
        super();

        this.buildUI();
        this.configUI();
    }

    protected void buildUI() {
        this.setLayout( new BoxLayout( this.getContentPane(), BoxLayout.Y_AXIS ) );

        try {
            this.store = UIRegistry.getStoresManager().getStore( JobConfigurationsStore.class );
        } catch ( InstantiationException e ) {
            throw new UnhandledUIException( e.getMessage(), e );
        }

        this.add( this.createConfigurationsList() );
        this.add( this.createControlsPane() );
    }

    protected void onRemove() {
        if ( this.list.getRowCount() == 0 ) {
            return;
        }

        this.store.remove( this.list.getSelectedRecord() );
        Dispatcher.get().forwardEvent(UIEvents.Core.Repaint, this.list );
    }

    protected void onProcess() {
        if ( this.list.getRowCount() == 0 ) {
            return;
        }

        final JobConfiguration configuration = this.list.getSelectedRecord();

        UIRegistry.getNotificationsManager().ask(
            String.format(
                "Do you really want to process %d jobs?",
                configuration.getJobsCount()
            ),
            new IEventHandler() {
                @Override
                public void handle(AppEvent event) {
                    ConfigurationsWindow.this.onProcessConfirmed( configuration );
                }
            },
            null
        );

    }

    protected void onProcessConfirmed( JobConfiguration configuration ) {
        configuration.setLastActivated( new Date() );

        Dispatcher.get().forwardEvent(
            SchedulerComponent.Events.Action.ProcessJobs,
            configuration.getItems(),
            configuration.getActivationProfile()
        );

        UIRegistry.getNotificationsManager().info("Configuration successfully activated!");

        Dispatcher.get().forwardEvent(
            UIEvents.Core.Repaint,
            this.list
        );
    }

    protected JComponent createConfigurationsList() {
        this.list = new TableAdapter<JobConfiguration>( this.store );
        JScrollPane pane = new JScrollPane( this.list );
        this.list.setFillsViewportHeight(true);
        return pane;
    }

    protected JComponent createControlsPane() {
        Box box = Box.createHorizontalBox();

        box.add( new JButton(
            new InteractionAction(
                "Process",
                new IEventHandler() {
                    @Override
                    public void handle(AppEvent event) {
                        ConfigurationsWindow.this.onProcess();
                    }
                }
            )
        ) );

        box.add( new JButton(
            new InteractionAction(
                "Remove",
                new IEventHandler() {
                    @Override
                    public void handle(AppEvent event) {
                        ConfigurationsWindow.this.onRemove();
                    }
                }
            )
        ) );

        box.add( new JButton(
            new InteractionAction(
                "Cancel",
                new WindowCloseHandler(this)
            )
        ) );
        return box;
    }

    protected void configUI() {
        this.setSize( 600, 400 );
        this.setTitle("Scheduler configurations");
    }

}
