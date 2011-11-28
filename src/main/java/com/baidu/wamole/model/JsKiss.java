package com.baidu.wamole.model;

import com.baidu.wamole.task.JsBuild;


public class JsKiss extends DefaultKiss<JsProject, JsBuild>{
	
	public JsKiss(JsProject project, String name) {
		super(project, name);
	}

	public String getExecUrl() {
		return "/project/" + project.getName() + "/exec" + name;
	}
}