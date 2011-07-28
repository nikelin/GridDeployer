package com.api.deployer.ui.components.scheduler;

import com.api.deployer.jobs.IJob;
import com.api.deployer.jobs.activation.JobActivationProfile;
import com.api.deployer.ui.components.scheduler.windows.ConfigurationWindow;
import com.api.deployer.ui.components.scheduler.windows.ConfigurationsWindow;
import com.api.deployer.ui.components.scheduler.windows.JobWindow;
import com.api.deployer.ui.components.scheduler.windows.ScheduledWindow;
import com.api.deployer.ui.components.scheduler.windows.details.JobDetailsWindow;
import com.api.deployer.ui.components.system.windows.JobProgressMonitor;
import com.api.deployer.ui.connector.DeployAgentConnector;
import com.api.deployer.ui.data.jobs.Job;
import com.redshape.ui.application.AbstractController;
import com.redshape.ui.application.UnhandledUIException;
import com.redshape.ui.application.annotations.Action;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.views.ViewException;
import com.redshape.ui.windows.swing.ISwingWindowsManager;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class SchedulingController extends AbstractController {

	public SchedulingController() {
		super();
	}
	
	protected void initEvents() {
		this.registerEvent( SchedulerComponent.Events.Views.Job.New );
		this.registerEvent( SchedulerComponent.Events.Views.Job.Added );
		this.registerEvent( SchedulerComponent.Events.Views.Jobs );
        this.registerEvent( SchedulerComponent.Events.Views.Configurations );
        this.registerEvent( SchedulerComponent.Events.Views.Scheduled );
		this.registerEvent( SchedulerComponent.Events.Action.ProcessJobs );
	}

	protected void initViews() {
	}

	protected DeployAgentConnector getConnector() {
		return UIRegistry.getContext().getBean( DeployAgentConnector.class );
	}

    @Action( eventType = "SchedulerComponent.Events.Views.Configurations")
    public void configurationsListView( AppEvent event ) {
        UIRegistry.<ISwingWindowsManager>getWindowsManager().open(ConfigurationsWindow.class);
    }

    @Action( eventType = "SchedulerComponent.Events.Views.Scheduled")
    public void scheduledListView( AppEvent event ) {
        UIRegistry.<ISwingWindowsManager>getWindowsManager().open(ScheduledWindow.class);
    }

    @Action( eventType = "SchedulerComponent.Events.Views.Job.Details" )
    public void jobDetailsView( AppEvent event ) {
        JobDetailsWindow window = new JobDetailsWindow( event.<Job>getArg(0) );
        window.setVisible(true);
    }

	@Action( eventType = "SchedulerComponent.Events.Action.ProcessJobs")
	public void onProcessAction( AppEvent event ) {
		if ( !this.getConnector().isConnected() ) {
			throw new UnhandledUIException("Cannot process request operation " +
					"without active connection to server!");
		}

		Collection<Job> jobs = event.getArg(0);
		if ( jobs.size() == 0 ) {
			return;
		}

        JobActivationProfile profile = event.getArg(1);

		for ( UUID result : this.scheduleJobs( jobs, profile ) ) {
			UIRegistry.<ISwingWindowsManager>getWindowsManager().open( new JobProgressMonitor(result) );
		}
	}

	protected Collection<UUID> scheduleJobs( Collection<Job> jobs, JobActivationProfile profile ) {
		Collection<UUID> results = new HashSet<UUID>();


		final Collection<IJob> resultCollection = new HashSet<IJob>();
		for ( Job job : jobs ) {
			IJob jobObject = job.getJob();
			if ( jobObject == null || job.getTarget() == null ) {
				continue;
			}

			jobObject.setAgentId( job.getTarget().getId() );

			resultCollection.add( jobObject );
		}

		try {
			this.getConnector().scheduleJobs(resultCollection, profile);
		} catch ( RemoteException e ) {
			throw new UnhandledUIException("Deploy server interaction exception!", e);
		}

		return results;
	}

	@Action( eventType = "SchedulerComponent.Events.Views.Job.New")
	public void onNewJobAction( AppEvent event ) {
		UIRegistry.<ISwingWindowsManager>getWindowsManager().open(JobWindow.class);
	}

	@Action( eventType = "SchedulerComponent.Events.Views.Jobs")
	public void jobsViewAction( AppEvent event ) throws ViewException {
		UIRegistry.<ISwingWindowsManager>getWindowsManager().open(ConfigurationWindow.class);
	}

}
