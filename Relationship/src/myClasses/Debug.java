package myClasses;

public class Debug {

	public static void DEBUG(String msg, String value) {
		System.out.println("\n===================================================================================");
		System.out.println(msg);
		System.out.println("<=>");
		System.out.println(value);
		System.out.println("-----------------------------------------------------------------------------------");
	}
	public static void DEBUG(String msgSingle) {
		System.out.println("\n===================================================================================");
		System.out.println(msgSingle);
		System.out.println("-----------------------------------------------------------------------------------");
	}
	public static void DEBUG(boolean bool) {
		if(bool)
			Debug.DEBUG("Boolean value: true");
		else
			Debug.DEBUG("Boolean value: false");
	}
	public static void DEBUG(String msg, long value) {
		System.out.println("\n===================================================================================");
		System.out.println(msg);
		System.out.println("<=>");
		System.out.println(value);
		System.out.println("-----------------------------------------------------------------------------------");
	}

}
