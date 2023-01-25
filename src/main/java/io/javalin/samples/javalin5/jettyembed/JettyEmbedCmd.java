package io.javalin.samples.javalin5.jettyembed;

import io.javalin.samples.javalin5.jettyembed.javalin.JavalinService;
import io.javalin.samples.javalin5.jettyembed.jetty.JettyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JettyEmbedCmd {

	public static void main(String[] args) {

		Log.info("Welcome to Jetty embed example...");

		JavalinService javalinService = new JavalinService();
		JettyService jettyService = new JettyService(javalinService);

		jettyService.start();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			jettyService.stop();
			jettyService.destroy();
		}));

		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			throw new RuntimeException("Main thread interrupted?!", e);
		}

	}

	private static final Logger Log = LoggerFactory.getLogger(JettyEmbedCmd.class);

}
