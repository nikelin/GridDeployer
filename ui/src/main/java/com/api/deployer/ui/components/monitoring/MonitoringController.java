package com.api.deployer.ui.components.monitoring;

import com.api.deployer.ui.components.monitoring.windows.PerformanceWindow;
import com.api.deployer.ui.components.monitoring.windows.ProcessesWindow;
import com.api.deployer.ui.components.monitoring.windows.StatusWindow;
import com.redshape.ui.application.AbstractController;
import com.redshape.ui.application.annotations.Action;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.windows.swing.ISwingWindowsManager;

/**
 * @author nikelin
 * @date 22/04/11
 * @package com.api.deployer.ui.components.monitoring
 */
public class MonitoringController extends AbstractController {

	public MonitoringController() {
		super();
	}

	@Override
	protected void initEvents() {
		this.registerEvent( MonitoringComponent.Events.Views.Performance );
		this.registerEvent( MonitoringComponent.Events.Views.Processes );
		this.registerEvent( MonitoringComponent.Events.Views.Status );
	}

	@Action( eventType = "MonitoringComponent.Events.Views.Performance" )
	public void performanceView( AppEvent event ) {
		UIRegistry.<ISwingWindowsManager>getWindowsManager().open( PerformanceWindow.class );
	}

	@Action( eventType = "MonitoringComponent.Events.Views.Status" )
	public void statusView( AppEvent event ) {
		UIRegistry.<ISwingWindowsManager>getWindowsManager().open( StatusWindow.class );
	}

	@Action( eventType = "MonitoringComponent.Events.Views.Processes" )
	public void processesView( AppEvent event ) {
		UIRegistry.<ISwingWindowsManager>getWindowsManager().open( ProcessesWindow.class );
	}

	@Override
	protected void initViews() {
	}
}
