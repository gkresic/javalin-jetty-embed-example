package io.javalin.samples.javalin5.jettyembed;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.net.URL;

public class JettyEmbedCmd {

	public static void main(String[] args) {

		Log.info("Welcome to Jetty embed example...");

		// load Jetty configuration (jetty.xml)
		XmlConfiguration configuration;
		try {

			Resource resource = Resource.newResource(Thread.currentThread().getContextClassLoader().getResource("jetty.xml"));

			if (resource == null)
				throw new FileNotFoundException("Jetty configuration (jetty.xml) not found on classpath");

			configuration = new XmlConfiguration(resource);

			Log.info("Loaded Jetty configuration from {}", resource);

		} catch (Exception e) {
			throw new RuntimeException("Error loading Jetty configuration", e);
		}

		// build Jetty server
		Server server;
		try {

			server =  (Server) configuration.configure();

			Log.info("Jetty configured");

		} catch (Exception e) {
			throw new RuntimeException("Error constructing Jetty server", e);
		}

		// initialize webapp context
		WebAppContext webappContext;
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

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				server.stop();
				server.destroy();
				Log.info("Jetty shut down");
			} catch (Exception e) {
				Log.error("Unable to shutdown Jetty server", e);
			}
		}));

		try {
			server.start();
			Log.info("Jetty started");
		} catch (Exception e) {
			throw new RuntimeException("Error starting Jetty", e);
		}

		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			throw new RuntimeException("Main thread interrupted?!", e);
		}

	}

	private static final Logger Log = LoggerFactory.getLogger(JettyEmbedCmd.class);

}
