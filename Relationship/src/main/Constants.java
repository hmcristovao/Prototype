package main;

public interface Constants {
	boolean outPrintConsole = false;
	boolean errPrintConsole = true;
	boolean disableWarningLog4j = true;
	
	String nameFileConsoleOut  = "txt_files\\consoleOut.txt";
	String nameFileConsoleErr  = "txt_files\\consoleErr.txt";
	String nameFileConsoleWarn = "txt_files\\consoleWarn.txt";

	String nameFileInput = "txt_files\\terms.txt";
	
	String nameFileQueryDefault = "txt_files\\query.txt";

	String serviceEndpoint = 	"http://dbpedia.org/sparql";
	String serviceEndpoint2 = 	"http://lod.openlinksw.com/sparql/";

	String addressBasic = "http://relationship/";
	String addressImage = "http://http://commons.wikimedia.org/wiki/File:";
	
	String nameGraph = "Graph relationship";

	boolean graphStreamVisualization = 	false;
	boolean gephiVisualization = 		false;
	String nameGephiWorkspace = 		"workspace0";

	int totalNodes = 20000;
	int totalEdges = 20000;

	boolean edgeLabel = 		true;
	boolean nodeLabel = 		true; // original concepts always have label
	boolean ignoreCaseConcept = false;

	enum Level {commonConcept, originalConcept, selectedBetweennessClosenessConcept, selectedEigenvectorConcept };
	
	// proporcion above total original concept
	double proporcionBetweenness = 4;
	
}
