package com.baidu.wamole.task;

import java.io.File;
import java.io.IOException;

import com.baidu.wamole.model.JsProject;

public class JsBuild extends AbstractBuild<JsProject, JsBuild> {

	public JsBuild(JsProject project, int id) throws IOException {
		super(project, id);
	}

	@Override
	public File getRootDir() {
		return getParent().getRootDir();
	}
}
