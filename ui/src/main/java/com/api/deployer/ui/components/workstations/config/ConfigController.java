package com.api.deployer.ui.components.workstations.config;

import com.api.deployer.ui.components.workstations.WorkstationComponent;
import com.api.deployer.ui.components.workstations.config.windows.ConfigWindow;

import com.api.deployer.ui.data.workstations.Workstation;
import com.redshape.ui.application.AbstractController;
import com.redshape.ui.application.annotations.Action;
import com.redshape.ui.application.events.AppEvent;

import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.windows.swing.ISwingWindowsManager;

public class ConfigController extends AbstractController {
	
	public ConfigController() {
		super();
	}
	
	@Override
	protected void initViews() {
		
	}
	
	@Override
	protected void initEvents() {
		this.registerEvent( WorkstationComponent.Events.View.Config );
	}
	
	@Action( eventType = "WorkstationComponent.Events.View.Config" )
	public void configView( AppEvent event ) {
		UIRegistry.<ISwingWindowsManager>getWindowsManager()
				  .open( ConfigWindow.class )
				  .setRecord( event.<Workstation>getArg(0) );
	}

	
}
