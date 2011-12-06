package com.baidu.wamole.resource;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.baidu.wamole.data.JsonParser;
import com.baidu.wamole.model.Project;
import com.baidu.wamole.model.Wamole;
import com.baidu.wamole.task.Build;
import com.baidu.wamole.template.ConfigurationFactory;

import freemarker.template.Template;

public class BuildResource {

	@GET
	public Response getBuildList() {
		@SuppressWarnings("rawtypes")
		Collection<Build> bs = Wamole.getInstance().getModels(Build.class);// .getQueue();

		return Response.ok(JsonParser.listToJson(bs).toString()).build();
	}
	
	/**
	 * 获取项目构建列表的HTML页面
	 * 
	 * @return
	 */
	@GET
	@Path("/{project:[^/]+}")
	public Response getBuilds() {
		StringWriter writer = new StringWriter();
		try {
			Template template = ConfigurationFactory.getInstance().getTemplate(
					"pages/page/builds.html");
			template.dump(writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Response.ok(writer.getBuffer().toString()).build();
	}

	/**
	 * 获取项目的构建列表
	 * 
	 * @param project
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{project:[^/]+}")
	public Response getProjectBuildList(@PathParam("project") String project) {
		@SuppressWarnings("rawtypes")
		Project p = Wamole.getInstance().getModel(Project.class, project);
		return Response.ok(JsonParser.listToJson(p.getBuilds()).toString())
				.build();
	}

	/**
	 * 提交一次构建，此处需要考虑参数的需求 TODO
	 * 
	 * @return
	 */
	@POST
	@Path("/{project:[^/]+}")
	public Response addBuild(@PathParam("project") String project,
			@FormParam("filter") String filter) {
		@SuppressWarnings("rawtypes")
		Project p = Wamole.getInstance().getModel(Project.class, project);
		try {
			if (filter == null)
				p.addBuild(null);
			else
				p.addBuild(URLDecoder.decode(filter, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return Response.ok().build();
	}

	@GET
	@Path("/{project:[^/]+}/{id:\\d+}")
	public Response getProjectBuild(@PathParam("project") String project,
			@PathParam("id") String id) {
		@SuppressWarnings("rawtypes")
		Project p = Wamole.getInstance().getModel(Project.class, project);
		return Response.ok(
				JsonParser.objToJson(p.getModel(Build.class, id)).toString())
				.build();
	}

}
