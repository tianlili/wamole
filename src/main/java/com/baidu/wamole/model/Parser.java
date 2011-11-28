package com.baidu.wamole.model;

import java.util.Map;

public interface Parser<K extends Kiss, P extends Project> {
	public Map<String ,K> parse(P project);
}
