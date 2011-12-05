package com.baidu.wamole.model;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface ModelGroup extends Persistence {

	/**
	 * 获取所有模型
	 * @return
	 */
	List<Model> getModels();
	
	/**
	 * 获取特定类型的model，readonly
	 * 
	 * @param clazz
	 * @return
	 */
	<M extends Model> Collection<M> getModels(Class<M> clazz);

	<M extends Model> M getModel(Class<M> clazz, String name);
	
	<M extends Model> void addModel(M m);
	
	void load() throws IOException;
}
