package com.baidu.wamole.model;

import java.io.IOException;

import javax.xml.bind.annotation.XmlRootElement;

import com.baidu.wamole.exception.TestException;
import com.baidu.wamole.process.TangramProcessor;
import com.baidu.wamole.task.BuildQueue;
import com.baidu.wamole.task.JsBuild;

@XmlRootElement
public class JsProject extends AbstractProject<JsProject, JsBuild> {

	public JsProject() {
	}

	public JsProject(String name, String path) {
		this.parent = Wamole.getInstance();
		this.name = name;
		this.path = path;
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
		if (kiss == null)
			return null;
		try {
			String s = new TangramProcessor().process(JsKiss.class.cast(kiss));
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			throw new TestException("请检查配置信息是否正确，可能由于资源无法匹配导致 : "
					+ searchString);
		}
	}

	public void addBuild() {
		JsBuild build = new JsBuild(this, getUsableBuildId());
		this.getModels().add(build);
		Wamole.getInstance().getModel(BuildQueue.class).addBuild(build);
		try {
			build.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
