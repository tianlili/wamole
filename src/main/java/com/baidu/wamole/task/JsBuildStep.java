package com.baidu.wamole.task;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.eclipse.jetty.util.log.Log;

import com.baidu.wamole.browser.Browser;
import com.baidu.wamole.browser.BrowserManager;
import com.baidu.wamole.model.JsProject;
import com.baidu.wamole.model.Wamole;

public class JsBuildStep extends AbstractBuildStep<JsProject, JsBuild> {
	private transient JsResultTable resultTable;

	public JsBuildStep(JsBuild build) {
		this.build = build;
		this.project = build.getProject();
	}

	/**
	 * 此方法用于确认用例列表和浏览器列表并定制一个执行结果对象
	 */
	@Override
	public boolean preBuild() {
		List<Browser> bs = Wamole.getInstance().getModel(BrowserManager.class)
				.getBrowsers();
		// TODO 后续追加浏览器配置并设置
		resultTable = new JsResultTable(bs, build, this);

		return bs.size() > 0;
	}

	public ResultTable getResultTable() {
		return this.resultTable;
	}

	@Override
	public boolean perform() {
		Wamole.getInstance().getModel(BrowserManager.class).setBuildStep(this);
		resultTable.starttime = new Date().getTime();
		while (!resultTable.isDead()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			Log.info("dead and save");
			resultTable.endtime = new Date().getTime();
			this.resultTable.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Wamole.getInstance().getModel(BrowserManager.class).setBuildStep(null);
		return true;
	}
}
