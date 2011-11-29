package com.baidu.wamole.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.baidu.wamole.browser.BrowserManager;
import com.baidu.wamole.server.JettyServer;
import com.baidu.wamole.task.BuildQueue;
import com.baidu.wamole.task.BuildThread;
import com.baidu.wamole.xml.DefaultXStream;
import com.thoughtworks.xstream.XStream;

public class Wamole extends AbstractModelGroup<ModelGroup> {

	private static Wamole instance;

	public static Wamole getInstance() {
		return instance;
	}

	private transient final File root;

	@Override
	public File getRootDir() {
		return root;
	}

	public Wamole(File root) throws IOException {
		super(null, "wamole");
		this.root = root;
		this.load();
		instance = this;
		new BuildThread().start();
	}

	public void load() throws IOException {
		getConfigFile().unmarshal(this);
		
		//初始化队列
		new BuildQueue(this);
	}

	private static final XStream XSTREAM = new DefaultXStream();
	
	static {
		// 定义XML中别名
		XSTREAM.alias("wamole", Wamole.class);
		XSTREAM.alias("bm", BrowserManager.class);
	}

	/**
	 * 新增项目接口，需要考虑重启等问题 <li>追加项目信息 <li>追加服务器信息
	 * 
	 * @param project
	 */
	public void addProject(String name, String path) {		
		// 启动服务
		JettyServer.addPath(new JsProject(name, path));
	}

	public List<Class<? extends Parser<?, ?>>> getParserTypeList() {
		List<Class<? extends Parser<?, ?>>> list = new ArrayList<Class<? extends Parser<?, ?>>>();
		list.add(TangramParser.class);
		list.add(AntPathParser.class);
		return list;
	}
}
