package com.baidu.wamole.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.util.log.Log;

import com.baidu.wamole.browser.Browser;
import com.baidu.wamole.browser.BrowserManager;
import com.baidu.wamole.model.Kiss;
import com.baidu.wamole.model.Wamole;
import com.baidu.wamole.template.ConfigurationFactory;

import freemarker.template.Template;
import freemarker.template.TemplateException;

public class JsResultTable implements ResultTable {
	protected Result[][] results; // 结果
	protected List<Browser> browserList; // 浏览器列表
	protected List<Browser> activeList;// 活动的浏览器列表
	protected List<Kiss> kissList; // 用例列表
	protected List<String> kissNameList; //
	protected DeadLine deadLine; // 超时时间
	protected int[] currentIndex; // 当前执行index
	protected int[] successCount;

	public JsResultTable(Build<?, ?> b) {
		
		// TODO 提供项目定制浏览器列表，目前使用全部浏览器运行
		this(new ArrayList<Browser>(Wamole.getInstance()
				.getModel(BrowserManager.class).getBrowsers()),
				new ArrayList<Browser>(Wamole.getInstance()
						.getModel(BrowserManager.class).getBrowsers()),
				new ArrayList<Kiss>(b.getProject().getKisses()), 20);
		
		
	}

	public JsResultTable(List<Browser> browserList, List<Browser> activeList,
			List<Kiss> kissList, int interval) {
		this.browserList = browserList;
		this.kissList = kissList;
		converseKissName();
		results = new Result[browserList.size()][kissList.size()];
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

	@Override
	public synchronized Kiss store(Result result) {
		int browserIndex = getBrowserIndex(result.getBrowser());
		int kissIndex = getKissIndex(result.getName());
		results[browserIndex][kissIndex] = result;
		++successCount[browserIndex];
		decrease();
		return getNextExcutableKiss(result.getBrowser());
	}

//	/**
//	 * 对browser当前result添加一个空的结果
//	 * 
//	 * @param browser
//	 */
//	private synchronized void storeEmpty(String browser, int index) {
//		Result result = new Result();
//		result.setBrowser(browser);
//		result.setFail(0);
//		result.setTotal(0);
//		result.setTimeStamp(0);
//		result.setName(kissList.get(index).getName());
//		Log.debug("store empty case : " + result.getName());
//		int browserIndex = getBrowserIndex(result.getBrowser());
//		int kissIndex = getKissIndex(result.getName());
//		results[browserIndex][kissIndex] = result;
//		++successCount[browserIndex];
//		decrease();
//	}

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
//		System.out.println(least);
		return least;
	}

	private void decrease() {
//		System.out.println("decrease count : " + getLeastCount());
		deadLine.decrease(getLeastCount());
	}

	@Override
	public int getBrowserIndex(String browser) {
		if (browserList.contains(browser)) {
			return browserList.lastIndexOf(browser);
		} else
			return -1;
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

	public synchronized void save() throws IOException {
		Template template = ConfigurationFactory.getInstance().getTemplate(
				"report_xml.ftl");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("browsers", browserList);
		map.put("kisses", kissList);
		map.put("results", results);
		String root = System.getProperty("user.home") + "/.wamole";
		SimpleDateFormat dateformat1 = new SimpleDateFormat(
				"yyyy-MM-dd_HH-mm-ss");
		String filePath = root + "/result/Report_"
				+ dateformat1.format(new Date()) + ".xml";
		File file = new File(filePath);
		// 建立文件
		if (!file.exists()) {
			// 建立文件夹
			if (filePath.lastIndexOf("/") > 0) {
				filePath = filePath.substring(0, filePath.lastIndexOf("/"));
				File dir = new File(filePath);
				dir.mkdirs();
			}
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Writer writer = null;
		try {
			writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			template.process(map, writer);
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
