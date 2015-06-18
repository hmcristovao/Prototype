package main;
public interface Config {

	// ============================================================================
	// CONFIG - LOG AND PRINT 
    //
	String  testNumber = "14"; 
	
	String nameFileCompletReport = "log\\complete_report_"+Config.testNumber+".txt";
	String nameFileShortReport   = "log\\short_report_"+Config.testNumber+".txt";
	String nameFileConsoleError  = "log\\consoleErr.txt";
	String doubleLine = "\n=============================================================================================\n";
	String singleLine = "\n---------------------------------------------------------------------------------------------\n";

	// ============================================================================
	// CONFIG - INPUT FILES 
    //
	String nameFileInput = "input\\terms"+Config.testNumber+".txt";
	String nameFileQueryDefault = "query_model\\query.txt";

	// ============================================================================
	// CONFIG - RDFs 
    //
	String serviceEndpoint  = "http://dbpedia.org/sparql";
	String serviceEndpoint2 = "http://lod.openlinksw.com/sparql/";

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
	
	String nameGEXFGraph = "graph\\graph"+Config.testNumber+".gext";
	
	// ============================================================================
	// CONFIG - ANALYSIS 
    //
	// range of the quantity of iterations
	int minIteration = 2;
	int maxIteration = 2;
	
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
	
	// ============================================================================
	// CONFIG - REPORT 
    //
	// quantity of nodes to show in short report
	int quantityNodesShortReport = 5;

	// ============================================================================
	// CONFIG - CONCEPT MAP 
    //
	String nameConceptMap = "conceptmap\\conceptmap"+Config.testNumber+".gext";

}
