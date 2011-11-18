package com.baidu.wamole.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.eclipse.jetty.util.ajax.JSON;

import com.baidu.wamole.browser.BrowserManager;
import com.baidu.wamole.config.Config;
import com.baidu.wamole.config.Config.NOOBConfig;
import com.baidu.wamole.task.Build;
import com.baidu.wamole.task.BuildThread;
import com.baidu.wamole.xml.CopyOnWriteList;
import com.baidu.wamole.xml.DefaultXStream;
import com.baidu.wamole.xml.XmlFile;
import com.thoughtworks.xstream.XStream;

public class Wamole {
	private Config config = new NOOBConfig();
	private CopyOnWriteList<Project<?, ?>> projects = new CopyOnWriteList<Project<?, ?>>();
	private transient final File root;
	private static Wamole instance;
	private CopyOnWriteList<Module> modules = new CopyOnWriteList<Module>();
	private Queue<Build<?, ?>> buildQueue;

	public CopyOnWriteList<Project<?, ?>> getProjectList() {
		return projects;
	}

	public Wamole(File root) {
		this.root = root;
		this.load();
		instance = this;
		buildQueue = new LinkedList<Build<?, ?>>();
		new BuildThread().start();
	}

	public List<Module> getModules() {
		return modules.getView();
	}

	public Module getModule(Class<? extends Module> clazz) {
		List<Module> tmp = modules.getView();
		for (int i = 0; i < tmp.size(); i++) {
			if (clazz.isInstance(tmp.get(i))) {
				return clazz.cast(tmp.get(i));
			}
		}
		return null;
	}

	public Config getConfig() {
		return this.config;
	}

	public void load() {
		XmlFile file = this.getConfigFile();
		try {
			System.out.println(file.getFile().getAbsolutePath());
			file.unmarshal(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private XmlFile getConfigFile() {
		return new XmlFile(XSTREAM, new File(root, "config.xml"));
	}

	private static final XStream XSTREAM = new DefaultXStream();
	{
		// 定义XML中别名
		XSTREAM.alias("wamole", Wamole.class);
		XSTREAM.alias("bm", BrowserManager.class);
	}

	public static Wamole getInstance() {
		return instance;
	}

	public Project<?, ?> getProject(String name) {
		List<Project<?, ?>> list = projects.getView();
		for (Project<?, ?> project : list) {
			if (project.getName().equals(name)) {
				return project;
			}
		}
		return null;
	}

	/**
	 * 新增项目接口，需要考虑重启等问题 <li>追加项目信息 <li>追加服务器信息
	 * 
	 * @param project
	 */
	public void addProject(Project<?, ?> project) {
		projects.getView().add(project);
	}

	public synchronized void addBuild(Build<?, ?> build) {
		this.buildQueue.add(build);
	}

	public synchronized Queue<Build<?, ?>> getBuildQueue() {
		return this.buildQueue;
	}

	private ArrayList<Class<? extends Parser<? extends Kiss, ? extends Project<?, ?>>>> parserList = new ArrayList<Class<? extends Parser<? extends Kiss, ? extends Project<?, ?>>>>();

	public ArrayList<Class<? extends Parser<? extends Kiss, ? extends Project<?, ?>>>> getParserTypeList() {
		if (parserList.size() == 0) {
			parserList.add(TangramParser.class);
			parserList.add(AntPathParser.class);
		}
		return parserList;
	}
	
	private ArrayList<Class<?extends Project<?,?>>> projectTypeList = new ArrayList<Class<? extends Project<?,?>>>();
	
	public ArrayList<Class<? extends Project<?,?>>> getProjectTypeList() {
		if (projectTypeList.size() == 0) {
			projectTypeList.add(JsProject.class);			
		}
		return projectTypeList;
	}
	
//	public void addProject(JSON json, String data){
//		getProjectList().add(json.fromJSON(data));
//	}
}
