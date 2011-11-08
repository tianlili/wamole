package com.baidu.wamole.model;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TangramParser implements Parser<JsKiss, JsProject> {
	private String srcdir;
	private String testdir;
	private Map<String, JsKiss> kisses;

	@Override
	public Map<String, JsKiss> parse(JsProject project) {
		kisses = new HashMap<String, JsKiss>();
		if (srcdir == null) {
			srcdir = "/src";
		}
		if (testdir == null) {
			testdir = "/test";
		}
		if (transSeprator(project.getPath()).endsWith("/")) {
			srcdir = transSeprator(project.getPath() + srcdir.substring(1));
			testdir = transSeprator(project.getPath() + srcdir.substring(1));
		} else {
			srcdir = transSeprator(project.getPath() + srcdir);
			testdir = transSeprator(project.getPath() + testdir);
		}
		parseDir(new File(srcdir), project);
		return kisses;
	}

	private void parseDir(File file, JsProject project) {
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (!files[i].isDirectory()) {
				String s = transSeprator(files[i].getAbsolutePath());
				//判断是否是js结尾
				if (s.endsWith(".js")) {
					//判断文件是否存在
					if(new File(s.replace(srcdir, testdir)).exists() ) {
						String testpath = s.replace(srcdir, testdir).replace(transSeprator(project.getPath()), "");
//						logger.debug("tangram parser parsed testcase path :" + testpath);
						kisses.put(testpath, new JsKiss(project, testpath));
					}
				}
			} else {
				parseDir(files[i], project);
			}
		}
	}

	private static String transSeprator(String s) {
		return s.replace("\\", "/");
	}
}
