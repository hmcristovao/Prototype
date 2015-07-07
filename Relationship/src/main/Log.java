package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintStream;

public class Log {
	public static BufferedWriter fileCompleteReport;
	public static BufferedWriter fileShortReport;
	public static PrintStream printStreamError;

	public static void init() throws Exception {
		Log.fileCompleteReport = new BufferedWriter(new FileWriter(Config.nameFileCompletReport));
		Log.fileShortReport    = new BufferedWriter(new FileWriter(Config.nameFileShortReport));
		Log.printStreamError   = new PrintStream(Config.nameFileConsoleError);
		System.setErr(printStreamError);
	}
	public static void close() throws Exception {
		Log.fileCompleteReport.close();
		Log.fileShortReport.close();
		Log.printStreamError.close();
	}
	public static void outFileCompleteReport(String msgSingle) throws Exception {
		fileCompleteReport.write(Config.doubleLine);
		fileCompleteReport.write(msgSingle);
		fileCompleteReport.write("\n");
	}
	public static void outFileShortReport(String msgSingle) throws Exception {
		fileShortReport.write(Config.doubleLine);
		fileShortReport.write(msgSingle);
		fileShortReport.write("\n");
	}
	public static void console(String msg, String value) {
		System.out.print(Config.doubleLine);
		System.out.println(msg);
		System.out.println("<=>");
		System.out.println(value);
		System.out.print(Config.singleLine);
	}
	public static void console(boolean bool) {
		if(bool)
			Log.console("Boolean value: true");
		else
			Log.console("Boolean value: false");
	}
	public static void console(String msg, long value) {
		System.out.print(Config.doubleLine);
		System.out.println(msg);
		System.out.println("<=>");
		System.out.println(value);
		System.out.print(Config.singleLine);
	}
	public static void console(String msg) {
		System.out.print(msg);
	}
	public static void console(double value) {
		System.out.print("\n" + value + "\n");
	}
	public static void console(String msg, double value) {
		System.out.print("\n" + msg + ": " + value + "\n");
	}
	public static void consoleln(String msg, String value) {
		Log.console(msg, value);
		System.out.print("\n");
	}
	public static void consoleln(boolean bool) {
		Log.console(bool);
		System.out.print("\n");
	}
	public static void consoleln(String msg, long value) {
		Log.console(msg, value);
		System.out.print("\n");
	}
	public static void consoleln(String msg) {
		Log.console(msg);
		System.out.print("\n");
	}
	public static void consoleln(double value) {
		Log.console(value);
		System.out.print("\n");
	}
	public static void consoleln(String msg, double value) {
		Log.console(msg, value);
		System.out.print("\n");
	}		
}
