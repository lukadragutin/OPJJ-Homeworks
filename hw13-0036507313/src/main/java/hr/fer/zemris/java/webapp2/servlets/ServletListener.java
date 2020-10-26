package hr.fer.zemris.java.webapp2.servlets;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Server listener that saves the time of server start up 
 * used to keep track od server up-time
 * @author Luka Dragutin
 *
 */
@WebListener
public class ServletListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		long now = System.currentTimeMillis();
		sce.getServletContext().setAttribute("time", now);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {	
	}

	
}
