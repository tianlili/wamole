package com.baidu.wamole.model;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.baidu.wamole.data.Exported;
import com.baidu.wamole.data.Imported;
import com.baidu.wamole.task.AbstractBuild;

/**
 * @author yangbo
 * 
 * @param <P>
 * @param <B>
 */
public abstract class AbstractProject<P extends AbstractProject<P, B>, B extends AbstractBuild<P, B>>
		extends AbstractModelGroup<Wamole> implements Project<P, B> {

	protected AbstractProject(String name, String path) {
		super(Wamole.getInstance(), name);
		this.name = name;
		this.path = path;
	}

	protected String name;

	protected String path;

	protected boolean inited;

	// 项目
	@Exported
	public String getName() {
		return this.name;
	}

	@Imported
	public void setName(String name) {
		this.name = name;
	}

	@Exported(encode = true)
	public String getPath() {
		return this.path;
	}

	@Imported(decode = false)
	public void setPath(String path) {
		this.path = path;
	}

	@SuppressWarnings("rawtypes")
	private static HashMap<Project, Map<String, Kiss>> kissMaps = new HashMap<Project, Map<String, Kiss>>();

	@SuppressWarnings("unchecked")
	private void initKiss(){
		kissMaps.put(this, getParser().parse(this));
	}
	
	public Kiss getKiss(String name) {
		if (!inited) {
			initKiss();
		}
		return kissMaps.get(this).get(name);
	}

	public Collection<Kiss> getKisses() {
		if(!inited)
			initKiss();
		return kissMaps.get(this).values();
	}

	@SuppressWarnings("unchecked")
	public Collection<B> getBuilds() {
		// 获取范型的实际类型
		return getModels((Class<B>) (((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0]));
	}

	public B getBuild(int id) {
		for (B b : getBuilds())
			if (b.getId() == id)
				return b;
		return null;
	}

	protected int getUsableBuildId() {
		int id = 0;
		for (B b : getBuilds()) {
			if (id <= b.getId())
				id++;
		}
		return id;
	}

	@Override
	public File getRootDir() {
		return new File(parent.getRootDir(), "project/" + name);
	}
}
