package com.api.deployer.ui.components.workstations.views;

import com.redshape.ui.views.IView;

import com.api.deployer.ui.components.workstations.WorkstationComponent;
import com.api.deployer.ui.data.workstations.Workstation;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.views.IView;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class ConsoleView implements IView {
	private static final long serialVersionUID = 109495635189452028L;
	
	private Workstation record;
	private JComponent component;
	private JTextArea consoleAreaComponent;
	private JTextField commandLineComponent;
	
	public ConsoleView( Workstation record ) {
		this.record = record;
		
		this.initListeners();
	}
	
	protected void initListeners() {
		Dispatcher.get().addListener( 
			WorkstationComponent.Events.Console.ExecuteResult,
			new IEventHandler() {
				@Override
				public void handle(AppEvent event) {
					ConsoleView.this.appendConsoleMessage( event.<String>getArg(0) );
				}
			}
		);
		
		Dispatcher.get().addListener(
			WorkstationComponent.Events.Console.Execute,
			new IEventHandler() {
				
				@Override
				public void handle(AppEvent event) {
					ConsoleView.this.appendConsoleMessage( event.<String>getArg(1) );
				}
			}
		);
	}
	
	@SuppressWarnings("deprecation")
	protected void appendConsoleMessage( String message ) {
		this.consoleAreaComponent.append( new Date().toLocaleString() );
		this.consoleAreaComponent.append( " - ");
		this.consoleAreaComponent.append(message);
		this.consoleAreaComponent.append( "\n" );
	}
	
	@Override
	public void handle(AppEvent event) {
		
	}

	@Override
	public void render(Container component) {
		component.add( this.component );
	}
	
	protected Workstation getRecord() {
		return this.record;
	}

	@Override
	public void init() {
		this.component = new JPanel();
		this.component.setSize( 500, 400 );
		this.component.setLayout( new BoxLayout(this.component, BoxLayout.Y_AXIS ) );
		
		this.component.add( new JLabel("Workstation: " + this.getRecord().getId() ) );
		
		this.consoleAreaComponent = new JTextArea(10, 25);
		this.consoleAreaComponent.setEditable(false);
		JScrollPane consoleAreaWrapper = new JScrollPane(this.consoleAreaComponent);
		this.component.add( consoleAreaWrapper );
		
		JPanel commandLinePanel = new JPanel();
		commandLinePanel.add( this.commandLineComponent = new JTextField(45) );
		
		commandLinePanel.add( new JButton(
			new InteractionAction(
				"Execute",
				new IEventHandler() {
					@Override
					public void handle(AppEvent event) {
						Dispatcher.get().forwardEvent(
							new AppEvent(
								WorkstationComponent.Events.Console.Execute,
								new Object[] { 
									ConsoleView.this.getRecord(),
									ConsoleView.this.commandLineComponent.getText()
								}
							)
						);
					}
				}
			)
		) );
		
		commandLinePanel.add( new JButton(
			new InteractionAction(
				"Back to list",
				new IEventHandler() {
					@Override
					public void handle( AppEvent event ) {
						Dispatcher.get().forwardEvent(
							new AppEvent(
								WorkstationComponent.Events.View.List
							)
						);
					}
				}
			)	
		) );
		
		this.component.add( commandLinePanel );
	}

	@Override
	public void unload(Container component) {
		component.remove( this.component );
	}
	
}
