package com.baidu.wamole.task;

import com.baidu.wamole.model.JsProject;

public class JsBuild extends AbstractBuild<JsProject, JsBuild> {

	public String filter;
	public JsBuild(JsProject project, int id, String filter){
		super(project, id);
		this.filter = filter;
		this.buildSteps.add(new JsBuildStep(this));
	}
	
}
