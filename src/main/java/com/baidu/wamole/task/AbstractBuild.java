package com.baidu.wamole.task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.jetty.util.log.Log;

import com.baidu.wamole.data.Exported;
import com.baidu.wamole.model.AbstractModel;
import com.baidu.wamole.model.AbstractProject;

public abstract class AbstractBuild<P extends AbstractProject<P, B>, B extends AbstractBuild<P, B>>
		extends AbstractModel<P> implements Build<P, B> {
	// protected P project;
	protected boolean finished;
	protected boolean started;
	protected int id;
	protected List<BuildStep<P, B>> buildSteps = new ArrayList<BuildStep<P, B>>();
	protected long starttime;
	protected long endtime;

	@Exported
	public int getId() {
		return this.id;
	}

	/**
	 * Creates a new build.
	 */
	protected AbstractBuild(P project, int id) {
		super(project, Integer.toString(id));
		this.id = id;
		this.starttime = new Date().getTime();
	}

	public P getProject() {
		return this.parent;
	}

	@Override
	public void build() {
		this.started = true;
		for (BuildStep<P, B> step : buildSteps) {
			step.preBuild();
			step.perform();
		}
		this.finished = true;
		this.endtime = new Date().getTime();
		try {
			this.save();
		} catch (IOException e) {
			Log.info("build saved cause IOException", e);
		}
	}

	@Exported
	@Override
	public boolean finished() {
		return this.finished;
	}
	
	@Exported
	public long getStarttime(){
		return this.starttime;
	}
	
	@Exported
	public long getEndtime(){
		return this.endtime;
	}

	@Override
	public boolean started() {
		return this.started;
	}

	@Override
	public File getRootDir() {
		return new File(this.getProject().getRootDir(), "builds/" + id);
	}
}
