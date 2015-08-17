package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintStream;

public class Log {
	public static BufferedWriter fileCompleteReport;
	public static BufferedWriter fileShortReport;
	public static BufferedWriter fileConsoleReport;
	public static PrintStream printStreamError;

	public static void initFiles() throws Exception {
		fileCompleteReport = new BufferedWriter(new FileWriter(Config.nameFileCompletReport));
		fileShortReport    = new BufferedWriter(new FileWriter(Config.nameFileShortReport));
		fileConsoleReport  = new BufferedWriter(new FileWriter(Config.nameFileConsoleReport));
		printStreamError   = new PrintStream(Config.nameFileConsoleError);
		System.setErr(printStreamError);
	}
	public static void close() throws Exception {
		if(fileCompleteReport != null) fileCompleteReport.close();
		if(fileShortReport != null)    fileShortReport.close();
		if(fileConsoleReport != null)  fileConsoleReport.close();
		if(printStreamError != null)   printStreamError.close();
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
	private static void print(String str) {
		System.out.print(str);
		try { fileConsoleReport.write(str); }
		catch(Exception e) {;}
	}
	private static void println(String str) {
		System.out.println(str);
		try { fileConsoleReport.write(str);
		      fileConsoleReport.write("\n"); }
		catch(Exception e) {;}		
	}
	public static void console(String msg, String value) {
		Log.print(Config.doubleLine);
		Log.println(msg);
		Log.println("<=>");
		Log.println(value);
		Log.print(Config.singleLine);
	}
	public static void console(boolean bool) {
		if(bool)
			Log.console("Boolean value: true");
		else
			Log.console("Boolean value: false");
	}
	public static void console(String msg, long value) {
		Log.print(Config.doubleLine);
		Log.println(msg);
		Log.println("<=>");
		Log.println(String.valueOf(value));
		Log.print(Config.singleLine);
}
	public static void console(String msg) {
		Log.print(msg);
	}
	public static void console(double value) {
		Log.print("\n" + value + "\n");
	}
	public static void console(String msg, double value) {
		Log.print("\n" + msg + ": " + value + "\n");
	}
	public static void consoleln(String msg, String value) {
		Log.console(msg, value);
		Log.print("\n");
	}
	public static void consoleln(boolean bool) {
		Log.console(bool);
		Log.print("\n");
	}
	public static void consoleln(String msg, long value) {
		Log.console(msg, value);
		Log.print("\n");
	}
	public static void consoleln(String msg) {
		Log.console(msg);
		Log.print("\n");
	}
	public static void consoleln(double value) {
		Log.console(value);
		System.out.print("\n");
	}
	public static void consoleln(String msg, double value) {
		Log.console(msg, value);
		Log.print("\n");
	}		
}
