package com.baidu.wamole.task;

import com.baidu.wamole.model.Kiss;
import com.baidu.wamole.model.Savable;

/**
 * 存储结果的2维表
 * 
 * @author dailiqi
 */
public interface ResultTable extends Savable{
	/**
	 * 存储一个结果，并且对超时时间减少一个interval
	 * 
	 * @param result
	 */
	public void store(Result result);

	/**
	 * 获取下个需要执行的kiss
	 * 
	 * @param browser 浏览器种类
	 * @return
	 */
	public Kiss getNextExcutableKiss(String executorName);

	public boolean isDead();
	
	public boolean hasKiss(String executorName);
	
	public int getTotal();
	public int getFail();
	public long getStarttime();
	public long getEndtime();
}
