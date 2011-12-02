package com.baidu.wamole.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.baidu.wamole.exception.TestException;

@Produces("text/html;charset=UTF-8")
public class FrameResource extends ProjectResource{
	@Context
	UriInfo uriInfo;

	/**
	 * <ul>
	 * <li>支持单用例执行
	 * <li>支持路径用例批量执行
	 * 
	 * @param hh
	 * @param path
	 * @param projectName
	 * @return
	 */
	@GET
	@Path("{path: .*}")
	public Response importCase(@PathParam("path") String path) {
		path = "/" + path;
		if (path.endsWith(".js"))
			try {
				String s = project.getExecutePage(path);
				String testimport = s.substring(s.lastIndexOf("<script"), s.lastIndexOf("</script>") +"</script>".length());
				s = s.replace(testimport, "");
//				System.out.println(s);
				return Response.ok(s).build();
			} catch (TestException e) {
				e.printStackTrace();
				return Response.ok(e.getMessage() + "path：" + path).build();
			}
		else
			return Response.status(404).build();
	}
}
