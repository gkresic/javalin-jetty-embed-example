package io.javalin.samples.javalin5.jettyembed.javalin;

import io.javalin.Javalin;
import jakarta.servlet.Servlet;

public class JavalinService {

	public static final String Path = "/rest";

	public JavalinService() {

		javalin = Javalin.createStandalone();

		javalin.routes(new ServletEndpointGroup(Path));

	}

	public Servlet servlet() { return javalin.javalinServlet(); }

	public String pathSpec() { return Path + "/*"; }

	private final Javalin javalin;

}
