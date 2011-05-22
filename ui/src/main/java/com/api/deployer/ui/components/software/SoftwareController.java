package com.api.deployer.ui.components.software;

import com.redshape.ui.AbstractController;

public class SoftwareController extends AbstractController {

	public SoftwareController() {
		super();
	}
	
	@Override
	protected void initEvents() {
		this.registerEvent( SoftwareComponent.Events.Views.Channels );
		this.registerEvent( SoftwareComponent.Events.Views.Overview );
		this.registerEvent( SoftwareComponent.Events.Views.Settings );
	}
	
	@Override
	protected void initViews() {
		
	}
	
}
