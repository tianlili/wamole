package com.baidu.wamole.model;

import java.util.List;

import com.baidu.wamole.data.Exported;
import com.baidu.wamole.data.Imported;
import com.baidu.wamole.task.AbstractBuild;
import com.baidu.wamole.task.BuildStep;
import com.baidu.wamole.util.CopyOnWriteList;

public abstract class AbstractProject<P extends AbstractProject<P, B>, B extends AbstractBuild<B, P>> extends AbstractItem
		implements Project<P, B> {

	protected AbstractProject(String name) {
		super(Wamole.getInstance(), name);
	}

	// 项目基础路径
	private String path;

	// 项目构建步骤
	private CopyOnWriteList<BuildStep<B>> buildSteps;

	// 项目
	@Exported
	public String getName() {
		return this.name;
	}
	
	@Imported
	public void setName(String name){
		this.name = name;
	}

	@Exported(encode = true)
	public String getPath() {
		return this.path;
	}
	
	@Imported(decode = false)
	public void setPath(String path){
		this.path = path;
	}

	public List<BuildStep<B>> getBuildSteps() {
		return buildSteps.getView();
	}
}
