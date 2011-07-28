package com.api.deployer.ui;

import com.redshape.ui.windows.AbstractMainWindow;

import javax.swing.*;
import java.awt.*;

public class DeployerWindow extends AbstractMainWindow {
	private static final long serialVersionUID = -7872697003134952096L;
	
	@Override
	public JPanel createEastPanel() {
		JPanel panel = new JPanel();
		panel.setAlignmentY( Container.TOP_ALIGNMENT );
		return panel;
	}
	
	@Override
	public JPanel createNorthPanel() {
		return new JPanel();
	}
	
	@Override
	public JPanel createWestPanel() {
		return new JPanel();
	}
	
	@Override
	protected void configUI() {
		this.setTitle("API Deployer Control Console");
		this.setSize(750, 700);
	}

	@Override
	protected JPanel createCenterPanel() {
		JPanel panel = new JPanel();
		panel.setAlignmentY( Container.TOP_ALIGNMENT );
		panel.add( new JLabel() );
		return panel;
	}

	@Override
	protected MenuBar createMenu() {
		return new MenuBar();
	}

	@Override
	protected JPanel createBottom() {
		return new JPanel();
	}

}
