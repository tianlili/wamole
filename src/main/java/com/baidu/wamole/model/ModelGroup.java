package com.baidu.wamole.model;

import java.util.Collection;

public interface ModelGroup extends Persistence {

	/**
	 * 获取特定类型的model，readonly
	 * 
	 * @param clazz
	 * @return
	 */
	<M extends Model> Collection<M> getModels(Class<M> clazz);

	// /**
	// * 获取一个model
	// * @param name
	// * @return
	// */
	// M getModel(String name);

	<M extends Model> M getModel(Class<M> clazz, String name);
	
	<M extends Model> void addModel(M m);
}
