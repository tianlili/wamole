package com.baidu.wamole.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.baidu.wamole.browser.BrowserManager;
import com.baidu.wamole.server.JettyServer;
import com.baidu.wamole.task.Build;
import com.baidu.wamole.task.BuildThread;
import com.baidu.wamole.util.CopyOnWriteList;
import com.baidu.wamole.util.CopyOnWriteMap;
import com.baidu.wamole.xml.DefaultXStream;
import com.baidu.wamole.xml.XmlFile;
import com.thoughtworks.xstream.XStream;

public class Wamole implements ItemGroup<TopLevelItem> {
	private CopyOnWriteList<Project<?, ?>> projects = new CopyOnWriteList<Project<?, ?>>();	
	private transient final File root;
	private static Wamole instance;
	private Queue<Build<?, ?>> buildQueue;
	
//	private ProjectManager pm;
//	private BuilderManager dm;

	public <T> T getModule(Class<T> clazz) {
		return null;
	}

	public CopyOnWriteList<Project<?, ?>> getProjectList() {
		return projects;
	}

	public List<Project<?, ?>> getProjects() {
		return projects.getView();
	}

	public Wamole(File root) throws IOException {
		this.root = root;
		this.load();
		instance = this;
		buildQueue = new LinkedList<Build<?, ?>>();
		new BuildThread().start();
	}

	public void load() throws IOException {
		getConfigFile().unmarshal(this);
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
		for (Project<?, ?> project : projects) {
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
		// 队列添加
		projects.add(project);
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

	/**
	 * parser的导出解决方案
	 */
	private ArrayList<Class<? extends Parser<? extends Kiss, ? extends Project<?, ?>>>> parserList = new ArrayList<Class<? extends Parser<? extends Kiss, ? extends Project<?, ?>>>>();

	public ArrayList<Class<? extends Parser<? extends Kiss, ? extends Project<?, ?>>>> getParserTypeList() {
		if (parserList.size() == 0) {
			parserList.add(TangramParser.class);
			parserList.add(AntPathParser.class);
		}
		return parserList;
	}

	/**
	 * 项目类型的导出解决方案
	 */
	private ArrayList<Class<? extends Project<?, ?>>> projectTypeList = new ArrayList<Class<? extends Project<?, ?>>>();

	public ArrayList<Class<? extends Project<?, ?>>> getProjectTypeList() {
		if (projectTypeList.size() == 0) {
			projectTypeList.add(JsProject.class);
		}
		return projectTypeList;
	}

	@Override
	public File getRootDir() {
		return null;
	}

	@Override
	public void save() throws IOException {

	}

	@Override
	public File getRootDirFor(TopLevelItem child) {
		return getRootDirFor(child.getName());
	}
	
	public File getRootDirFor(String name){
		return new File(new File(getRootDir(),"projects"), name); 
	}

	@Override
	public TopLevelItem getItem(String name) {
		return items.get(name);
	}

	@Override
	public Collection<TopLevelItem> getItems() {
		return items.values();
	}

	/* package */transient final Map<String, TopLevelItem> items = new CopyOnWriteMap.Tree<String, TopLevelItem>(
			CopyOnWriteMap.CaseInsensitiveComparator.INSTANCE);
}
