package com.baidu.wamole.model;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.baidu.wamole.exception.TestException;
import com.baidu.wamole.process.Processor;
import com.baidu.wamole.task.JsBuild;

@XmlRootElement
public class JsProject extends AbstractProject<JsProject, JsBuild>{

	public void addModel(JsBuild build) {};
	
	public JsProject(String name, String path) {
		super(name, path);
	}
	
	private boolean inited;
	private Processor<Kiss> processor;
	private List<JsBuild> builds;

	private Parser<Kiss, JsProject> parser;	
	
	public void addBuild() {
		try {
			builds.add(new JsBuild(this, getUsableBuildId()));
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
					parser.parse(this);
			} else {
				throw new TestException("tangram project init fail!");
			}
		}

		inited = true;
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

	@Override
	public File getRootDir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <TM extends Model> Collection<TM> getModels(Class<TM> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <TM extends Model> TM getModel(Class<TM> clazz, String name) {
		// TODO Auto-generated method stub
		return null;
	}
}
