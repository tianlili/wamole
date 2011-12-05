package com.baidu.wamole.resource;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.baidu.wamole.browser.Browser;
import com.baidu.wamole.browser.BrowserManager;
import com.baidu.wamole.data.JsonParser;
import com.baidu.wamole.exception.TestException;
import com.baidu.wamole.model.JsKiss;
import com.baidu.wamole.model.Wamole;
import com.baidu.wamole.task.Result;
import com.baidu.wamole.template.ConfigurationFactory;
import com.sun.jersey.api.core.ResourceContext;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 浏览器资源
 * 
 * @author dailiqi
 */
@Produces("text/html;charset=UTF-8")
public class BrowserResource {
	@Context
	UriInfo uriInfo;
	@Context
	ResourceContext context;
	@Context
	HttpServletRequest request;

	@GET
	public Response get() {
		StringWriter writer = new StringWriter();
		try {
			Template template = ConfigurationFactory.getInstance().getTemplate(
					"pages/page/browsers.html");
			// Map<String, Object> map = new HashMap<String, Object>();
			// map.put("project", project);
			// template.process(map, writer);
			template.dump(writer);
		} catch (IOException e) {
			e.printStackTrace();
			// } catch (TemplateException e) {
			// e.printStackTrace();
		}
		return Response.ok(writer.getBuffer().toString()).build();
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getData() {
		return Response.ok(
				JsonParser.listToJson(
						Wamole.getInstance()
								.getModel(BrowserManager.class,
										BrowserManager.name).getBrowsers())
						.toString()).build();
	}

	@GET
	@Path("/register")
	public Response getPage() {
		StringWriter writer = new StringWriter();
		try {
			Template template = ConfigurationFactory.getInstance().getTemplate(
					"browser/register.ftl");
			template.process(null, writer);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		return Response.ok(writer.toString()).build();
	}

	/**
	 * 当返回 false 则表示已注过，否则注册
	 * 
	 * @param param
	 * @return
	 */
	@POST
	@Path("/register")
	public Response register(@HeaderParam("user-agent") String ua) {
		String ip = request.getRemoteHost();
		// 新建一个instance
		Browser instance = Browser.build(ip, ua).build();
		List<Browser> browsers = Wamole.getInstance()
				.getModel(BrowserManager.class, "browsers").getBrowsers();
		// 获取已存在的 list 进行对比
		for (Browser browser : browsers) {
			if (browser.isEqual(instance)) {
				return Response.ok("false").build();
			}
		}
		Wamole.getInstance().getModel(BrowserManager.class, "browsers")
				.addBrowser(instance);
		return Response.ok(instance.getId()).build();
	}

	// @GET
	// public Response list() {
	// BrowserManager bm = (BrowserManager) Wamole.getInstance().getModule(
	// BrowserManager.class);
	// List<Browser> list = bm.getBrowsers();
	// StringWriter writer = new StringWriter();
	// try {
	// Template template = ConfigurationFactory.getInstance().getTemplate(
	// "browser/list.ftl");
	// Map<String, Object> map = new HashMap<String, Object>();
	// map.put("browsers", list);
	// template.process(map, writer);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (TemplateException e) {
	// e.printStackTrace();
	// }
	// return Response.ok(writer.toString()).build();
	// }

	@GET
	@Path("/capture/{ids}")
	public Response getCapturePage() {
		StringWriter writer = new StringWriter();
		try {
			Template template = ConfigurationFactory.getInstance().getTemplate(
					"browser/capture.ftl");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("step", Wamole.getInstance().getModel(BrowserManager.class).getStep());
			try {
				template.process(map, writer);
			} catch (TemplateException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Response.ok(writer.toString()).build();
	}

	@PUT
	@Path("/capture/{ids}")
	public Response capture(@PathParam("ids") String id,
			@FormParam("name") String name,
			@FormParam("starttime") String starttime,
			@FormParam("endtime") String endtime,
			@FormParam("fail") String fail, @FormParam("total") String total,
			@FormParam("cov") String cov, @HeaderParam("user-agent") String ua) {

		BrowserManager bm = Wamole.getInstance().getModel(BrowserManager.class,
				BrowserManager.name);
		if (null == bm.getBrowser(id)) {
			return Response.status(404).build();
		}
		Result result = new Result();
		result.setBrowser(bm.getBrowser(id).getName());
		if (null != endtime && !"undefined".equals(endtime)
				&& !"".equals(endtime)) {
			result.setFail(Integer.valueOf(fail));
			result.setTotal(Integer.valueOf(total));
			result.setName(name);
			result.setTimeStamp(Long.valueOf(endtime) - Long.valueOf(starttime));
		}
		try {
			JsKiss kiss = bm.notice(id, result);
			if (null == kiss)
				return Response.ok("").build();
			else
				return Response.ok(kiss.getExecUrl()).build();
		} catch (TestException e) {
			e.printStackTrace();
			return Response.status(500).build();
		}
	}
}
