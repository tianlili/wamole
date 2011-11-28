package com.baidu.wamole.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.baidu.wamole.browser.BrowserManager;
import com.baidu.wamole.server.JettyServer;
import com.baidu.wamole.task.Build;
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

	private Queue<Build<?, ?>> buildQueue;

	public Wamole(File root) throws IOException {
		super(null, "wamole");
		this.root = root;
		this.load();
		instance = this;
		buildQueue = new LinkedList<Build<?, ?>>();
		new BuildThread().start();
	}

	public void load() throws IOException {
		getConfigFile().unmarshal(this);
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
	public void addProject(Project<?, ?> project) {
		addModel(project);
		// 启动服务
		JettyServer.addPath(project);
	}

	public void addBuild(Project<?, ?> project) {

	}

	public synchronized void addBuild(Build<?, ?> build) {
		this.buildQueue.add(build);
	}

	public synchronized Queue<Build<?, ?>> getBuildQueue() {
		return this.buildQueue;
	}

	public List<Class<? extends Parser<?, ?>>> getParserTypeList() {
		List<Class<? extends Parser<?, ?>>> list = new ArrayList<Class<? extends Parser<?, ?>>>();
		list.add(TangramParser.class);
		list.add(AntPathParser.class);
		return list;
	}
}
