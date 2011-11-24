package com.baidu.wamole;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.baidu.wamole.exception.ConfigException;
import com.baidu.wamole.model.Wamole;
import com.baidu.wamole.server.JettyServer;

public class Main {
	private final String root;

	public static void main(String[] args) throws Exception {
		Main main = new Main();
		main.loadConfig();
		try {
			main.startServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadConfig() {
		File file = new File(root);
		if (!file.exists() || !new File(root + "/config.xml").exists()) {
			initWamole();
		}

		try {
			new Wamole(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initWamole() {
		File file = new File(root);
		file.mkdirs();
		File configFile = new File(root + "/config.xml");
		try {
			FileOutputStream w = new FileOutputStream(configFile);
			// 当第一次建立时，建立一个config.xml
			w.write("<?xml version='1.0' encoding='UTF-8'?>".getBytes());
			w.write("<wamole></wamole>".getBytes());
			w.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startServer() throws Exception {
		JettyServer.setPort(8080);
		JettyServer.start();
	}

	{
		root = System.getProperty("user.home") + "/.wamole";
	}
}
