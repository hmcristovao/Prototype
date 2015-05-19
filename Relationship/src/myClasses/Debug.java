package myClasses;

public class Debug {

	public static void out(String msg, String value) {
		System.out.println("\n===================================================================================");
		System.out.println(msg);
		System.out.println("<=>");
		System.out.println(value);
		System.out.println("-----------------------------------------------------------------------------------");
	}
	public static void out(String msgSingle) {
		System.out.println("\n===================================================================================");
		System.out.println(msgSingle);
		System.out.println("-----------------------------------------------------------------------------------");
	}
	public static void out(boolean bool) {
		if(bool)
			Debug.out("Boolean value: true");
		else
			Debug.out("Boolean value: false");
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
		
	
}
