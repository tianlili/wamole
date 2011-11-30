package com.baidu.wamole.task;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

import com.baidu.wamole.model.AbstractModel;
import com.baidu.wamole.model.Wamole;

public class BuildQueue extends AbstractModel<Wamole>{
	public BuildQueue() {
		super(Wamole.getInstance(), name);
	}

	private Queue<Build<?,?>> queue = new LinkedList<Build<?,?>>();
	private Build<?,?> current;

	public void addBuild(Build<?,?> build) {
		this.queue.add(build);
		if (current == null) {
			this.current = build;
		}
	}
	
	public Queue<Build<?,?>> getQueue(){
		return this.queue;
	}

	public Build<?,?> getCurrent() {
		return this.current;
	}

	@Override
	public File getRootDir() {
		return getParent().getRootDir();
	}

	public static final String name = "queue";
}
