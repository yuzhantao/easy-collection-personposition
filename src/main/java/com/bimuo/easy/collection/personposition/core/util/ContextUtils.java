package com.bimuo.easy.collection.personposition.core.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;


public class ContextUtils{

	private static ApplicationContext applicationContext;

	protected static Logger logger = LogManager.getLogger(ContextUtils.class);

	public static void setApplicationContext(ApplicationContext applicationContext) {
		synchronized (ContextUtils.class) {
			logger.debug("setApplicationContext, notifyAll");
			ContextUtils.applicationContext = applicationContext;
			ContextUtils.class.notifyAll();
		}
	}

	public static ApplicationContext getApplicationContext() {
		synchronized (ContextUtils.class) {
			while (applicationContext == null) {
				try {
					logger.debug("getApplicationContext, wait...");
					ContextUtils.class.wait(60000);
					if (applicationContext == null) {
						logger.warn("Have been waiting for ApplicationContext to be set for 1 minute", new Exception());
					}
				} catch (InterruptedException ex) {
					logger.debug("getApplicationContext, wait interrupted");
				}
			}
			return applicationContext;
		}
	}

	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}
	
	public static <T> Object getBean(Class<T> clazz) {
		return getApplicationContext().getBean(clazz);
	}
}
