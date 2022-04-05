package com.onbelay.testapp.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.time.LocalDateTime;

public class ApplicationShutdown implements ApplicationListener<ContextClosedEvent> {
	private static final Logger logger = LogManager.getLogger(ApplicationShutdown.class);
	
	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		LocalDateTime dateTime = LocalDateTime.now();
	}
	
	

}
