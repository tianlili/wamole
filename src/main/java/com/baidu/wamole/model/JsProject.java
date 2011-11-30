package com.baidu.wamole.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.baidu.wamole.exception.TestException;
import com.baidu.wamole.process.TangramProcessor;
import com.baidu.wamole.task.JsBuild;

@XmlRootElement
public class JsProject extends AbstractProject<JsProject, JsBuild> {

	public JsProject(String name, String path) {
		super(name, path);
	}

	@Override
	public void addBuild() {
		JsBuild build = new JsBuild(this, getUsableBuildId());
		this.addModel(build);
		Wamole.getInstance().getBuildQueue().addBuild(build);
	}

	private Parser<JsKiss> parser;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setParser(Parser parser) {
		this.parser = parser;
		this.parser.parse(this);
	}

	@Override
	public Parser<JsKiss> getParser() {
		if (parser == null)
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
			throw new TestException("请检查配置信息是否正确，可能由于资源无法匹配导致 : "+searchString);
		}
	}

}
