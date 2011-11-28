package com.baidu.wamole.model;

import java.io.File;

import com.baidu.wamole.task.Build;

public class DefaultKiss<P extends Project<P, B>, B extends Build<P, B>> extends AbstractModel<P> implements Kiss {

	protected String name;

	protected P project;

	public DefaultKiss(P project, String name) {
		super(project, name);
	}

	public P getProject() {
		return getParent();
	}

	@Override
	public File getRootDir() {
		return getParent().getRootDir();
	}
}