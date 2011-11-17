package com.baidu.wamole.resource;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.baidu.wamole.data.JsonParser;
import com.baidu.wamole.model.Project;
import com.baidu.wamole.model.Wamole;
import com.baidu.wamole.template.ConfigurationFactory;
import com.sun.jersey.api.core.ResourceContext;

import freemarker.template.Template;

@Path("/")
@Produces("text/html;charset=UTF-8")
public class RootResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	ResourceContext context;

	@GET
	public Response getView() {
		StringWriter writer = new StringWriter();
		try {
			Template template = ConfigurationFactory.getInstance().getTemplate(
					"pages/page/index.html");
			template.dump(writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Response.ok(writer.getBuffer().toString()).build();
	}

	@GET
	@Path("/project")
	public Response getProject() {
		StringWriter writer = new StringWriter();
		try {
			Template template = ConfigurationFactory.getInstance().getTemplate(
					"pages/page/projects.html");
			template.dump(writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Response.ok(writer.getBuffer().toString()).build();
	}

	@GET
	@Path("/project")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProjectData() {
		List<Project<?, ?>> list = Wamole.getInstance().getProjectList()
				.getView();
		return Response.ok(JsonParser.listToJson(list).toString()).build();
	}

	@Path("/project/{name}")
	public ProjectResource getProjectByName(@PathParam("name") String name) {
		ProjectResource resource = context.getResource(ProjectResource.class);
		resource.setName(name);
		return resource;
	}

	@Path("/task")
	public TaskResource getResourceByName() {
		return context.getResource(TaskResource.class);
	}

	@Path("/browser")
	public BrowserResource getBrowser() {
		return context.getResource(BrowserResource.class);
	}

	@Path("/data")
	public DataResource getData() {
		return context.getResource(DataResource.class);
	}

	@Path("/build")
	public BuildResource build() {
		return context.getResource(BuildResource.class);
	}
}
