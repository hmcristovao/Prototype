package myClasses;

public interface Constants {
	String nameFileConsoleOut = "txt_files\\consoleOut.txt";
	String nameFileConsoleErr = "txt_files\\consoleErr.txt";

	String nameFileInput = "txt_files\\terms4.txt";
	
	String nameFileQueryDefault = "txt_files\\query.txt";

	String serviceEndpoint = "http://dbpedia.org/sparql";
	String serviceEndpoint2 = "http://lod.openlinksw.com/sparql/";

	String addressBasic = "http://relationship/";
	String addressImage = "http://http://commons.wikimedia.org/wiki/File:";
	
	String nameGraph = "Graph relationship";

	String nameGephiWorkspace = "workspace0";

	int totalNodes = 20000;
	int totalEdges = 20000;

	boolean edgeLabel = true;
	boolean nodeLabel = false; // original concepts always have label
	boolean ignoreCaseConcept = false;

	enum Level {originalConcept, commonConcept};
}
