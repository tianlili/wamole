package com.baidu.wamole.resource.data;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.baidu.wamole.data.JsonParser;
import com.baidu.wamole.model.Project;
import com.baidu.wamole.model.Wamole;

public class ProjectDataResource {	
	/**
	 * list project
	 * 
	 * @return
	 */
	@GET
	public Response getProjectList() {
		List<Project> list = Wamole.getInstance().getProjectList().getView();
		return Response.ok(JsonParser.toJsonList(list).toString()).build();
	}

	@GET
	@Path("/{name: [^/]+}")
	public Response getProject(@PathParam("resource") String projName) {
		List<Project> list = Wamole.getInstance().getProjectList().getView();
		for (Project p : list)
			if (p.getName().equals(projName)) {
				return Response.ok(JsonParser.toJson(p)).build();
			}
		return Response.noContent().build();
	}
	
	@POST
	@Path("/{name: [^/]+}")
	public Response addProject(@PathParam("resource") String projName) {
		List<Project> list = Wamole.getInstance().getProjectList().getView();
		for (Project p : list)
			if (p.getName().equals(projName)) {
				return Response.ok(JsonParser.toJson(p)).build();
			}
		return Response.noContent().build();
	}
}
