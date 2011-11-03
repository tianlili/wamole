package com.baidu.wamole.resource.data;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.core.Response;

import com.baidu.wamole.browser.Browser;
import com.baidu.wamole.browser.BrowserManager;
import com.baidu.wamole.data.JsonParser;
import com.baidu.wamole.model.Wamole;

public class BrowserDataResource {
	@GET
	public Response getBrowserList() {
		BrowserManager bm = (BrowserManager) Wamole.getInstance().getModule(
				BrowserManager.class);
		List<Browser> list = bm.getBrowsers();

		return Response.ok(JsonParser.toJsonList(list).toString()).build();
	}
}
