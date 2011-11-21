package com.baidu.wamole.model;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.baidu.wamole.data.Exported;
import com.baidu.wamole.data.Imported;

public class TangramParser implements Parser<JsKiss, JsProject> {
//	private TangramParser(){};
	private static final TangramParser instance = new TangramParser();
	public static TangramParser getInstance(){
		return instance;
	}

	private String src;
	
	private String test;

	@Imported	
	public void setSrc(String src){
		this.src = src;
	}

	@Imported
	public void setTest(String test){
		this.test = test;
	}
	
	@Exported
	public String getSrc(){
		return this.src;
	}
	
	@Exported
	public String getTest(){
		return this.test;
	}
	
	@Override
	public Map<String, JsKiss> parse(JsProject project) {
		Map<String, JsKiss> kisses;
		kisses = new HashMap<String, JsKiss>();
		if(new File(project.getPath(), src).exists())
		parseDir(new File(project.getPath()+ src), project, src, test, kisses);
		return kisses;
	}

	private void parseDir(File file, JsProject project, String srcdir,
			String testdir, Map<String, JsKiss> kisses) {
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (!files[i].isDirectory()) {
				String s = transSeprator(files[i].getAbsolutePath());
				// 判断是否是js结尾
				if (s.endsWith(".js")) {
					// 判断文件是否存在
					if (new File(s.replace(srcdir, testdir)).exists()) {
						String testpath = s.replace(srcdir, testdir).replace(
								transSeprator(project.getPath()), "");
						kisses.put(testpath, new JsKiss(project, testpath));
					}
				}
			} else {
				parseDir(files[i], project, srcdir, testdir, kisses);
			}
		}
	}

	@Exported(encode = true)
	public static String getDescription() {
		return "提供类Tangram项目的解析方案，包括<li>源码依赖的解决方案<li>源码的code coverage解决方案<li>用例提取同源码目录及名称";
	}

	private static String transSeprator(String s) {
		return s.replace("\\", "/");
	}
}
