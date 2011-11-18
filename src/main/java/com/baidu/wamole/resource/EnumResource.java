package com.baidu.wamole.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.baidu.wamole.data.JsonParser;
import com.baidu.wamole.model.Wamole;
import com.sun.jersey.api.core.ResourceContext;

@Produces("text/html;charset=UTF-8")
public class EnumResource {
	@Context
	UriInfo uriInfo;
	@Context
	ResourceContext context;

	@GET
	@Path("/parser")
	public Response getParserType() {
		return Response.ok(JsonParser.classListToJson(Wamole.getInstance().getParserTypeList()).toString())
				.build();
	}
}
