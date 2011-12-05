package com.baidu.wamole.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;

import com.baidu.wamole.model.Kiss;

/**
 * testsuite
 * <ul>
 * 字段列表
 * <li>name
 * <li>tests
 * <li>failures
 * <li>errors
 * <li>time
 * <li>package
 * 
 * @author yangbo
 * 
 */
public class JsResultBrowser {

	/**
	 * browser name
	 */
	private String name;
	/**
	 * 用例数
	 */
	private int tests;
	/**
	 * 失败用例数
	 */
	private int failures;
	private int errors = 0;
	/**
	 * 校验点数
	 */
	private int total;
	private float time = 0;
	private long starttime;

	private ArrayList<Result> testsuite = new ArrayList<Result>();

	public final transient LinkedList<Kiss> waitingList = new LinkedList<Kiss>();

	JsResultBrowser(String name, Collection<Kiss> list) {
		this.name = name;
		this.tests = list.size();
		this.total = 0;
		this.starttime = new Date().getTime();
		waitingList.addAll(list);
		Collections.sort(waitingList, new Comparator<Kiss>() {
			@Override
			public int compare(Kiss o1, Kiss o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
	}

	void add(Result report) {
		this.testsuite.add(report);
		this.failures += report.isError() ? 1 : 0;
		this.total += report.getTotal();
		this.time += report.getTimeStamp();
	}
}
