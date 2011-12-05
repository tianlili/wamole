package com.baidu.wamole.model;



public class JsKiss extends DefaultKiss<JsProject>{
	
	public JsKiss(JsProject project, String name) {
		super(project, name);
	}

	public String getExecUrl() {
		return "/project/" + project.getName() + "/exec" + name;
	}
}