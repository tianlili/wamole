package com.baidu.wamole.resource;

import java.io.IOException;
import java.io.StringWriter;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

	/**
	 * 获取index页的HTML页面
	 * 
	 * @return
	 */
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

	/**
	 * 请求项目列表的HTML接口
	 * 
	 * @return
	 */
	@GET
	@Path("/project")
	@Consumes(MediaType.TEXT_HTML)
	public Response getProjectList() {
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

	/**
	 * 请求项目列表的数据接口
	 * 
	 * @return
	 */
	@GET
	@Path("/project")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getProjectListData() {
		return Response.ok(
				JsonParser.listToJson(
						Wamole.getInstance().getModels(Project.class))
						.toString()).build();
	}

	/**
	 * 请求添加项目的HTML页面
	 * 
	 * @return
	 */
	@GET
	@Path("/addProject")
	public Response getProjectAddPage() {
		StringWriter writer = new StringWriter();
		try {
			Template template = ConfigurationFactory.getInstance().getTemplate(
					"pages/page/addProject.html");
			template.dump(writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Response.ok(writer.getBuffer().toString()).build();
	}

	/**
	 * Project添加接口的数据接口，以form表单形式提供
	 * 
	 * @param name
	 * @param path
	 * @param parser
	 * @return
	 */
	@POST
	@Path("/addProject")
	@Produces(MediaType.TEXT_HTML)
	public void addProject(@FormParam("name") String name, @FormParam("path") String path) {
		//TODO add return info
		Wamole.getInstance().addProject(name, path);
	}

	@Path("/project/{name}")
	public ProjectResource getProjectByName(@PathParam("name") String name) {
		ProjectResource resource = context.getResource(ProjectResource.class);
		resource.setProject(Wamole.getInstance().getModel(Project.class, name));
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

	@Path("/build")
	public BuildResource build() {
		BuildResource b = context.getResource(BuildResource.class);
		return b;
	}

	@Path("/enum")
	public EnumResource getEnum() {
		return context.getResource(EnumResource.class);
	}
}
