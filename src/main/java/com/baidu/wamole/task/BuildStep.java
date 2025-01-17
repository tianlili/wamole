package com.baidu.wamole.task;

import com.baidu.wamole.model.Project;

/**
 * 项目构建器，此类型的所有配置直接保存于Build的配置中
 * 
 * @author dailiqi
 * @param <B>
 *            Build类型
 */
public interface BuildStep<P extends Project<P, B>, B extends Build<P, B>>{

	public B getBuild();
	
	public P getProject();
	
	public boolean preBuild();

	public boolean perform();
}
