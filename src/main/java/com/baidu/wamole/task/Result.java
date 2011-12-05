package com.baidu.wamole.task;

/**
 * testcase
 * <ul>字段列表
 * <li>
 * @author yangbo
 *
 */
public class Result {
	private String name;
	private int failure;
	private int error = 0;
	private int total;
	private float time;
	
	private transient String browser;
	private transient String task;
	private transient boolean isError;

	@Override
	public String toString() {
		return "result: name = " + name + "	fail:" + failure + "total:" + total;
	}

	public String getName() {
		return name;
	}

	public int getTotal() {
		return total;
	}

	public int getFail() {
		return failure;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFail(int fail) {
		this.failure = fail;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public float getTimeStamp() {
		return time;
	}

	/**
	 * 记录时长
	 * @param timeStamp 单位毫秒
	 */
	public void setTimeStamp(long timeStamp) {
		this.time = ((float)timeStamp)/1000;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public boolean isError() {
		return isError;
	}

}
