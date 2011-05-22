package com.api.deployer.ui.widgets;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import com.api.deployer.ui.connector.DeployAgentConnector;
import org.apache.log4j.Logger;

import com.api.deployer.ui.components.scheduler.SchedulerComponent;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.events.AppEvent;
import com.redshape.ui.events.IEventHandler;
import com.redshape.ui.events.UIEvents;
import com.redshape.ui.widgets.AbstractWidget;

public class StatusWidget extends AbstractWidget {
	private static final Logger log = Logger.getLogger( StatusWidget.class );
	private static final long serialVersionUID = 4072703371137953240L;

	private Timer timer = new Timer();

	private Component component;
	private JLabel connectionStatus;
	private JLabel errorStatus;
	private JLabel errorsCounter;
	private JLabel jobsStatus;
	private JLabel memoryUsage;
	
	public StatusWidget() {
		super();
		
		this.buildUI();
		this.configUI();
	}

	protected Timer getTimer() {
		return this.timer;
	}

	protected void configUI() {
		Dispatcher.get().addListener( DeployAgentConnector.Event.Connected, new IEventHandler() {
			@Override
			public void handle(AppEvent event) {
				StatusWidget.this.makeConnected();
			}
		});
		
		Dispatcher.get().addListener( DeployAgentConnector.Event.Disconnected, new IEventHandler() {
			@Override
			public void handle(AppEvent event) {
				StatusWidget.this.makeDisconnected();
			}
		});
	}
	
	protected void makeConnected() {
		this.connectionStatus.setText("Stable");
		this.connectionStatus.setForeground( new Color( 0, 255, 0 ) );
		this.connectionStatus.revalidate();
		this.connectionStatus.repaint();
	}
	
	protected void makeDisconnected() {
		this.connectionStatus.setText("None");
		this.connectionStatus.setForeground( new Color( 255, 0, 0 ) );
		this.connectionStatus.revalidate();
		this.connectionStatus.repaint();
	}
	
	protected void buildUI() {
		JPanel panel = new JPanel();
		
		JPanel jobsStatusPanel = new JPanel();
		jobsStatusPanel.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked( MouseEvent e ) {
				Dispatcher.get().forwardEvent(
					new AppEvent(
						SchedulerComponent.Events.Views.Jobs
					)
				);
			}
		});
		
		jobsStatusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		this.jobsStatus = new JLabel();
		this.jobsStatus.setText("0");
		jobsStatusPanel.add( new JLabel("Active jobs: ") );
		jobsStatusPanel.add( this.jobsStatus );
		jobsStatusPanel.add( this.jobsStatus );
		panel.add( jobsStatusPanel );
		
		
		JPanel connStatusPanel = new JPanel();
		connStatusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		this.connectionStatus = new JLabel();
		this.makeDisconnected();
		connStatusPanel.add( new JLabel("Connection status:"));
		connStatusPanel.add(this.connectionStatus);
		panel.add( connStatusPanel );
		
		JPanel errStatusPanel = new JPanel();
		errStatusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		this.errorStatus = new JLabel("None");
		this.errorStatus.setMaximumSize( new Dimension(200, 20 ) );
		errStatusPanel.add( new JLabel("Error:") );
		errStatusPanel.add( this.errorStatus );
		panel.add( errStatusPanel );
		
		JPanel errorCountPanel = new JPanel();
		errorCountPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		this.errorsCounter = new JLabel("0");
		errorCountPanel.add( new JLabel("Errors:") );
		errorCountPanel.add( this.errorsCounter );
		panel.add(errorCountPanel);

		JPanel memoryUsagePanel = new JPanel();
		this.memoryUsage = new JLabel();
		memoryUsagePanel.add(this.memoryUsage);
		panel.add(memoryUsagePanel);
		
		this.component = panel;	
	}

	protected void updateJobsCount( Integer count ) {
		Integer currently = Integer.valueOf( StatusWidget.this.jobsStatus.getText() );
		
		StatusWidget.this.jobsStatus.setText( String.valueOf( count + currently ) );
		StatusWidget.this.jobsStatus.revalidate();
		StatusWidget.this.jobsStatus.repaint();
	}
	
	@Override
	public void init() {
		this.getTimer().scheduleAtFixedRate( new TimerTask() {
			@Override
			public void run() {
				StatusWidget.this.memoryUsage.setText(String.format(
						"%dMB / %dMB",
						(int) Runtime.getRuntime().totalMemory() / (1024 * 1024),
						(int) Runtime.getRuntime().maxMemory() / (1024 * 1024)
				));
				Dispatcher.get().forwardEvent( UIEvents.Core.Repaint,
						StatusWidget.this );
			}
		}, 0, 500);

		Dispatcher.get().addListener( DeployAgentConnector.Event.Job.Canceled, new IEventHandler() {
			@Override
			public void handle(AppEvent event) {	
				StatusWidget.this.updateJobsCount(-1);
			}
		});
		
		Dispatcher.get().addListener( DeployAgentConnector.Event.Job.Scheduled, new IEventHandler() {
			@Override
			public void handle(AppEvent event) {
				StatusWidget.this.updateJobsCount(1);
			}
		});
		
		Dispatcher.get().addListener( UIEvents.Core.Error, new IEventHandler() {
			@Override
			public void handle(AppEvent event) {
				String errorDescription;
				if ( event.getArg(0) instanceof Throwable ) {
					errorDescription = ( (Throwable) event.getArg(0) ).getMessage();
					log.error( errorDescription, (Throwable) event.getArg(0) );
				} else {
					errorDescription = String.valueOf( event.getArg(0) );
					log.error( errorDescription );
				}
				
				StatusWidget.this.errorStatus.setText( errorDescription );
				StatusWidget.this.errorsCounter.setText(
					String.valueOf(
						Integer.valueOf( StatusWidget.this.errorsCounter.getText() ) + 1
					)
				);
				Dispatcher.get().forwardEvent( UIEvents.Core.Repaint );
			}
		});
	}

	@Override
	public void unload( Container component ) {
		component.remove( this.component );
	}
	
	@Override
	public void render(Container component) {
		component.add( this.component );
	}
	
	
}
