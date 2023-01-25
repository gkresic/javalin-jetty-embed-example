package io.javalin.samples.javalin5.jettyembed.jetty;

import io.javalin.samples.javalin5.jettyembed.javalin.JavalinService;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.net.URL;

public class JettyService {

	public JettyService(JavalinService javalinService) {

		// load Jetty configuration from jetty.xml
		try {
			Resource resource = Resource.newResource(Thread.currentThread().getContextClassLoader().getResource("jetty.xml"));
			if (resource == null)
				throw new FileNotFoundException("Jetty configuration (jetty.xml) not found on classpath");
			configuration = new XmlConfiguration(resource);
		} catch (Exception e) {
			throw new RuntimeException("Error loading Jetty configuration", e);
		}

		// build Jetty server
		try {
			server =  (Server) configuration.configure();
		} catch (Exception e) {
			throw new RuntimeException("Error constructing Jetty server", e);
		}

		// initialize webapp context
		try {

			Handler handler = server.getHandler();
			while (handler instanceof HandlerWrapper && !(handler instanceof WebAppContext))
				handler = ((HandlerWrapper) handler).getHandler();

			if (handler == null)
				throw new RuntimeException("No WebAppContext handler configured in Jetty?!");

			assert handler instanceof WebAppContext;	// just to silence IntelliJ

			webappContext = (WebAppContext) handler;

			URL webAppDir = Thread.currentThread().getContextClassLoader().getResource("war");

			if (webAppDir == null)
				throw new FileNotFoundException("Jetty root (war) not found on classpath");

			webappContext.setResourceBase(webAppDir.toURI().toString());

		} catch (Exception e) {
			throw new RuntimeException("Error configuring Jetty webapp context", e);
		}

		// register Javalin servlet
		webappContext.addServlet(new ServletHolder(javalinService.servlet()), javalinService.pathSpec());

	}

	public XmlConfiguration configuration() { return configuration; }

	public Server server() { return server; }

	public WebAppContext webappContext() { return webappContext; }

	public void start() {
		try {
			server.start();
			Log.info("Jetty started");
		} catch (Exception e) {
			throw new RuntimeException("Error starting Jetty", e);
		}
	}

	public void stop() {
		try {
			server.stop();
			Log.info("Jetty stopped");
		} catch (Exception e) {
			Log.error("Error stopping jetty", e);
		}
	}

	public void destroy() {
		try {
			server.destroy();
			Log.info("Jetty shut down");
		} catch (Exception e) {
			Log.error("Unable to shutdown Jetty server", e);
		}
	}

	private static final Logger Log = LoggerFactory.getLogger(JettyService.class);

	private final XmlConfiguration configuration;
	private final Server server;
	private final WebAppContext webappContext;

}
