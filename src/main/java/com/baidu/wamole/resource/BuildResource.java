package com.baidu.wamole.resource;

import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.baidu.wamole.data.JsonParser;
import com.baidu.wamole.model.Project;
import com.baidu.wamole.model.Wamole;
import com.baidu.wamole.task.Build;

public class BuildResource {

	@GET
	public Response getBuildList() {
		@SuppressWarnings("rawtypes")
		Collection<Build> bs = Wamole.getInstance().getModels(Build.class);// .getQueue();

		return Response.ok(JsonParser.listToJson(bs).toString()).build();
	}

	/**
	 * 获取项目的构建列表
	 * 
	 * @param project
	 * @return
	 */
	@GET
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
	public Response addBuild(@PathParam("project") String project) {
		@SuppressWarnings("rawtypes")
		Project p = Wamole.getInstance().getModel(Project.class, project);
		p.addBuild();
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
