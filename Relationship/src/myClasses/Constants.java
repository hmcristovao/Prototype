package myClasses;

public interface Constants {
	String nameFileConsoleOut = "txt_files\\consoleOut.txt";
	String nameFileConsoleErr = "txt_files\\consoleErr.txt";

	String nameFileInput = "txt_files\\terms.txt";
	
	String nameFileQueryDefault = "txt_files\\query.txt";

	String serviceEndpoint2 = "http://dbpedia.org/sparql";
	String serviceEndpoint = "http://lod.openlinksw.com/sparql/";

	String nameGraph = "Graph relationship";

	String nameGephiWorkspace = "workspace0";

	int totalNodes = 20000;
	int totalEdges = 20000;

	boolean edgeLabel = true;
	boolean nodeLabel = false; // original concepts always have label

	enum Level {originalConcept, commonConcept};
}
