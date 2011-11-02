package com.baidu.wamole.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.baidu.wamole.data.JsonParser;
import com.baidu.wamole.model.Project;
import com.baidu.wamole.model.Wamole;

public class DataResource {

	/**
	 * list project
	 * 
	 * @return
	 */
	@GET
	@Path("/project")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getProjectList() {
		List<Project> list = Wamole.getInstance().getProjectList().getView();
		return Response.ok(JsonParser.toJsonList(list).toString()).build();
	}

	@GET
	@Path("/project/{name: [^/]+}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getProject(@PathParam("resource") String projName) {
		List<Project> list = Wamole.getInstance().getProjectList().getView();
		for (Project p : list)
			if (p.getName().equals(projName)) {
				return Response.ok(JsonParser.toJson(p)).build();
			}
		return Response.noContent().build();
	}
	
	@POST
	@Path("/project/{name: [^/]+}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addProject(@PathParam("resource") String projName) {
		List<Project> list = Wamole.getInstance().getProjectList().getView();
		for (Project p : list)
			if (p.getName().equals(projName)) {
				return Response.ok(JsonParser.toJson(p)).build();
			}
		return Response.noContent().build();
	}
}
