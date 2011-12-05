package com.baidu.wamole.task;

import java.util.Date;

import org.eclipse.jetty.util.log.Log;
/**
 * 报告的超时时间
 * 
 * @author dailiqi
 */
public class DeadLine {
	
	private int interval;// 每个结果最多等待时间，单位时间秒
	private long deadline;// 总体超时时间
	private int count;
	private long buffer;
	private int size;

	/**
	 * Constructor
	 * 
	 * @param interval
	 */
	public DeadLine(int interval, int size) {
		this.interval = interval*1000;
		long now = System.currentTimeMillis();
		buffer = this.interval * 10;
		this.size = size;
		this.deadline = now + this.interval * size + buffer;
		Log.info("constructor, deadline : " + new Date(this.deadline)
				+ ",\n now : " + new Date(now));
	}

	/**
	 * 是否超时
	 * 
	 * @return
	 */
	public boolean isDead() {
		Log.info("isDead now      : "
				+ new Date(System.currentTimeMillis()));
		Log.info("isDead deadline : " + new Date(deadline));
		return this.count > this.size || System.currentTimeMillis() > deadline;
	}

	/**
	 * 尝试减少deadline的大小
	 * 
	 * @param count 当前最小count项
	 * @return
	 */
	public long decrease(int count) {
		Log.info("this.count : " + this.count + ", count : "
				+ count);
		if (count > this.count) {
			this.count = count;
			deadline = System.currentTimeMillis() + buffer + interval
					* (size - count);
		}
		// if (count > this.count) {
		// deadline = deadline - interval * 1000 * (count - this.count);
		// Log.info("isDead now      : " + new
		// Date(System.currentTimeMillis()));
		// Log.info("isDead deadline : " + new Date(deadline));
		// this.count = count;
		// }
		// deadline = deadline - interval * 1000;
		return deadline;
	}
}
