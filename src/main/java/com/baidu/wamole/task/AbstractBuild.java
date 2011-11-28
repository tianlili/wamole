package com.baidu.wamole.task;

import java.io.IOException;
import java.util.List;

import com.baidu.wamole.model.AbstractModel;
import com.baidu.wamole.model.AbstractProject;

public abstract class AbstractBuild<P extends AbstractProject<P, B>, B extends AbstractBuild<P, B>>
		extends AbstractModel<P> implements Build<P, B> {
	protected P project;
	protected boolean finished;
	protected boolean started;
	protected int id;

	public void setId(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;
	}
	
	/**
	 * Creates a new build.
	 */
	protected AbstractBuild(P project, int id) throws IOException {
		super(project, Integer.toString(id));
		this.project = project;
	}

	public P getProject() {
		return this.project;
	}

	@Override
	public void build() {
		this.started = true;
		List<BuildStep<P,B>> buildSteps = project.getBuildSteps();
		for (BuildStep<P,B> step : buildSteps) {
			step.preBuild();
			step.perform();
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
	
	@Override
	public synchronized void save() throws IOException {
	}
}
