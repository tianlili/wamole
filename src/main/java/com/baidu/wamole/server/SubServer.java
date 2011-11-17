package com.baidu.wamole.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * 添加子服务器用于解决需要重启的问题，因为仅文件服务器存在重启需求，暂时考虑接收文件服务器请求，其他忽略<br/>
 * 
 * 服务器设置为对外界隐藏，但是，通过特定端口访问可达
 * 
 * <li>本类维护一个已用接口列表，用于解决可能存在的端口冲突。
 * <li>仅支持加入fileresourcehandler
 * 
 * @author yangbo
 * 
 */
public class SubServer extends Server {

	public SubServer(String alias, String path, int port) {
		super(port);
		WebAppContext wac = new WebAppContext(path, alias);
		this.setHandler(wac);
	}
}
