package com.baidu.wamole.resource;

import java.io.IOException;
import java.io.StringWriter;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.baidu.wamole.data.JsonParser;
import com.baidu.wamole.data.ProjectFileData;
import com.baidu.wamole.model.JsProject;
import com.baidu.wamole.model.Parser;
import com.baidu.wamole.model.Project;
import com.baidu.wamole.model.Wamole;
import com.baidu.wamole.template.ConfigurationFactory;
import com.sun.jersey.api.core.ResourceContext;

import freemarker.template.Template;

/**
 * 项目信息资源
 * 
 * @author dailiqi
 */
@Produces("text/html;charset=UTF-8")
public class ProjectResource {
	protected Project<?, ?> project;
	String name;
	@Context
	UriInfo uriInfo;
	@Context
	ResourceContext context;

	public void setName(String name) {
		this.project = Wamole.getInstance().getModel(Project.class, name);
		this.name = name;
	}

	@SuppressWarnings("rawtypes")
	public void setProject(Project p){
		this.project = p;
	}
	
	@GET
	public Response get() {
		StringWriter writer = new StringWriter();
		try {
			Template template = ConfigurationFactory.getInstance().getTemplate(
					"pages/page/project.html");
			template.dump(writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Response.ok(writer.getBuffer().toString()).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getData() {
		return Response.ok(project).build();		
	}

	/**
	 * 项目设置接口，用于更新parser和浏览器接口
	 * 
	 * @return
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateProject() {
		if (project instanceof JsProject) {
			@SuppressWarnings("rawtypes")
			Parser parser = (Parser) JsonParser.jsonToObject(uriInfo).get(
					"parser");
			if (project != null && parser != null)
				JsProject.class.cast(project).setParser(parser);
		}
	}

	@Path("/exec")
	public ExecuteResource executeCase() {
		return context.getResource(ExecuteResource.class);
	}

	@Path("/frame")
	public FrameResource importCase() {
		FrameResource fr = context.getResource(FrameResource.class);
		fr.setProject(project);
		return fr;
	}

	@GET
	@Path("/detail")
	public Response getDetail() {
		return Response.ok("detail").build();
	}

	@GET
	@Path("/files{path:[^?]*}")
	public Response getFile() {
		StringWriter writer = new StringWriter();
		try {
			Template template = ConfigurationFactory.getInstance().getTemplate(
					"pages/page/files.html");
			template.dump(writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Response.ok(writer.getBuffer().toString()).build();
	}

	@GET
	@Path("/files{path:[^?]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFileData(@PathParam("path") String path) {
		return Response.ok(
				JsonParser.objToJson(ProjectFileData.getData(project, path))
						.toString()).build();
	}
}
