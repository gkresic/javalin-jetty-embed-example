package io.javalin.samples.javalin5.jettyembed.javalin;

import io.javalin.apibuilder.ApiBuilder;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;

public class RootEndpointGroup implements EndpointGroup {

	public void addEndpoints() {

		ApiBuilder.get("/foo", this::foo);

		ApiBuilder.get("/bar", this::bar);

	}

	private void foo(Context context) {

		context.result("Foo");

	}

	private void bar(Context context) {

		context.result("Bar");

	}

}
