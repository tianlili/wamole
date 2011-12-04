package com.baidu.wamole.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import com.baidu.wamole.model.Kiss;

public class JsResultBrowser {

	/**
	 * browser name
	 */
	private String name;

	int total;

	int fail;

	private ArrayList<Result> testsuite = new ArrayList<Result>();

	public final transient LinkedList<Kiss> waitingList = new LinkedList<Kiss>();

	JsResultBrowser(String name, Collection<Kiss> list) {
		this.name = name;
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
		this.total += report.getTotal();
		this.fail += report.getFail();
	}
}
