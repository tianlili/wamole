package com.baidu.wamole.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.util.log.Log;

import com.baidu.wamole.browser.Browser;
import com.baidu.wamole.model.AbstractModel;
import com.baidu.wamole.model.JsKiss;
import com.baidu.wamole.model.JsProject;
import com.baidu.wamole.model.Kiss;
import com.baidu.wamole.xml.DefaultXStream;
import com.baidu.wamole.xml.XmlFile;

public class JsResultTable extends AbstractModel<JsProject> implements
		ResultTable {
	protected transient Result[][] results; // 结果
	protected transient List<String> browserList; // 浏览器列表
	protected transient List<String> activeList;// 活动的浏览器列表
	protected transient List<Kiss> kissList; // 用例列表
	protected transient List<String> kissNameList; //
	protected transient DeadLine deadLine; // 超时时间
	protected transient int[] currentIndex; // 当前执行index
	protected transient int[] successCount;
	
	protected BrowserReport[] report;

	protected transient final JsBuild b;

	public JsResultTable(List<Browser> bs, JsBuild b) {
		// TODO 提供项目定制浏览器列表，目前使用全部浏览器运行
		this(bs, bs, new ArrayList<Kiss>(b.getProject().getKisses()), 20, b);
		this.parent = b.getProject();
	}

	private JsResultTable(List<Browser> browserList, List<Browser> activeList,
			List<Kiss> kissList, int interval, JsBuild b) {
		this.b = b;
		this.browserList = convertBrowser(browserList);
		this.activeList = convertBrowser(activeList);
		this.kissList = kissList;
		converseKissName();
		results = new Result[browserList.size()][kissList.size()];
		report = new BrowserReport[browserList.size()];
		currentIndex = new int[browserList.size()];
		successCount = new int[browserList.size()];
		for (Browser browser : browserList) {
			// 如果注册浏览器当前并无可用浏览器，则将该浏览器count直接置为最高。
			if (!activeList.contains(browser)) {
				successCount[browserList.indexOf(browser)] = kissList.size() + 1;
			}
		}
		deadLine = new DeadLine(interval, kissList.size());
		decrease();
	}

	private void converseKissName() {
		kissNameList = new ArrayList<String>();
		for (int i = 0; i < kissList.size(); i++) {
			kissNameList.add(kissList.get(i).getName());
		}
	}

	private List<String> convertBrowser(List<Browser> browsers) {
		List<String> list = new ArrayList<String>();
		for (Browser b : browsers)
			list.add(b.getName());
		return list;
	}

	@Override
	public synchronized Kiss store(Result result) {		
		int browserIndex = getBrowserIndex(result.getBrowser());
		int kissIndex = getKissIndex(result.getName());
		results[browserIndex][kissIndex] = result;
		if(report[browserIndex] == null)
			report[browserIndex] = new BrowserReport(browserList.get(browserIndex));
		report[browserIndex].add(new CaseReport(result));
		++successCount[browserIndex];
		decrease();
		return getNextExcutableKiss(result.getBrowser());
	}

	@Override
	public int getBrowserIndex(String browser) {
		return browserList.indexOf(browser);
	}

	// /**
	// * 对browser当前result添加一个空的结果
	// *
	// * @param browser
	// */
	// private synchronized void storeEmpty(String browser, int index) {
	// Result result = new Result();
	// result.setBrowser(browser);
	// result.setFail(0);
	// result.setTotal(0);
	// result.setTimeStamp(0);
	// result.setName(kissList.get(index).getName());
	// Log.debug("store empty case : " + result.getName());
	// int browserIndex = getBrowserIndex(result.getBrowser());
	// int kissIndex = getKissIndex(result.getName());
	// results[browserIndex][kissIndex] = result;
	// ++successCount[browserIndex];
	// decrease();
	// }

	/**
	 * 获取各个浏览器成功执行了多少Case
	 * 
	 * @return
	 */
	private int getLeastCount() {
		int least = successCount[0];
		for (int i = 0; i < successCount.length; i++) {
			if (successCount[i] < least) {
				least = successCount[i];
			}
		}
		// System.out.println(least);
		return least;
	}

	private void decrease() {
		// System.out.println("decrease count : " + getLeastCount());
		deadLine.decrease(getLeastCount());
	}

	@Override
	public int getKissIndex(String kissName) {
		if (kissNameList.contains(kissName)) {
			return kissNameList.lastIndexOf(kissName);
		} else
			return -1;
	}

	@Override
	public Result getResult(int browserIndex, int kissIndex) {
		return results[browserIndex][kissIndex];
	}

	// @Override
	public synchronized Kiss getNextExcutableKiss(String browser) {
		int index = browserList.lastIndexOf(browser);
		int current = currentIndex[index];
		Kiss kiss = null;
		// 如果kiss列表中还有可
		if (current < kissList.size()) {
			kiss = kissList.get(current);
		}
		currentIndex[index] = ++current;
		Log.debug("browser : " + browser + ",next case:" + kiss);
		return kiss;
	}

	@Override
	public boolean isDead() {
		return deadLine.isDead();
	}

	// public synchronized void save() throws IOException {
	// Template template = ConfigurationFactory.getInstance().getTemplate(
	// "report_xml.ftl");
	// Map<String, Object> map = new HashMap<String, Object>();
	// map.put("browsers", browserList);
	// map.put("kisses", kissList);
	// map.put("results", results);
	// String root = System.getProperty("user.home") + "/.wamole";
	// SimpleDateFormat dateformat1 = new SimpleDateFormat(
	// "yyyy-MM-dd_HH-mm-ss");
	// String filePath = root + "/result/Report_"
	// + dateformat1.format(new Date()) + ".xml";
	// File file = new File(filePath);
	// // 建立文件
	// if (!file.exists()) {
	// // 建立文件夹
	// if (filePath.lastIndexOf("/") > 0) {
	// filePath = filePath.substring(0, filePath.lastIndexOf("/"));
	// File dir = new File(filePath);
	// dir.mkdirs();
	// }
	// try {
	// file.createNewFile();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// Writer writer = null;
	// try {
	// writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
	// } catch (UnsupportedEncodingException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (FileNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// try {
	// template.process(map, writer);
	// } catch (TemplateException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	@Override
	public File getRootDir() {
		return b.getRootDir();
	}
	
	@Override
	public XmlFile getConfigFile() {
		DefaultXStream xs = DefaultXStream.getInstance();
		xs.alias("testsuites", getClass());
		xs.alias("testSuite", BrowserReport.class);
		xs.alias("testcase", CaseReport.class);
		return new XmlFile(xs, new File(getRootDir(), "jsunit.xml"));
	}
	
	class BrowserReport extends ArrayList<CaseReport>{
		String name;
		public BrowserReport(String name) {
			this.name = name;
		}
	}
	
	class CaseReport {
		String name;
		public CaseReport(Result result) {
			this.name = result.getName();
		}
	}
}