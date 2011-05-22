package com.api.deployer.ui.components.scheduler.windows;

import com.api.deployer.ui.components.scheduler.SchedulerComponent;
import com.api.deployer.ui.components.scheduler.windows.details.JobDetailsWindow;
import com.api.deployer.ui.components.scheduler.windows.details.ResultDetailsWindow;
import com.api.deployer.ui.connector.DeployAgentConnector;
import com.api.deployer.ui.data.jobs.Job;
import com.api.deployer.ui.data.jobs.JobStore;
import com.api.deployer.ui.data.jobs.loaders.ScheduleJobsLoader;
import com.api.deployer.ui.data.jobs.results.JobResult;
import com.api.deployer.ui.data.jobs.results.JobResultsStore;
import com.api.deployer.ui.data.jobs.results.loaders.JobResultsLoader;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.UnhandledUIException;
import com.redshape.ui.actions.InteractionAction;
import com.redshape.ui.data.IStore;
import com.redshape.ui.data.adapters.swing.TableAdapter;
import com.redshape.ui.data.loaders.LoaderException;
import com.redshape.ui.data.loaders.policies.RefreshPolicy;
import com.redshape.ui.events.AppEvent;
import com.redshape.ui.events.IEventHandler;
import com.redshape.ui.events.handlers.WindowCloseHandler;
import com.redshape.ui.utils.UIRegistry;

import javax.swing.*;
import java.rmi.RemoteException;

/**
 * @author nikelin
 * @date 25/04/11
 * @package com.api.deployer.ui.components.scheduler.windows
 */
public class ScheduledWindow extends JFrame {
    private TableAdapter<Job> jobsList;
    private TableAdapter<JobResult> resultsList;
    private IStore<Job> store;
    private IStore<JobResult> resultsStore;

    public ScheduledWindow() {
        super();

        this.configUI();
        this.buildUI();
    }

    protected DeployAgentConnector getConnector() {
        return UIRegistry.getContext().getBean( DeployAgentConnector.class );
    }

    protected void buildUI() {
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        JTabbedPane pane = new JTabbedPane();
        pane.add( "Processing", this.createListPane() );
        pane.add( "Results", this.createResultsPane() );
        this.add(pane);
        this.add( this.createButtonsPane() );
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

    protected JComponent createResultsPane() {
        JPanel pane = new JPanel();
        pane.setLayout( new BoxLayout( pane, BoxLayout.Y_AXIS ) );
        pane.add( this.createResultsList() );
        pane.add( this.createResultsControlPane() );
        return pane;
    }

    protected JComponent createResultsList() {
        this.resultsStore = new JobResultsStore(
                new RefreshPolicy<JobResult>( new JobResultsLoader( ), 5000 ) );

        this.resultsList = new TableAdapter(this.resultsStore);
        JScrollPane pane = new JScrollPane(this.resultsList);
        this.resultsList.setFillsViewportHeight(true);
        return pane;
    }

    protected void onResultDetails() {
        ResultDetailsWindow window = new ResultDetailsWindow( this.resultsList.getSelectedRecord() );
        window.setVisible(true);
    }

    protected void onJobDetails() {
        JobDetailsWindow window = new JobDetailsWindow( this.jobsList.getSelectedRecord() );
        window.setVisible(true);
    }

    protected JComponent createResultsControlPane() {
        Box box = Box.createHorizontalBox();

        box.add( new JButton(
            new InteractionAction(
                "Refresh",
                new IEventHandler() {
                    @Override
                    public void handle(AppEvent event) {
                        try {
                            ScheduledWindow.this.resultsStore.load();;
                        } catch ( LoaderException e ) {
                            throw new UnhandledUIException( e.getMessage(), e );
                        }
                    }
                }
            )
        ) );

        box.add( new JButton(
            new InteractionAction(
                "Details",
                new IEventHandler() {
                    @Override
                    public void handle(AppEvent event) {
                        ScheduledWindow.this.onResultDetails();
                    }
                }
            )
        ) );

        return box;
    }

    protected JComponent createListPane() {
        JPanel pane = new JPanel();
        pane.setLayout( new BoxLayout( pane, BoxLayout.Y_AXIS ) );
        pane.add(this.createJobsList());
        pane.add(this.createControlPanel());
        return pane;
    }

    protected JComponent createControlPanel() {
        Box box = Box.createHorizontalBox();

        box.add( new JButton(
            new InteractionAction(
                "Refresh",
                new IEventHandler() {
                    @Override
                    public void handle(AppEvent event) {
                        try {
                            ScheduledWindow.this.store.load();
                        } catch ( LoaderException e ) {
                            throw new UnhandledUIException( e.getMessage(), e );
                        }
                    }
                }
            )
        ));

        box.add( new JButton(
            new InteractionAction(
                "Stop",
                new IEventHandler() {
                    @Override
                    public void handle(AppEvent event) {
                        ScheduledWindow.this.onStop();
                    }
                }
            )
        ) );

        box.add( new JButton(
            new InteractionAction(
                "New",
                new IEventHandler() {
                    @Override
                    public void handle(AppEvent event) {
                        Dispatcher.get().forwardEvent(
                            SchedulerComponent.Events.Views.Jobs
                        );
                    }
                }
            )
        ) );

        box.add( new JButton(
            new InteractionAction(
                "Pause",
                new IEventHandler() {
                    @Override
                    public void handle(AppEvent event) {
                        ScheduledWindow.this.onPause();
                    }
                }
            )
        ) );

        box.add( new JButton(
            new InteractionAction(
                "Details",
                new IEventHandler() {
                    @Override
                    public void handle(AppEvent event) {
                        ScheduledWindow.this.onJobDetails();
                    }
                })
        ) );

        box.add( new JButton(
            new InteractionAction(
                "Resume",
                new IEventHandler() {
                    @Override
                    public void handle(AppEvent event) {
                        ScheduledWindow.this.onResume();
                    }
                }
            )
        ));

        return box;
    }

    protected void onStopConfirmed() {
        try {
            this.getConnector().cancelJob(this.jobsList.getSelectedRecord().getId());
        } catch ( RemoteException e ) {
            throw new UnhandledUIException("Unable to stop selected job", e );
        }
    }

    protected void onStop() {
        UIRegistry.getNotificationsManager().ask(
            "Do you really want to stop selected job?",
            new IEventHandler() {
                @Override
                public void handle(AppEvent event) {
                    ScheduledWindow.this.onStopConfirmed();
                }
            },
            null );
    }

    protected void onPauseConfirmed() {
        try {
            this.getConnector().pauseJob( this.jobsList.getSelectedRecord().getId() );
        } catch ( RemoteException e ) {
            throw new UnhandledUIException( e.getMessage(), e );
        }
    }

    protected void onPause() {
        UIRegistry.getNotificationsManager().ask(
            "Do you really want to pause selected job?",
            new IEventHandler() {
                @Override
                public void handle(AppEvent event) {
                    ScheduledWindow.this.onPauseConfirmed();
                }
            },
            null
        );
    }

    protected void onResume() {
        try {
            this.getConnector().resumeJob(this.jobsList.getSelectedRecord().getId());
        } catch ( RemoteException e ) {
            throw new UnhandledUIException( e.getMessage(), e );
        }
    }

    protected JComponent createJobsList() {
        this.jobsList = new TableAdapter( this.store = new JobStore(
                new RefreshPolicy<Job>( new ScheduleJobsLoader(), 5000 ) ) );
        JScrollPane pane = new JScrollPane( this.jobsList );
        this.jobsList.setFillsViewportHeight(true);
        return pane;
    }

    protected void configUI() {
        this.setTitle("Scheduled jobs");
        this.setSize( 500, 300 );
    }

}
