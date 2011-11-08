package com.baidu.wamole.model;


import java.util.List;

import com.baidu.wamole.data.Exported;
import com.baidu.wamole.data.URLEncode;
import com.baidu.wamole.task.AbstractBuild;
import com.baidu.wamole.task.BuildStep;
import com.baidu.wamole.xml.CopyOnWriteList;

public abstract class AbstractProject<P extends AbstractProject<P, B> ,B extends AbstractBuild<B, P>> implements Project<P ,B> {

	private String name;
	// 项目基础路径
	private String path;
	
	//项目构建步骤
	private CopyOnWriteList<BuildStep> buildSteps;
	
	//项目
	@Exported
	public String getName() {
		return this.name;
	}

	@Exported
	@URLEncode(enc = "UTF-8")
	public String getPath() {
		return this.path;
	}
	
	public List<BuildStep> getBuildSteps() {
		return buildSteps.getView();
	}
}
