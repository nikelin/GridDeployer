package com.api.deployer.ui.components.settings.views;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import org.apache.log4j.spi.LoggingEvent;

import com.api.deployer.ui.utils.ILogsWaiter;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.actions.InteractionAction;
import com.redshape.ui.events.AppEvent;
import com.redshape.ui.events.IEventHandler;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.windows.swing.ISwingWindowsManager;

public class LogConsoleWindow extends JFrame {
	private static final long serialVersionUID = 6757781215658898515L;
	
	/**
	 * Text console messages holder
	 */
	private JTextArea logsConsole;
	
	/**
	 * Log messages formatting pattern
	 */
	public String pattern = "[%s] - [%s] - %s - %s \n";
	
	public LogConsoleWindow() {
		super();
	
		this.buildUI();
		this.initListeners();
		this.configUI();
	}
	
	protected void configUI() {
		this.setAlwaysOnTop(true);
		this.setTitle("Activity console");
		this.setSize( 600, 250 );
	}
	
	protected void initListeners() {
		Dispatcher.get().addListener(
			ILogsWaiter.Events.New,
			new IEventHandler() {
				@Override
				public void handle(AppEvent event) {
					LoggingEvent log = event.getArg(0);
					
					LogConsoleWindow.this.logsConsole
										 .append(
											String.format( 
												LogConsoleWindow.this.getPattern(),
												log.getLevel(),
												log.getClass(),
												new Date( log.timeStamp )
													.toLocaleString(),
												log.getMessage()
											)
										 );
				}
			}
		);
	}
	
	protected void buildUI() {
		this.setLayout( new BoxLayout( this.getContentPane(), BoxLayout.PAGE_AXIS ) );
		
		this.logsConsole = new JTextArea( 10, 40 );
		this.logsConsole.setEnabled(false);
		JScrollPane consoleWrapper = new JScrollPane( this.logsConsole );
		this.add( consoleWrapper );
		
		Box box = Box.createHorizontalBox();
		box.add(
			new JButton(
				new InteractionAction(
					"Close",
					new IEventHandler() {
						@Override
						public void handle( AppEvent event ) {
							UIRegistry.<ISwingWindowsManager>getWindowsManager()
									  .close( LogConsoleWindow.this );
						}
					}
				)
			)
		);
		this.add(box);
	}
	
	public void setPattern( String pattern ) {
		this.pattern = pattern;
	}
	
	public String getPattern() {
		return this.pattern;
	}
	
}
