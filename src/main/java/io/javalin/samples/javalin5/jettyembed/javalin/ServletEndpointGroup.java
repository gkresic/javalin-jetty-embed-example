package io.javalin.samples.javalin5.jettyembed.javalin;

import io.javalin.apibuilder.ApiBuilder;
import io.javalin.apibuilder.EndpointGroup;

// workaround for https://github.com/javalin/javalin/issues/1793
public class ServletEndpointGroup implements EndpointGroup {

	public ServletEndpointGroup(String path) {
		this.path = path;
	}

	public void addEndpoints() {
		ApiBuilder.path(path, new RootEndpointGroup());
	}

	private final String path;

}
