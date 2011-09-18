package com.api.deployer.ui.widgets;

import com.redshape.ui.views.IView;
import com.redshape.ui.views.widgets.AbstractWidget;

import java.awt.*;

public class TasksWidget extends AbstractWidget {
	private static final long serialVersionUID = 4855126566976557847L;

	private IView view;
	
	public TasksWidget() {
		super();
	}
	
	@Override
	public void render( Container container ) {
		this.view.render( container );
	}
	
	@Override
	public void unload( Container container ) {
		this.view.unload( container );
	}
	
	@Override
	public void init() {
		this.view.init();
	}
	
}
