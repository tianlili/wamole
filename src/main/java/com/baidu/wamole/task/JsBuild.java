package com.baidu.wamole.task;

import com.baidu.wamole.model.JsProject;

public class JsBuild extends AbstractBuild<JsProject, JsBuild> {

	public JsBuild(JsProject project, int id){
		super(project, id);
		this.buildSteps.add(new JsBuildStep(this));
	}

}
