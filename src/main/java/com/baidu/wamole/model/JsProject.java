package com.baidu.wamole.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import com.baidu.wamole.data.Exported;
import com.baidu.wamole.exception.TestException;
import com.baidu.wamole.process.Processor;
import com.baidu.wamole.task.JsBuild;

@XmlRootElement
public class JsProject extends AbstractProject<JsProject, JsBuild> implements Savable{

	private Map<String, Kiss> kisses = null;
	private boolean inited;
	private Processor<Kiss> processor;
	private JsBuild build;

	private Parser<Kiss, JsProject> parser;

	public JsBuild getBuild() {
		return build;
	}

	@Exported
	public JsBuild getBuild(int id) {
		return getBuilds().get(id);
	}

	public void addBuild() {
		try {
			builds.add(new JsBuild(this));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 项目初始化
	 * 
	 * @throws TestException
	 */
	private void init() throws TestException {
		if (!inited) {
			// kisses = new ConcurrentHashMap<String, Kiss>();
			File root = new File(this.getPath());
			if (root.isDirectory()) {
				if (parser != null)
					kisses = parser.parse(this);
			} else {
				throw new TestException("tangram project init fail!");
			}
		}

		inited = true;
	}

	@Override
	public Kiss getKiss(String kissName) {
		if (!inited) {
			try {
				init();
			} catch (TestException e) {
				e.printStackTrace();
			}
		}
		return kisses == null ? null : (Kiss) kisses.get(kissName);
	}

	@Override
	public List<Kiss> getKisses() {
		if (!inited) {
			try {
				init();
			} catch (TestException e) {
				e.printStackTrace();
			}
		}
		return new ArrayList<Kiss>(kisses.values());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setParser(Parser parser) {
		this.parser = parser;
	}

	@Override
	public String getExecutePage(String searchString) throws TestException {
		Kiss kiss = this.getKiss(searchString);
		try {
			String s = processor.process(kiss);
			return s;
		} catch (Exception e) {
			throw new TestException("请检查配置信息是否正确，可能由于资源无法匹配导致.");
		}
	}
}
