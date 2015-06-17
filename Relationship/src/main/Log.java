package main;

import java.io.PrintStream;

public class Log {

	public static void out(String msg, String value) {
		System.out.println("\n===================================================================================");
		System.out.println(msg);
		System.out.println("<=>");
		System.out.println(value);
		System.out.println("-----------------------------------------------------------------------------------");
	}
	public static void out(PrintStream printStream, String msgSingle) {
		System.setOut(printStream);
		System.out.println("\n===================================================================================");
		System.out.println(msgSingle);
		System.out.println("-----------------------------------------------------------------------------------");
	}
	public static void out(String msgSingle) {
		System.out.println("\n===================================================================================");
		System.out.println(msgSingle);
		System.out.println("-----------------------------------------------------------------------------------");
	}
	public static void out(boolean bool) {
		if(bool)
			Log.out("Boolean value: true");
		else
			Log.out("Boolean value: false");
	}
	public static void out(String msg, long value) {
		System.out.println("\n===================================================================================");
		System.out.println(msg);
		System.out.println("<=>");
		System.out.println(value);
		System.out.println("-----------------------------------------------------------------------------------");
	}
	public static void err(String msg) {
		System.err.println(msg);
	}
	public static void err(double value) {
		System.err.println("\n" + value + "\n");
	}
	public static void err(String msg, double value) {
		System.err.println("\n" + msg + ": " + value + "\n");
	}
		
	
}
