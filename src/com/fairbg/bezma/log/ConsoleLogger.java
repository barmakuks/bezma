package com.fairbg.bezma.log;

public class ConsoleLogger implements ILogger {

	@Override
	public void i(String tag, String text) {
		System.out.println("INFO:" + tag + " : " + text);
	}

	@Override
	public void e(String tag, String text) {
		System.out.println("ERROR:" + tag + " : " + text);
	}

}
