package main;

public interface Config {
	boolean outPrintConsole = false;
	boolean errPrintConsole = true;
	boolean disableWarningLog4j = true;
	
	String nameFileConsoleOut  = "log\\consoleOut00.txt";
	String nameFileConsoleErr  = "log\\consoleErr.txt";
	String nameFileConsoleWarn = "log\\consoleWarn.txt";

	String nameFileInput = "input\\terms00.txt";
	
	String nameFileQueryDefault = "query_model\\query.txt";

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
	
	// CONFIG OF ANALISING
	// quantity of nodes to selection (about the quantity total of original nodes)
	double proporcionBetweennessCloseness = 0.5;
	// precision added up to rounding
	double precisionBetweennessCloseness = 1.0;
	// quantity of nodes to selection (about the quantity total of original nodes)
	double proporcionEigenvector = 1.3;
	// precision added up to rounding
	double precisionEigenvector = 0.5; 
	// consider the adition of new concepts without "Category:" prefix
	boolean additionNewConceptWithoutCategory = true;
	

	
}
