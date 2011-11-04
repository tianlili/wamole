package com.baidu.wamole.resource.data;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.baidu.wamole.data.JsonParser;
import com.baidu.wamole.data.ProjectFileData;
import com.baidu.wamole.model.Project;
import com.baidu.wamole.model.Wamole;
import com.sun.jersey.api.core.ResourceContext;

public class ProjectDataResource {
	@Context
	ResourceContext context;

	/**
	 * list project
	 * 
	 * @return
	 */
	@GET
	public Response getProjectList() {
		List<Project> list = Wamole.getInstance().getProjectList().getView();
		return Response.ok(JsonParser.listToJson(list).toString()).build();
	}

	@GET
	@Path("/{name: [^/]+}")
	public Response getProject(@PathParam("name") String projName) {
		List<Project> list = Wamole.getInstance().getProjectList().getView();
		for (Project p : list)
			if (p.getName().equals(projName)) {
				return Response.ok(JsonParser.objToJson(p)).build();
			}
		// TODO
		return Response.noContent().build();
	}

	@POST
	@Path("/{name: [^/]+}")
	public Response addProject(@PathParam("name") String projName) {
		List<Project> list = Wamole.getInstance().getProjectList().getView();
		for (Project p : list)
			if (p.getName().equals(projName)) {
				return Response.ok(JsonParser.objToJson(p)).build();
			}
		// TODO
		return Response.noContent().build();
	}

	@GET
	@Path("/{name:[^/]+}/kiss/{path:[^?]+}")
	public Response listKiss(@PathParam("name") String name,
			@PathParam("path") String path) {
		return Response.noContent().build();
	}

	@GET
	@Path("/{name:[^/]+}/view{path:[^?]*}")
	public Response listView(@PathParam("name") String name,
			@PathParam("path") String path) {
		Project project = Wamole.getInstance().getProject(name);
		return Response.ok(
				JsonParser.objToJson(ProjectFileData.getData(project, path)).toString())
				.build();
	}
}
