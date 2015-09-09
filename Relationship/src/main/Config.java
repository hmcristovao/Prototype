package main;
public interface Config {

	// ============================================================================
	// CONFIG - RUN, LOG AND PRINT 
    //
	String  testNumber = "00"; 
	
	String nameFileCompletReport = "C:\\Users\\Henrique\\Documents\\log\\complete_report_"+Config.testNumber+".txt";
	String nameFileShortReport   = "C:\\Users\\Henrique\\Documents\\log\\short_report_"+Config.testNumber+".txt";
	String nameFileConsoleReport = "C:\\Users\\Henrique\\Documents\\log\\console_report_"+Config.testNumber+".txt";
	String nameFileConsoleError  = "C:\\Users\\Henrique\\Documents\\log\\consoleErr.txt";
	String doubleLine = "\n=============================================================================================\n";
	String singleLine = "\n---------------------------------------------------------------------------------------------\n";
	String starsLine  = "\n*********************************************************************************************\n";

	// ============================================================================
	// CONFIG - INPUT FILES 
    //
	String nameUserTermsFile       = "userterms\\terms"+Config.testNumber+".txt";
	String nameQueryDefaultFile    = "query_model\\query.txt";
	String nameVocabularyFile      = "vocabulary\\linkvocabulary.txt";
	String nameUselessConceptsFile = "vocabulary\\uselessconcepts.txt";
	
	
	// ============================================================================
	// CONFIG - OUTPUT FILES  (.GEXF and .TXT concept map) 
    //
	String nameGexfGraphFile      = "graph\\graph"+Config.testNumber;
	String nameTxtConceptMapFile  = "C:\\Users\\Henrique\\Documents\\conceptmap"+Config.testNumber+".txt";
	String nameClxConceptMapFile  = "C:\\Users\\Henrique\\Documents\\conceptmap"+Config.testNumber+".clx";
    	
	// ============================================================================
	// CONFIG - RDFs 
    //
	String selectedServiceSnorqlEndPoint = Config.serviceSnorqlEndpoint_1;
	
	String serviceSnorqlEndpoint_1 = "http://dbpedia.org/sparql";
	String serviceSnorqlEndpoint_2 = "http://lod.openlinksw.com/sparql/";
	String serviceSnorqlEndpoint_3 = "http://www.ida.liu.se/projects/semtech/energy/snorql/";
	String serviceSnorqlEndpoint_4 = "http://data.nobelprize.org/snorql/";

	String addressBasic = "http://relationship/";
	String addressImage = "http://commons.wikimedia.org/wiki/File:";
	
	String markQueryReplacement = "#######";
	
	String originalConceptAddress = "http://dbpedia.org/resource/";
	String namePersistenceRDFsTableFile = "persistenceRdfs\\persistenceRdfs.dat";
	String dirRdfsPersistenceFiles = "persistenceRdfs";
	
	// ============================================================================
	// CONFIG - GRAPHs 
    //
	String nameGraph = "Graph relationship";

	boolean gephiVisualization = 		false;
	String nameGephiWorkspace = 		"workspace0";

	boolean graphStreamVisualization = 	false;

	int maxNodes = 50000;
	int minEdges = 50000;

	boolean ignoreCaseConcept = false;
	
	boolean edgeLabelStreamGephi = true;
	boolean nodeLabelStreamGephi = false; // original concepts always have label in Stream Gephi
	
	boolean edgeLabelFileGephi   = true;
	boolean nodeLabelFileGephi   = false; // original concepts always have label in Stream Gephi

	boolean directedStreamGraph = false;
	boolean directedGephiGraph  = true;  // only accept true 
 
	
	// ============================================================================
	// CONFIG - ANALYSIS 
    //
	// range of the quantity of iterations
	int minIteration = 6;  // better set: 8
	int maxIteration = 50;  // it is necessary a great value to ensure that the connected component is 1  
	
	// proporcion above total original concept (used in the build betweenness+closeness sorted table) 
	double proporcionBetweenness = 10;

	// quantity of nodes to selection in all connected componentes (proporcion relative to quantity total of original nodes)
	double proporcionBetweennessCloseness = 2.0;
	// precision added up to rounding the calculate of quantity of each connected component
	double precisionBetweennessCloseness = 0.5;
	// maximum limit to quantity of new concepts distributed in all connected component, +0.5 in each component (excluding the addition of nodes by "Category:")
	double maxBetweennessCloseness = 15;

	// quantity of nodes to selection in all connected componentes (proporcion relative to quantity total of original nodes)
	double proporcionEigenvector = 2.0;
	// precision added up to rounding the calculate of quantity of each connected component
	double precisionEigenvector = 0.5; 
	// maximum limit to quantity of new concepts distributed in all connected component, +0.5 in each component (excluding the addition of nodes by "Category:")
	double maxEigenvector = 15; 

	// make the duplication of concept: from with "Category:" to without one
	boolean additionNewConceptWithoutCategory = true;
	
	// indicate concepts that do not belong to connected component (for example: original conpects)
	int withoutConnectedComponent = -1;
	
	// K-core used in all system
	int kCoreFilter = 2;
	// since iteration number x to apply K-core filter trigger
	int iterationTriggerApplyKCoreFilterAlgorithm = 5;  // better set: 5
	// quantity of nodes to shoot K-core n algorithm
	int quantityNodesToApplyKcoreFilter = 700;
	
	// N-degree filter used in all system
	int nDegreeFilter = 2;
	// since iteration number x to apply n-degree filter trigger
	int iterationTriggerApplyNDegreeFilterAlgorithm = 1;  // better set: 2
	// quantity of nodes to shoot n-degree filter algorithm
	int quantityNodesToApplyNdegreeFilter = 5000;  // better set: 20000
	
	// choice of nodes to be head (they are used to build the shortest paths)
	// obs.: the original concepts are always chosen
	boolean isBetweennessCloseness = true;
	boolean isEigenvector = false;
	boolean isSelected = false;
	
	// keep all nodes with link to original concepts (in stage after selection of head nodes)
	// normally this flag improves much more the final quantity of concepts in concept map 
	boolean isKeepNeighborsOfOriginalConcepts = false;
	
	// to fix a bug in Gephi Tool Kit - calculate wrong the quantity of connected component
	// turn the flag to true and put the especific original concept that is getting alone
	boolean isFixBugInGephiToolKit = true;
	String originalConceptWithGephiToolKitBug = "Tim Berners-Lee";
	
	// ============================================================================
	// CONFIG - REPORT 
    //
	// quantity of nodes to show in short report
	int quantityNodesShortReport = 1;

	// ============================================================================
	// CONFIG - CONCEPT MAP 
    //
	// to calculate min and max concepts to map
	int conceptsQuantityCalulationFactor = 18;  // (factor default: 18) - goal of concepts quantity: log2(1/original quantity)*2 + (factor)
	int conceptsMinMaxRange = 5;
	
	String backGroundcolorOriginalConcept = "200,200,200,255";
	String borderThicknessConceptWithHint = "2";
	
}
