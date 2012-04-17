package com.fairbg.bezma.communication;

public class Logger {
	public static void writeln(Object sender, String log)
	{
		System.out.println((sender != null ? sender.toString() : "") + ": " + log);
	}
}
