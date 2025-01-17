package com.baidu.wamole.model;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.baidu.wamole.data.Exported;
import com.baidu.wamole.data.Imported;
import com.baidu.wamole.util.AntPathMatcher;

public class AntPathParser implements Parser<DefaultKiss<?>> {
	private String casepattern;
	private String filepath;
	private AntPathMatcher matcher;
	private Map<String, DefaultKiss<?>> kisses;
	
	@Imported
	public void setCasepattern(String pattern){
		this.casepattern = pattern;
	}

	@Override
	public Map<String, Kiss> parse(Project<?,?> project) {
		kisses = new HashMap<String, DefaultKiss<?>>();
		String filepath = project.getPath();
		parse(project, new File(filepath));
		return null;
	}

	/**
	 * 递归解析项目资源
	 * 
	 * @param file
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void parse(Project<?,?> project, File file) {
		File[] files = file.listFiles();
		for (File file2 : files) {
			if (file2.isDirectory()) {
				this.parse(project, file2);
			} else {
				String absolutePath = file2.getAbsolutePath();
				String relativePath = absolutePath.substring(new File(filepath)
						.getAbsolutePath().length());
				relativePath = relativePath.replace("\\", "/");
				if (matcher.match(casepattern, relativePath)) {
					kisses.put(relativePath, new DefaultKiss(project,
							relativePath));
				}
			}

		}
	}

	@Exported()
	public static String getDescription() {
		return "类Ant的路径解析方案";
	}
}
