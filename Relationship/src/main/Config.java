package main;
public interface Config {

	// ============================================================================
	// CONFIG - LOG AND PRINT 
    //
	boolean outPrintConsole = false;
	boolean errPrintConsole = true;
	boolean disableWarningLog4j = true;
	
	String nameFileConsoleOut  = "log\\consoleOut00.txt";
	String nameFileConsoleErr  = "log\\consoleErr.txt";
	String nameFileConsoleWarn = "log\\consoleWarn.txt";

	// ============================================================================
	// CONFIG - INPUT FILES 
    //
	String nameFileInput = "input\\terms00.txt";
	
	String nameFileQueryDefault = "query_model\\query.txt";

	// ============================================================================
	// CONFIG - RDFs 
    //
	String serviceEndpoint = 	"http://dbpedia.org/sparql";
	String serviceEndpoint2 = 	"http://lod.openlinksw.com/sparql/";

	String addressBasic = "http://relationship/";
	String addressImage = "http://http://commons.wikimedia.org/wiki/File:";
	
	// ============================================================================
	// CONFIG - GRAPHs 
    //
	String nameGraph = "Graph relationship";

	boolean graphStreamVisualization = 	false;
	boolean gephiVisualization = 		false;
	String nameGephiWorkspace = 		"workspace0";

	int totalNodes = 50000;
	int totalEdges = 50000;

	boolean edgeLabel = 		true;
	boolean nodeLabel = 		true; // original concepts always have label
	boolean ignoreCaseConcept = false;

	enum Level {commonConcept, originalConcept, selectedBetweennessClosenessConcept, selectedEigenvectorConcept };
		
	// ============================================================================
	// CONFIG - ANALYSIS 
    //
	// proporcion above total original concept
	double proporcionBetweenness = 4;

	// quantity of nodes to selection (about the quantity total of original nodes)
	double proporcionBetweennessCloseness = 0.5;
	// precision added up to rounding
	double precisionBetweennessCloseness = 1.0;
	// maximum limit to quantity of new concepts in each connected component (excluding the addition of nodes by "Category:")
	double maxBetweennessCloseness = 6;

	// quantity of nodes to selection (about the quantity total of original nodes)
	double proporcionEigenvector = 1.3;
	// precision added up to rounding
	double precisionEigenvector = 0.5; 
	// maximum limit to quantity of new concepts in each connected component (excluding the addition of nodes by "Category:")
	double maxEigenvector = 8; 

	// make the duplication of concept: from with "Category:" to without one
	boolean additionNewConceptWithoutCategory = true;
	
	// range of the quantity of iterations
	int minIteration = 3;
	int maxIteration = 4;
	

}
