package com.api.deployer.ui.components.system.windows;

import java.awt.Dialog.ModalExclusionType;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.api.deployer.jobs.result.IJobResult;
import com.api.deployer.ui.connector.DeployAgentConnector;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.UnhandledUIException;
import com.redshape.ui.actions.InteractionAction;
import com.redshape.ui.events.AppEvent;
import com.redshape.ui.events.EventType;
import com.redshape.ui.events.IEventHandler;
import com.redshape.ui.utils.UIRegistry;

public class JobProgressMonitor extends JFrame {
	private static final long serialVersionUID = 5129379919333018311L;
	
	private JProgressBar progressBar;
	private UUID descriptor;
	private Timer timer = new Timer(true);
	
	public JobProgressMonitor( UUID descriptor ) {
		super();
		
		this.descriptor = descriptor;
		
		this.buildUI();
		this.configUI();
		this.initListeners();
		this.startTimer();
	}
	
	protected DeployAgentConnector getConnector() {
		return UIRegistry.getContext().getBean( DeployAgentConnector.class );
	}
	
	protected UUID getDescriptor() {
		return this.descriptor;
	}
	
	protected void initListeners() {
		Dispatcher.get().addListener(
			DeployAgentConnector.Event.Job.Canceled,
			new IEventHandler() {
				@Override
				public void handle(AppEvent event) {
					JobProgressMonitor.this.stop();
					
					JOptionPane.showMessageDialog( UIRegistry.getRootContext(), "Job has been canceled!");
					
					JobProgressMonitor.this.setVisible(false);
				}
			}
		);
	}
	
	protected void buildUI() {
		JPanel progressPanel = new JPanel();
		progressPanel.setLayout( new BoxLayout( progressPanel, BoxLayout.Y_AXIS ) );
		
		this.progressBar = new JProgressBar( JProgressBar.HORIZONTAL, 0, 100 );
		this.progressBar.setStringPainted(true);
		this.progressBar.setIndeterminate(true);
		this.progressBar.setString("In progress... Wait.");
		this.progressBar.setVisible(true);
		progressPanel.add( this.progressBar );
		
		this.add( progressPanel );
		
		Box buttonsBox = Box.createHorizontalBox();
		buttonsBox.add(new JButton(
			new InteractionAction(
				"Hide",
				new IEventHandler() {
					@Override
					public void handle(AppEvent event) {
						JobProgressMonitor.this.setVisible(false);
					}
				}
			)
		) );
		
		buttonsBox.add( new JButton(
			new InteractionAction(
				"Cancel",
				new IEventHandler() {
					@Override
					public void handle( AppEvent event ) {
						if ( JOptionPane.OK_OPTION 
								== JOptionPane.showConfirmDialog(
										UIRegistry.getRootContext(), "Do you really want to cancel current job?") ) {
							Dispatcher.get().forwardEvent(
								new AppEvent(
									DeployAgentConnector.Event.Job.Cancel,
									JobProgressMonitor.this.getDescriptor()
								)
							);
							
							JobProgressMonitor.this.stop();
						}
					}
				}
			)
		) );
		progressPanel.add( buttonsBox );
		this.add(progressPanel);
	}
	
	protected void configUI() {
		this.setTitle("Processing job...");
		this.setSize( 300, 65 );
		this.setResizable(false);
		this.setModalExclusionType( ModalExclusionType.TOOLKIT_EXCLUDE );
	}
	
	protected void stop() {
		this.timer.cancel();
		this.setVisible(false);
	}
	
	protected void startTimer() {
		this.timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				UUID jobId = JobProgressMonitor.this.getDescriptor();
				DeployAgentConnector connector = JobProgressMonitor.this.getConnector();
				
				try {
					if ( connector.isComplete(jobId) ) {
						EventType type;
						if ( !connector.isFailed(jobId) ) {
							type = DeployAgentConnector.Event.Job.Complete;
						} else {
							type = DeployAgentConnector.Event.Job.Fail;
						}
						
						Dispatcher.get().forwardEvent( new AppEvent(type, jobId ) );
						
						JobProgressMonitor.this.stop();
					} 
				} catch ( RemoteException e  ) {
					throw new UnhandledUIException("Job status tracking exception!", e);
				}
			}
		}, 0, 150);
	}
	
}
