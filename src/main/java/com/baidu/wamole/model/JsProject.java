package com.baidu.wamole.model;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.baidu.wamole.exception.TestException;
import com.baidu.wamole.process.TangramProcessor;
import com.baidu.wamole.task.JsBuild;

@XmlRootElement
public class JsProject extends AbstractProject<JsProject, JsBuild> {

	public JsProject(String name, String path) {
		super(name, path);
	}

//	private Processor<Kiss> processor;
	private List<JsBuild> builds;

	private Parser<JsKiss> parser;

	public void addBuild() {
		try {
			builds.add(new JsBuild(this, getUsableBuildId()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setParser(Parser parser) {
		this.parser = parser;
		this.parser.parse(this);
	}

	@Override
	public Parser<JsKiss> getParser() {
		if(parser == null)
			parser = new TangramParser();
		return parser;
	}

	@Override
	public String getExecutePage(String searchString) throws TestException {
		Kiss kiss = this.getKiss(searchString);
		try {
			String s = new TangramProcessor().process(JsKiss.class.cast(kiss));
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			throw new TestException("请检查配置信息是否正确，可能由于资源无法匹配导致.");
		}
	}

}
