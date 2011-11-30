package com.baidu.wamole.task;

import java.io.File;

import com.baidu.wamole.model.JsProject;

public class JsBuild extends AbstractBuild<JsProject, JsBuild> {

	public JsBuild(JsProject project, int id){
		super(project, id);
		this.buildSteps.add(new JsBuildStep(this));
	}

	@Override
	public File getRootDir() {
		return getParent().getRootDir();
	}
}
