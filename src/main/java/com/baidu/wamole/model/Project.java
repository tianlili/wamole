package com.baidu.wamole.model;

import java.util.Collection;

import com.baidu.wamole.exception.TestException;
import com.baidu.wamole.task.Build;

public interface Project<P extends Project<P, B>, B extends Build<P, B>> extends TopModel, ModelGroup {
	/**
	 * project alias
	 * 
	 * @return
	 */
	String getName();

	/**
	 * project base dir
	 * 
	 * @returnw
	 */
	String getPath();

	/**
	 * 根据名称获取具体用例
	 * 
	 * @param kissName
	 * @return
	 */
	Kiss getKiss(String kissName);

	/**
	 * 获取用例列表
	 * 
	 * @return
	 */
	Collection<Kiss> getKisses();

	/**
	 * 根据查询字符串返回可执行用例的列表页
	 * 
	 * @param searchString
	 * @return
	 */
	String getExecutePage(String searchString) throws TestException;

	Collection<B> getBuilds();

	void addBuild();
}
