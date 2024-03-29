package com.api.deployer.ui.utils;

import com.redshape.ui.application.events.UIEvents;
import org.apache.log4j.spi.LoggingEvent;

import java.util.Collection;

public interface ILogsWaiter {
	
	public static class Events extends UIEvents {
		
		protected Events( String code ) {
			super(code);
		}
		
		public static final Events New = new Events("ILogsWaiter.Events.New");
		public static final Events Start = new Events("ILogsWaiter.Events.Start");
		public static final Events Stop = new Events("ILogsWaiter.Events.Stop");
	}
	
	public void start();
	
	public void stop();
	
	public Collection<LoggingEvent> getLogs();
	
}
