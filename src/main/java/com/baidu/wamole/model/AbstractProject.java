package com.baidu.wamole.model;

import java.io.File;
import java.io.IOException;
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

//	protected AbstractProject(String name, String path) {
//		super(Wamole.getInstance(), name);
//		this.path = path;
//	}

	protected String path;
	protected String name;

	// kiss init if needed
	protected transient boolean kissInited;
	// build init if needed
	protected transient boolean buildInited;

	public void loadBuildList() throws IOException {
		try {
			loadChildren("builds");			
		} finally {
			buildInited = true;
		}
	}

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
	private void initKiss() {
		kissMaps.put(this, getParser().parse(this));
	}

	public Kiss getKiss(String name) {
		if (!kissInited) {
			initKiss();
		}
		return kissMaps.get(this).get(name);
	}

	public Collection<Kiss> getKisses() {
		if (!kissInited)
			initKiss();
		return kissMaps.get(this).values();
	}

	@SuppressWarnings("unchecked")
	public Collection<B> getBuilds() {
		if (!buildInited)
			try {
				loadBuildList();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
		return new File(Wamole.getInstance().getRootDir(), "project/" + name);
	}	
}
