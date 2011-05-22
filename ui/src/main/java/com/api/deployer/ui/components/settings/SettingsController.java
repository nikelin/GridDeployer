package com.api.deployer.ui.components.settings;

import com.api.deployer.ui.components.settings.views.LogConsoleWindow;
import com.redshape.ui.AbstractController;
import com.redshape.ui.annotations.Action;
import com.redshape.ui.events.AppEvent;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.views.ViewException;
import com.redshape.ui.windows.swing.ISwingWindowsManager;

public class SettingsController extends AbstractController {

	public SettingsController() {
		super();	
	}
	
	protected void initEvents() {
		this.registerEvent( SettingsComponent.Events.Views.LogsConsole );
	}
	
	protected void initViews() {
	}
	
	@Action( eventType = "SettingsComponent.Events.Views.LogsConsole" )
	public void logsConsoleView( AppEvent event ) {
		UIRegistry.<ISwingWindowsManager>getWindowsManager().open( LogConsoleWindow.class );
	}
	
	@Action( eventType = "SettingsComponent.Events.Views.PXE" )
	public void pxeConfigurationView( AppEvent event ) {
		
	}
	
}
