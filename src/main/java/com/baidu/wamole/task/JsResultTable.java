package com.baidu.wamole.task;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.baidu.wamole.browser.Browser;
import com.baidu.wamole.data.Exported;
import com.baidu.wamole.model.AbstractModel;
import com.baidu.wamole.model.JsProject;
import com.baidu.wamole.model.Kiss;
import com.baidu.wamole.xml.XmlFile;
import com.caucho.quercus.UnimplementedException;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class JsResultTable extends AbstractModel<JsProject> implements
		ResultTable {
	int fail;
	int total;
	long starttime;
	long endtime;
	/**
	 * 结果记录
	 */
	private ArrayList<JsResultBrowser> testsuites = new ArrayList<JsResultBrowser>();

	private transient DeadLine deadLine; // 超时处理
	private transient HashMap<String, JsResultBrowser> resultMap;
	private transient final JsBuild b;
	private transient final JsBuildStep bs;

	public JsResultTable(List<Browser> browsers, JsBuild b, JsBuildStep bs) {
		this.b = b;
		this.bs = bs;
		this.total = browsers.size() * b.getProject().getKisses(b.filter).size();
		Collection<Kiss> list = b.getProject().getKisses(b.filter);
		resultMap = newReport(browsers, list);
		deadLine = new DeadLine(20, list.size());
	}

	@Override
	public synchronized void store(Result result) {
		resultMap.get(result.getBrowser()).add(result);
		int count = 0;
		for (JsResultBrowser br : resultMap.values())
			count = count < br.waitingList.size() ? br.waitingList.size()
					: count;

		// 处理计数器
		total += result.getTotal();
		fail += result.getFail();
		deadLine.decrease(count);
	}

	@Override
	public boolean hasKiss(String executorName) {
		return resultMap.containsKey(executorName)
				&& resultMap.get(executorName).waitingList.size() > 0;
	}

	public synchronized Kiss getNextExcutableKiss(String executorName) {
		return resultMap.get(executorName).waitingList.pop();
	}

	private HashMap<String, JsResultBrowser> newReport(
			List<Browser> browserList, Collection<Kiss> kissList) {
		HashMap<String, JsResultBrowser> reportList = new HashMap<String, JsResultBrowser>();
		for (Browser browser : browserList)
			reportList.put(browser.getName(),
					new JsResultBrowser(browser.getName(), kissList));
		return reportList;
	}

	@Override
	public boolean isDead() {
		int count = 0;
		for (JsResultBrowser br : resultMap.values())
			count += br.waitingList.size();
		return deadLine.isDead() || count == 0;
	}

	@Override
	public File getRootDir() {
		throw new UnimplementedException();
	}

	@Override
	public XmlFile getConfigFile() {
		testsuites.addAll(resultMap.values());

		// XStream xs = new XStream();
		// xs.alias("testsuites", JsResultTable.class);
		// xs.alias("testsuite", JsResultBrowser.class);
		// xs.alias("testcase", Result.class);
		// xs.aliasAttribute(JsResultBrowser.class, "name", "browser");
		// xs.addImplicitCollection(JsResultTable.class, "testsuites");
		// xs.addImplicitCollection(JsResultBrowser.class, "testsuite");

		return new XmlFile(new File(b.getRootDir(), "jsunit.xml"));
	}
	
	@Exported
	@Override
	public long getEndtime() {
		return this.endtime;
	}
	
	@Exported
	@Override
	public long getStarttime() {
		return this.starttime;
	}
	
	@Exported
	public int getTotal(){
		return this.total;
	}
	
	@Exported
	public int getFail(){
		return this.fail;
	}
}
