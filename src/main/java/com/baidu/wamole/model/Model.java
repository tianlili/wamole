package com.baidu.wamole.model;

public interface Model extends Persistence, Savable{
	String getName();
	
	void setName(String name);
	
	/**
	 * 从文件存储的对象不包含父对象关系，此处需要从外部进行设置
	 * @param parent
	 */
	void setParent(ModelGroup parent);
}
