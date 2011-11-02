package com.baidu.wamole.model;

import com.baidu.wamole.data.Exported;
import com.baidu.wamole.data.URLEncode;


public abstract class AbstractProject implements Project {

	// 项目名称
	public String name;

	// 项目基础路径
	public String path;

	@Exported
	public String getName() {
		return this.name;
	}

	@Exported
	@URLEncode(enc = "UTF-8")
	public String getPath() {
		return this.path;
	}
}
