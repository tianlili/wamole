package com.baidu.wamole.model;

public interface Kiss {
	/**
	 * 提供用例唯一标示，此字段与{Parser}中产生，在processor和Build中用于过滤和执行
	 * 
	 * @return
	 */
	String getName();
}
