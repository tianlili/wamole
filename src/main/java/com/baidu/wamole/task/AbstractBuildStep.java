package com.baidu.wamole.task;

import com.baidu.wamole.model.Project;
import com.baidu.wamole.task.Build;
import com.baidu.wamole.task.BuildStep;

public abstract class AbstractBuildStep<P extends Project<P, B>, B extends Build<P, B>>
		implements BuildStep<P, B> {
	protected transient B build;
	protected transient P project;
	
	@Override
	public B getBuild() {
		return build;
	}
	
	@Override
	public P getProject() {
		return project;
	}
}
