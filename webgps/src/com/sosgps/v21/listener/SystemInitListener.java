package com.sosgps.v21.listener;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;


public class SystemInitListener implements ServletContextListener {
	private static final Logger logger = LoggerFactory
	.getLogger(SystemInitListener.class);
	public static Map<String, Properties> globalMessagesMap;

	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}

	public void contextInitialized(ServletContextEvent arg0) {
		logger.info("[contextInitialized] init globalMessage");
		ServletContext context = arg0.getServletContext();
		String messageLocation = context.getInitParameter("messageLocation");
		if (messageLocation != null && messageLocation.length() > 0) {
			try {
				if (messageLocation.startsWith("classpath:"))
					messageLocation = messageLocation.substring(10);
				globalMessagesMap = new HashMap<String, Properties>();
				Resource rs = new ClassPathResource(messageLocation);
				File[] files = rs.getFile().listFiles();
				for (File f : files) {
					if (!f.getName().endsWith(".properties")) {
						continue;
					}
					Properties properties = new Properties();
					properties.load(new FileInputStream(f));
					globalMessagesMap.put(f.getName().substring(0,
							f.getName().indexOf(".properties")), properties);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}