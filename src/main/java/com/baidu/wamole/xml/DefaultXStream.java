package com.baidu.wamole.xml;

import com.baidu.wamole.browser.BrowserManager;
import com.baidu.wamole.model.Wamole;
import com.baidu.wamole.task.BuildQueue;
import com.baidu.wamole.task.JsBuildStep;
import com.baidu.wamole.task.JsResultBrowser;
import com.baidu.wamole.task.JsResultTable;
import com.baidu.wamole.task.Result;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;

public class DefaultXStream extends XStream {
	private DefaultXStream() {
		super();
		alias("wamole", Wamole.class);
		alias("browsers", BrowserManager.class);
		alias("queue", BuildQueue.class);
		alias("testsuites", JsResultTable.class);
		alias("testsuite", JsResultBrowser.class);
		alias("testcase", Result.class);
		alias("jsunit", JsBuildStep.class);
		
		aliasAttribute(JsResultBrowser.class, "name", "name");
		aliasAttribute(JsResultBrowser.class, "tests", "tests");
		aliasAttribute(JsResultBrowser.class, "failures", "failures");
		aliasAttribute(JsResultBrowser.class, "errors", "errors");
		aliasAttribute(JsResultBrowser.class, "time", "time");
		aliasAttribute(JsResultBrowser.class, "total", "total");
		aliasAttribute(JsResultBrowser.class, "starttime", "starttime");
		aliasAttribute(Result.class, "name", "name");
		aliasAttribute(Result.class, "time", "time");
		aliasAttribute(Result.class, "error", "error");
		aliasAttribute(Result.class, "failure", "failure");
		aliasAttribute(Result.class, "total", "total");
		
		addImplicitCollection(JsResultTable.class, "testsuites");
		addImplicitCollection(JsResultBrowser.class, "testsuite");
		
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
