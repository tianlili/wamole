package com.baidu.wamole.task;

import com.baidu.wamole.model.Model;
import com.baidu.wamole.model.Project;

/**
 * 项目构建
 * @author dailiqi
 *
 * @param <B> Build类型
 * @param <P> Project类型
 */
public interface Build<P extends Project<P, B>, B extends Build<P, B>> extends Model{
	public P getProject();
	public void build();
	public boolean started();
	public boolean finished();
}
