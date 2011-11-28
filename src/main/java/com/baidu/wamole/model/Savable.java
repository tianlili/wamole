package com.baidu.wamole.model;

import java.io.IOException;

public interface Savable {
	void save() throws IOException;
	
	public static class NOOP implements Savable{
		@Override
		public void save() throws IOException {
			//do nothing
		}
	}
}
