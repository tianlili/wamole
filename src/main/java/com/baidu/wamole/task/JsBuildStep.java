package com.baidu.wamole.task;

import java.io.IOException;

import com.baidu.wamole.model.JsProject;
import com.baidu.wamole.model.Wamole;

public class JsBuildStep implements BuildStep<JsProject, JsBuild> {
	private JsResultTable resultTable;
	JsBuild build;
	JsProject project;
	public JsBuildStep(JsBuild build) {
		this.build = build;
	}

	@Override
	public JsBuild getBuild() {
		return null;
	}

	@Override
	public JsProject getProject() {
		return project;
	}
	
	/**
	 * 此方法用于确认用例列表和浏览器列表并定制一个执行结果对象
	 */
	@Override
	public boolean preBuild() {
		resultTable = new JsResultTable(build);
		
		return  Wamole.getInstance().getBrowserManager().getBrowsers().size() > 0;
	}

	public ResultTable getResultTable() {
		return this.resultTable;
	}

	@Override
	public boolean perform() {
		Wamole.getInstance().getBrowserManager().setBuildStep(this);
		while (!resultTable.isDead()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			this.resultTable.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Wamole.getInstance().getBrowserManager().setBuildStep(this);
		return true;
	}
//
//	/**
//	 * 根据配置信息配置，检测当前active的浏览器，将浏览器遍历出来
//	 * 
//	 * @param list
//	 * @return
//	 */
//	private List<String> config(List<Browser> list) {
//		List<String> result = new ArrayList<String>();
//		for (Browser browser : list) {
//			if (browserList.contains(browser.getName().toLowerCase())) {
//				if (browser.getName().toLowerCase().equals("msie")) {
//					result.add(browser.getName() + browser.getVersion());
//				} else {
//					result.add(browser.getName());
//				}
//			}
//		}
//		return result;
//	}
}
