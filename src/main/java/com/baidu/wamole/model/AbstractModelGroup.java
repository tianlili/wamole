package com.baidu.wamole.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class AbstractModelGroup<Parent extends ModelGroup>
		extends AbstractModel<Parent> implements ModelGroup {

	protected AbstractModelGroup(Parent parent, String name) {
		super(parent, name);
	}

	protected List<Model> models = new ArrayList<Model>();

	/**
	 * 获取一种类型的model，无法在此处直接追加新实例
	 * 
	 * @param clazz
	 * @return
	 */
	public <M extends Model> Collection<M> getModels(Class<M> clazz) {
		ArrayList<M> list = new ArrayList<M>();
		for (Model o : models) {
			if (clazz.isInstance(o))
				list.add(clazz.cast(o));
		}
		return Collections.unmodifiableCollection(list);
	}

	/**
	 * 根据类型和名称获取特定实例
	 * 
	 * @param clazz
	 * @param name
	 * @return
	 */
	public <M extends Model> M getModel(Class<M> clazz, String name) {
		for (M m : getModels(clazz))
			if (m.getName().equals(name))
				return m;
		return null;
	}
	
	public <M extends Model> M getModel(Class<M> clazz){
		return getModels(clazz).iterator().next();
	}
	
	public <M extends Model> void addModel(M m) {
		models.add(m);
	}
}
