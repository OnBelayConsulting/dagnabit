package com.onbelay.testapp.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


/**
 * This bean is called once the application context has been loaded.
 * @author canmxf
 *
 */
@Component
public class StartUpEventListener {
	private static final Logger logger = LogManager.getLogger();
	
	@EventListener
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
	}
	
	@EventListener
	public void onStoppingEvent(ContextStoppedEvent event) {
		logger.error("Application is stopping");
	}
	

}
