package com.baidu.wamole.resource;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.baidu.wamole.resource.data.BrowserDataResource;
import com.baidu.wamole.resource.data.ProjectDataResource;
import com.sun.jersey.api.core.ResourceContext;

@Produces(MediaType.APPLICATION_JSON+";charset=UTF-8")
public class DataResource {

	@Context
	ResourceContext context;
	
	@Path("/project")
	public ProjectDataResource getProjectResource(){
		return context.getResource(ProjectDataResource.class);
	}

	@Path("/browser")
	public BrowserDataResource getBrowserResource(){
		return context.getResource(BrowserDataResource.class);
	}
}
