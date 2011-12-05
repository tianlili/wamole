package com.baidu.wamole.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.baidu.wamole.model.Project;
import com.baidu.wamole.util.FileUtil;

public class ProjectFileData implements FileData {

	@SuppressWarnings("rawtypes")
	Project project;
	String path;
	File file;
	boolean exe;
	String content;

	private ProjectFileData(@SuppressWarnings("rawtypes") Project project,
			String path) {
		this.project = project;
		// 屏蔽可能产生的影响
		this.file = new File(this.project.getPath(), path);
		this.path = this.file.isDirectory() && !path.endsWith("/") ? (path + "/")
				: path;
		if (this.isReadable()) {
			this.content = FileUtil.readFile(file);
		} else
			this.content = "";
	}

	@Exported(encode = true)
	public String getPath() {
		return this.path;
	}

	@Exported
	public boolean isReadable() {
		return this.file.isFile() && isReadable(this.path);
	}

	@Exported
	public boolean isExe() {
		return this.project.getKiss(this.path) != null;
	}

	@Exported
	public boolean isDir() {
		return this.file.isDirectory();
	}

	@Exported
	public long getLastModified() {
		return this.file.lastModified();
	}

	@Exported
	public int getSize() {
		return this.content.length();
	}

	@Exported(recurse = false, encode = true)
	public String getContent() {
		return this.content;
	}

	@Exported(recurse = false)
	public List<ProjectFileData> getChildren() {
		List<ProjectFileData> children = new ArrayList<ProjectFileData>();
		if (this.file.isDirectory())
			for (File f : this.file.listFiles())
				children.add(getData(project, this.path + f.getName()));
		return children;
	}

	public boolean equals(Object obj) {
		if (obj instanceof ProjectFileData) {
			ProjectFileData data = (ProjectFileData) obj;
			return data.project.equals(this.project)
					&& data.path.equals(this.path);
		}
		return false;
	}

	private static final ArrayList<ProjectFileData> cache = new ArrayList<ProjectFileData>();

	private static ProjectFileData getCache(
			@SuppressWarnings("rawtypes") Project project, String path) {
		ProjectFileData _data = null;
		File f = new File(project.getPath(), path);
		if (f.isDirectory() && !path.endsWith("/"))
			path += "/";
		for (ProjectFileData data : cache) {
			if (data.project.equals(project) && data.path.equals(path))
				_data = data;
		}
		return _data;
	}

	/**
	 * 缓存以提高效率
	 * 
	 * @param project
	 * @param path
	 * @return
	 */
	public static ProjectFileData getData(
			@SuppressWarnings("rawtypes") Project project, String path) {

		ProjectFileData _data = getCache(project, path);
		if (_data == null) {
			_data = new ProjectFileData(project, path);
			cache.add(_data);
		}
		return _data;
	}

	private static final String readableWhiteList = "_.htm_.html_.php_.js_.json_.css_.xml_.txt_";

	public static boolean isReadable(String name) {
		return name.indexOf(".") > 0
				&& readableWhiteList.indexOf("_"
						+ name.substring(name.lastIndexOf(".")) + "_") >= 0;
	}
}
