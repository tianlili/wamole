package com.baidu.wamole.task;

import java.io.IOException;
import java.util.List;

import com.baidu.wamole.model.AbstractItem;
import com.baidu.wamole.model.AbstractProject;

public abstract class AbstractBuild<B extends AbstractBuild<B, P>, P extends AbstractProject<P, B>> extends AbstractItem
		implements Build<B, P> {
	protected P project;
	protected boolean finished;
	protected boolean started;

	/**
	 * Creates a new build.
	 */
	protected AbstractBuild(P project) throws IOException {
		super(project, "jsbuild");
		this.project = project;
	}

	@Override
	public P getProject() {
		return this.project;
	}

	@Override
	public void build() {
		this.started = true;
		List<BuildStep<B>> buildSteps = project.getBuildSteps();
		for (BuildStep<B> step : buildSteps) {
//			if (step.preBuild(this)) {
//				step.perform(this);
//			}
			step.preBuild(this);
			step.perform(this);
		}
		this.finished = true;
	}

	@Override
	public boolean finished() {
		return this.finished;
	}

	@Override
	public boolean started() {
		return this.started;
	}
}
