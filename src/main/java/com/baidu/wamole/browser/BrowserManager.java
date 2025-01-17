package com.baidu.wamole.browser;

import java.io.File;
import java.util.List;

import com.baidu.wamole.exception.TestException;
import com.baidu.wamole.model.AbstractModel;
import com.baidu.wamole.model.JsKiss;
import com.baidu.wamole.model.Wamole;
import com.baidu.wamole.task.JsBuildStep;
import com.baidu.wamole.task.Result;
import com.baidu.wamole.util.CopyOnWriteList;
import com.caucho.quercus.UnimplementedException;

public class BrowserManager extends AbstractModel<Wamole> {
	public static final String name = "browsers";
	private CopyOnWriteList<StaticBrowser> statics;
	private CopyOnWriteList<Browser> browsers;// = new CopyOnWriteList<Browser>();
	private JsBuildStep buildStep;

	public BrowserManager() {
		super(Wamole.getInstance(), name);
	}

	/**
	 * 浏览器刷新间隔，单位时间秒
	 */
	private int step = 4;

	private boolean autorun = true;

	public List<StaticBrowser> getStaticBrowsers() {
		return statics.getView();
	}

	public JsKiss notice(String id, Result result) throws TestException {
		getBrowser(id).notice();
		// 当务任务时
		if (null == buildStep) {
			return null;
			//当有任务时
		} else {
			// 当result有结果
			if (null != result && null != result.getName()) {
				buildStep.getResultTable().store(result);
			}
			// result无结果，当前任务中需要该浏览器进行测试
			if (buildStep.getResultTable().hasKiss(result.getBrowser())) {
				return (JsKiss) buildStep.getResultTable()
						.getNextExcutableKiss(result.getBrowser());
			} else {
				return null;
			}
		}
	}

	public Browser getBrowser(String id) {
		List<Browser> brs = getBrowsers();
		for (Browser br : brs) {
			if (br.getId().equals(id))
				return br;
		}
		return null;
	}

	public void removeBrowser(String id) {
		List<Browser> brs = getBrowsers();
		for (Browser br : brs) {
			if (br.getId().equals(id))
				browsers.remove(br);
		}
	}

	public List<Browser> getBrowsers() {
		if(browsers == null)
			browsers = new CopyOnWriteList<Browser>();
		return browsers.getView();
	}

	public boolean isStatic(Browser browser) {
		if (statics.size() > 0) {
			for (StaticBrowser sb : statics) {
				if (sb.getBrowser().isEqual(browser))
					return true;
			}
		}
		return false;
	}

	public void addBrowser(Browser browser) {
		browsers.add(browser);
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public boolean isAutorun() {
		return autorun;
	}

	public void setAutorun(boolean autorun) {
		this.autorun = autorun;
	}

	public void invokeAll() {
		// 根据浏览器配置，启动浏览器，并
		if (autorun) {
			new Thread("Browser Mananger invoker static browser thread!") {
				@Override
				public void run() {
					List<StaticBrowser> statics = getStaticBrowsers();
					if (statics.size() > 0) {
						for (StaticBrowser staticBrowser : statics) {
							try {
								staticBrowser.doStart();
							} catch (TestException e) {
								e.printStackTrace();
							}
						}
					}
					try {
						Thread.sleep(step * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					new TimeOutThread(getStaticBrowsers(), step).start();
				}
			}.start();
		}
	}

	public void setBuildStep(JsBuildStep buildStep) {
		this.buildStep = buildStep;
	}
	
	@Override
	public File getRootDir() {
		throw new UnimplementedException();
	}
}
