package com.baidu.wamole.xml;

import com.baidu.wamole.browser.BrowserManager;
import com.baidu.wamole.model.Wamole;
import com.baidu.wamole.task.BuildQueue;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;

public class DefaultXStream extends XStream {
	private DefaultXStream() {
		super();
		alias("wamole", Wamole.class);
		alias("browsers", BrowserManager.class);
		alias("queue", BuildQueue.class);
		init();
	}

	private DefaultXStream(HierarchicalStreamDriver hierarchicalStreamDriver) {
		super(hierarchicalStreamDriver);
		init();
	}

	private void init() {
		// 添加注册converter
		registerConverter(new ConcurrentHashMapConverter(getMapper(),
				getReflectionProvider()));
		registerConverter(new XmlConverter(getMapper()));
	}
	
	public static DefaultXStream getInstance(){
		return new DefaultXStream();
	}
}
