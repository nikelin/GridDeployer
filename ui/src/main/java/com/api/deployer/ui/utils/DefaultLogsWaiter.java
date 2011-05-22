package com.api.deployer.ui.utils;

import java.util.Collection;
import java.util.HashSet;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

import com.redshape.ui.Dispatcher;
public class DefaultLogsWaiter implements ILogsWaiter {
	private Collection<LoggingEvent> logs = new HashSet<LoggingEvent>();
	private Appender appender;
	
	public DefaultLogsWaiter() {
		this.appender = new ConsoleAppender() {
			@Override
			public void doAppend(LoggingEvent event) {
				DefaultLogsWaiter.this.logs.add(event);
				Dispatcher.get().forwardEvent( ILogsWaiter.Events.New, event, this );
			}	
		};
	}
	
	public Collection<LoggingEvent> getLogs() {
		return this.logs;
	}
	
	public void start() {
		Logger.getRootLogger().addAppender( this.appender );
		Dispatcher.get().forwardEvent( ILogsWaiter.Events.Start, this );
	}
	
	public void stop() {
		Logger.getRootLogger().removeAppender(this.appender);
		Dispatcher.get().forwardEvent( ILogsWaiter.Events.Stop, this );
	}
	
}
