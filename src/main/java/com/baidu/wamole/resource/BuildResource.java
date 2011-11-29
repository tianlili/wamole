package com.baidu.wamole.resource;

import java.util.Queue;

import javax.ws.rs.GET;
import javax.ws.rs.core.Response;

import com.baidu.wamole.model.Wamole;
import com.baidu.wamole.task.Build;
import com.baidu.wamole.task.BuildQueue;

public class BuildResource {

	@GET
	public Response getBuildList() {
		Queue<Build<?,?>> queue = Wamole.getInstance().getModel(BuildQueue.class, "").getQueue();
		
		return Response.ok(queue.toString()).build();
	}
	
}
