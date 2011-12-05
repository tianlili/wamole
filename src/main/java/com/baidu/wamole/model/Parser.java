package com.baidu.wamole.model;

import java.util.Map;

public interface Parser<K extends Kiss> {
	public Map<String ,Kiss> parse(Project<?, ?> project);
}
