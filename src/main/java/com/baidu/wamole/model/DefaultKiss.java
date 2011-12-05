package com.baidu.wamole.model;


public class DefaultKiss<P extends Project<P, ?>>
		implements Kiss {

	protected String name;

	protected P project;

	public DefaultKiss(P project, String name) {
		this.project = project;
		this.name = name;
	}

	public P getProject() {
		return project;
	}

	@Override
	public String getName() {
		return this.name;
	}
}