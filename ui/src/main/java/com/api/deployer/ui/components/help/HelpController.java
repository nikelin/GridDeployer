package com.api.deployer.ui.components.help;

import com.redshape.ui.application.AbstractController;

import com.api.deployer.ui.components.help.windows.AboutWindow;
import com.redshape.ui.application.AbstractController;
import com.redshape.ui.application.annotations.Action;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.windows.swing.ISwingWindowsManager;

import javax.swing.*;

public class HelpController extends AbstractController {
	
	public HelpController() {
		super();
	}
	
	@Override
	protected void initViews() {
		
	}
	
	@Override
	protected void initEvents() {
		this.registerEvent( HelpComponent.Event.View.About );
		this.registerEvent( HelpComponent.Event.View.Contents );
		this.registerEvent( HelpComponent.Event.View.Update );
	}
	
	@Action( eventType = "HelpComponent.Event.View.About" )
	public void aboutView( AppEvent event ) {
		UIRegistry.<ISwingWindowsManager>getWindowsManager()
				  .open( AboutWindow.class );
	}
	
	@Action( eventType = "HelpComponent.Event.View.Contents" )
	public void contentsView( AppEvent event ) {
		JOptionPane.showMessageDialog( UIRegistry.getRootContext(), "Not implemented yet.");
	}
	
	@Action( eventType = "HelpComponent.Event.View.Update" )
	public void updateView( AppEvent event ) {
		JOptionPane.showMessageDialog( UIRegistry.getRootContext(), "Not implemented yet.");
	}
	
}
