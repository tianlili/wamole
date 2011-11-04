package com.baidu.wamole.data;

import java.util.List;

public interface FileData {
	String getPath();
	boolean isExe();
	boolean isDir();
	List<? extends FileData> getChildren();
}
