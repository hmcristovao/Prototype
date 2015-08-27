package main;
public interface Config {

	// ============================================================================
	// CONFIG - RUN, LOG AND PRINT 
    //
	String  testNumber = "20"; 
	
	String nameFileCompletReport = "log\\complete_report_"+Config.testNumber+".txt";
	String nameFileShortReport   = "log\\short_report_"+Config.testNumber+".txt";
	String nameFileConsoleReport = "log\\console_report_"+Config.testNumber+".txt";
	String nameFileConsoleError  = "log\\consoleErr.txt";
	String doubleLine = "\n=============================================================================================\n";
	String singleLine = "\n---------------------------------------------------------------------------------------------\n";
	String starsLine  = "\n*********************************************************************************************\n";

	// ============================================================================
	// CONFIG - INPUT FILES 
    //
	String nameUserTermsFile    = "userterms\\terms"+Config.testNumber+".txt";
	String nameQueryDefaultFile = "query_model\\query.txt";
	String nameVocabularyFile   = "vocabulary\\linkvocabulary.txt";
	
	
	// ============================================================================
	// CONFIG - OUTPUT FILES  (.GEXF and .TXT) 
    //
	String nameFileGexfGraph      = "graph\\graph"+Config.testNumber;
	String nameFileTxtConceptMap  = "C:\\Users\\Henrique\\Documents\\conceptmap"+Config.testNumber+".txt";
	String nameFileCmapConceptMap = "conceptmap\\conceptmap"+Config.testNumber+".cmap";
    enum time {whileIteration, afterIteration, afterSelectionMainConcepts, finalGraph };

	
	// ============================================================================
	// CONFIG - RDFs 
    //
	String selectedServiceSnorqlEndPoint = Config.serviceSnorqlEndpoint_1;
	
	String serviceSnorqlEndpoint_1 = "http://dbpedia.org/sparql";
	String serviceSnorqlEndpoint_2 = "http://lod.openlinksw.com/sparql/";
	String serviceSnorqlEndpoint_3 = "http://www.ida.liu.se/projects/semtech/energy/snorql/";
	String serviceSnorqlEndpoint_4 = "http://data.nobelprize.org/snorql/";
	String serviceSnorqlEndpoint_5 = "http://dbpedia.org/snorql";

	String addressBasic = "http://relationship/";
	String addressImage = "http://commons.wikimedia.org/wiki/File:";
	
	String markQueryReplacement = "#######";
	
	String originalConceptAddress = "http://dbpedia.org/resource/";
	
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

	enum Status {commonConcept, originalConcept, selected, selectedBetweennessClosenessConcept, selectedEigenvectorConcept, noStatus};
	
	enum Category {no, yes, was};
	
	
	// ============================================================================
	// CONFIG - ANALYSIS 
    //
	// range of the quantity of iterations
	int minIteration = 4;   // better set: 8
	int maxIteration = 4;  // better set: 12
	
	// proporcion above total original concept (used in the build betweenness+closeness sorted table) 
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
	
	// indicate concepts that do not belong to connected component (for example: original conpects)
	int withoutConnectedComponent = -1;
	
	// K-core used in all system
	int kCoreFilter = 2;
	// since iteration number x to apply K-core filter trigger
	int iterationTriggerApplyKCoreFilterAlgorithm = 3;  // better set: 5
	// quantity of nodes to shoot K-core n algorithm
	// int quantityNodesToApplyKcoreFilter = 100;
	
	// N-degree filter used in all system
	int nDegreeFilter = 2;
	// since iteration number x to apply n-degree filter trigger
	int iterationTriggerApplyNDegreeFilterAlgorithm = 1;  // better set: 2
	// quantity of nodes to shoot n-degree filter algorithm
	int quantityNodesToApplyNdegreeFilter = 1000;  // better set: 5000
	
	// ============================================================================
	// CONFIG - REPORT 
    //
	// quantity of nodes to show in short report
	int quantityNodesShortReport = 1;

	// ============================================================================
	// CONFIG - CONCEPT MAP 
    //
	// to calculate min and max concepts to map
	int conceptsQuantityCalulationFactor = 18;  // goal of concepts quantity: log2(1/original quantity)*2 + (factor)
	int conceptsMinMaxRange = 5;
	
}
