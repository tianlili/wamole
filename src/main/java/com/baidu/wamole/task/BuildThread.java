package com.baidu.wamole.task;

import java.util.Queue;

import com.baidu.wamole.model.Wamole;

public class BuildThread extends Thread {
	public void run() {
		while (true) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Queue<Build<?,?>> queue = Wamole.getInstance()
					.getBuildQueue().getQueue();
			if (!queue.isEmpty()) {
				System.out.println("queue running "+queue.size());
				Build<?,?> build = queue.peek();
				if (build.started()) {
					if (build.finished()) {
						queue.remove();
						if (queue.peek() != null) {
							queue.peek().build();
						}
					}
				} else {
					build.build();
				}
			}
		}
	}
}