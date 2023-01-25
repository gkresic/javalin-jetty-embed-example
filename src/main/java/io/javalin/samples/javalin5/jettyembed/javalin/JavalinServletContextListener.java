package io.javalin.samples.javalin5.jettyembed.javalin;

import io.javalin.Javalin;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.ServletRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavalinServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {

		ServletContext context = event.getServletContext();

		String path = "/rest";

		javalin = Javalin.createStandalone();

		javalin.routes(new ServletEndpointGroup(path));

		ServletRegistration.Dynamic registration = context.addServlet("Javalin Servlet", javalin.javalinServlet());
		registration.addMapping(path + "/*");

		Log.info("Initialized");

	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {

		javalin.close();

		javalin = null;

	}

	private static final Logger Log = LoggerFactory.getLogger(JavalinServletContextListener.class);

	private Javalin javalin = null;

}
