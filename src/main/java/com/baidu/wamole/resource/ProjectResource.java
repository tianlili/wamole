package com.baidu.wamole.resource;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.baidu.wamole.data.JsonParser;
import com.baidu.wamole.data.ProjectFileData;
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
	private Project<?, ?> project;
	String name;
	@Context
	UriInfo uriInfo;
	@Context
	ResourceContext context;

	public void setName(String name) {
		List<Project<?, ?>> list = Wamole.getInstance().getProjectList()
				.getView();
		for (Project<?, ?> project : list) {
			if (project.getName().equals(name)) {
				this.project = project;
			}
		}
		this.name = name;
	}

	@GET
	// @Consumes(MediaType.TEXT_HTML)
	// @Produces(MediaType.TEXT_HTML)
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
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getData() {
		if (project == null)
			return Response.noContent().build();
		return Response.ok(JsonParser.objToJson(project).toString()).build();
	}

	/**
	 * 项目新增接口，不存在则新增，否则更新
	 * @param path 项目跟路径
	 * @param parser 项目解析器
	 * @param type 项目类型
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addProject(@FormParam("path") String path,
			@FormParam("parser") String parser, @FormParam("type") String type) {
		
		return Response.ok("").build();
	}
	
	@PUT
	// TODO 后续补充
	public Response updateProject(){
		return Response.ok("").build();
	}

	@Path("/exec")
	public ExecuteResource executeCase() {
		return context.getResource(ExecuteResource.class);
	}

	@Path("/frame")
	public FrameResource importCase() {
		return context.getResource(FrameResource.class);
	}

	@GET
	@Path("/detail")
	public Response getDetail() {
		return Response.ok("detail").build();
	}

	@GET
	@Path("/build")
	public Response build() {
		Wamole.getInstance().addBuild(project.getBuild());
		return Response.ok("").build();
	}

	@GET
	@Path("/files{path:[^?]*}")
	@Consumes(MediaType.TEXT_HTML)
	@Produces(MediaType.TEXT_HTML)
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
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFileData(@PathParam("path") String path) {
		return Response.ok(
				JsonParser.objToJson(ProjectFileData.getData(project, path))
						.toString()).build();
	}
}
