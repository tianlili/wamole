package com.baidu.wamole.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.baidu.wamole.model.AbstractModel;
import com.baidu.wamole.model.AbstractProject;

public abstract class AbstractBuild<P extends AbstractProject<P, B>, B extends AbstractBuild<P, B>>
		extends AbstractModel<P> implements Build<P, B> {
//	protected P project;
	protected boolean finished;
	protected boolean started;
	protected int id;
	protected List<BuildStep<P,B>> buildSteps = new ArrayList<BuildStep<P,B>>();

	public void setId(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;
	}
	
	/**
	 * Creates a new build.
	 */
	protected AbstractBuild(P project, int id) {
		super(project, Integer.toString(id));
	}

	public P getProject() {
		return this.parent;
	}

	@Override
	public void build() {
		this.started = true;
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
