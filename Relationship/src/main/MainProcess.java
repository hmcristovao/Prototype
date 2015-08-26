// v4.52 - fixed problem with sort average. Problem with "Category:" subword 

package main;

import graph.GephiGraphData;
import graph.NodeData;
import graph.NodesTableArray;
import graph.QuantityNodesEdges;
import graph.SystemGraphData;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.stream.gephi.JSONSender;

import rdf.SetQuerySparql;
import basic.*;
import user.*;

// this class manages the main process and it does system logs
public class MainProcess {	
	public static int iteration = 0;
	public static SetQuerySparql  currentSetQuerySparql;
	public static SystemGraphData currentSystemGraphData;
	
	public static void body(Wrapterms parser) throws Exception {
		try {
			start();
			parseTerms(parser);
			do {
				indicateIterationNumber();
				updateCurrentSetQuerySparqlVar();
				assemblyQueries();
				collectRDFs();
				createCurrentSystemGraphData();
				connectStreamVisualization();
				buildStreamGraphData_buildEdgeTable_fromRdfs();
				showQuantitiesStreamGraph();
				if(iteration >= 1) 
					copyAllObjectsLastIteration();
				if(iteration >= Config.iterationTriggerApplyNDegreeFilterAlgorithm 
				   && WholeSystem.getStreamGraphData().getTotalNodes() > Config.quantityNodesToApplyNdegreeFilter) 
 					applyNDegreeFilterTrigger();
				if(iteration >= Config.iterationTriggerApplyKCoreFilterAlgorithm) 
					applyKCoreFilterTrigger();
				buildGephiGraphData_NodesTableHash_NodesTableArray_fromStreamGraph();
				clearStreamGraphSink();
				
				classifyConnectedComponent();
                buildGexfGraphFile(Config.time.whileIteration);
				// conditionals set to continue the iteration
				if(breakIteration())
					break;
				calculateDistanceMeasuresWholeNetwork();
				storeDistanceMeasuresWholeNetworkToMainNodeTable();
				calculateEigenvectorMeasureWholeNetwork();
				storeEigenvectorMeasuresWholeNetworkToMainNodeTable();
				sortMeasuresWholeNetwork();
				buildSubGraphsRanks();
				buildSubGraphsTablesInConnectedComponents();
				sortConnectedComponentsRanks();
				selectLargestNodesByBetweennessCloseness();
				selectLargestNodesByEigenvector();
				reportSelectedNodesToNewIteration();
				if(Config.additionNewConceptWithoutCategory)
					duplicateConceptsWithoutCategory(iteration);				
				prepareDataToNewIteration();
				iteration++;
				
if(WholeSystem.getConceptsRegister().getConcept("Memory") == null)
	Log.consoleln("1>>>>>> Memory = null");
else
	Log.consoleln("1>>>>>> Memory = "+WholeSystem.getConceptsRegister().getConcept("Memory").toStringLong());

			} while(true);
			indicateAlgorithmIntermediateStage1(); // after iterations loop
			deleteCommonNodes_remainOriginalAndSelectedConcepts();
			iteration++;
			createCurrentSystemGraphData();
			buildGephiGraphData_NodesTableHash_NodesTableArray_fromStreamGraph();
			calculateDistanceMeasuresWholeNetwork();
			storeDistanceMeasuresWholeNetworkToMainNodeTable();
			calculateEigenvectorMeasureWholeNetwork();
			storeEigenvectorMeasuresWholeNetworkToMainNodeTable();
			classifyConnectedComponent();
 			buildGexfGraphFile(Config.time.afterIteration);
			indicateAlgorithmIntermediateStage2(); // selection main concepts

			
			if(WholeSystem.getConceptsRegister().getConcept("Memory") == null)
				Log.consoleln("2>>>>>> Memory = null");
			else
				Log.consoleln("2>>>>>> Memory = "+WholeSystem.getConceptsRegister().getConcept("Memory").toStringLong());

			
			// create a average sort list of nodes to get the least 
			createSortAverageOnlySelectedConcepts();
			int baseConnectedComponentCount = currentSystemGraphData.getConnectedComponentsCount();
			int nodeDataPos = 0;
			// loop while:
			// 1. quantity of selected concepts + original concepts > goal of total concepts, AND
			// 2. there are not concepts to try the remotion (because the quantity of componentes connected is improving)
			
			if(WholeSystem.getConceptsRegister().getConcept("Memory") == null)
				Log.consoleln("3>>>>>> Memory = null");
			else
				Log.consoleln("3>>>>>> Memory = "+WholeSystem.getConceptsRegister().getConcept("Memory").toStringLong());

			
			
			while(WholeSystem.getSortAverageSelectedConcepts().getCount() + WholeSystem.getQuantityOriginalConcepts() > WholeSystem.getGoalConceptsQuantity()
				  &&
				  nodeDataPos < WholeSystem.getSortAverageSelectedConcepts().getCount()) {
				
				// get the node that has the least average				
				NodeData nodeDataWithLeastAverage = WholeSystem.getSortAverageSelectedConcepts().getNodeData(nodeDataPos);
				Log.consoleln("Node data with least average: "+nodeDataWithLeastAverage+" (pos: "+nodeDataPos+")");
				// store the current informations of node that will be deleted (because it can be recovered)
				Node currentNode = nodeDataWithLeastAverage.getStreamNode();
				List<Edge> currentEdgeSet = new ArrayList<Edge>();
				for(Edge currentEdge : currentNode.getEdgeSet()) {
					currentEdgeSet.add(currentEdge);
				}
				// delete the node in the stream graph
				WholeSystem.getStreamGraphData().deleteNode(currentNode);
				classifyConnectedComponent(); 
				// if connected component quantity improved then recover the deleted node, edges (to stream graph)
				Log.consoleln("currentSystemGraphData.getConnectedComponentsCount() > baseConnectedComponentCount "+
				              currentSystemGraphData.getConnectedComponentsCount() +" > "+ baseConnectedComponentCount);
				if(currentSystemGraphData.getConnectedComponentsCount() > baseConnectedComponentCount) {
					WholeSystem.getStreamGraphData().insert(currentNode,currentEdgeSet);
					// try get the next node:
					nodeDataPos++;
				}
				// if ok then start at the first node again (the least average node)
				else {
					// cria novo systemGraphData
					iteration++;
					createCurrentSystemGraphData();
					buildGephiGraphData_NodesTableHash_NodesTableArray_fromStreamGraph();
					calculateDistanceMeasuresWholeNetwork();
					storeDistanceMeasuresWholeNetworkToMainNodeTable();
					calculateEigenvectorMeasureWholeNetwork();
					storeEigenvectorMeasuresWholeNetworkToMainNodeTable();
					nodeDataPos = 0;
					// create a average sort list of nodes to get the least 
					createSortAverageOnlySelectedConcepts();
				}
			} 
			// verify whether the goal was achieved: quantity of selected concepts + original concepts == goal of total concepts
			if(WholeSystem.getSortAverageSelectedConcepts().getCount() + WholeSystem.getQuantityOriginalConcepts() <= WholeSystem.getGoalConceptsQuantity())
				Log.consoleln("- Goal achieved!!! Total concepts: " + WholeSystem.getSortAverageSelectedConcepts().getCount() + WholeSystem.getQuantityOriginalConcepts());
			else
				Log.consoleln("- Goal did not achieve. Total concepts: " + WholeSystem.getSortAverageSelectedConcepts().getCount() + WholeSystem.getQuantityOriginalConcepts());
			    // meta não atingida
				// tentar retirar seguinda a ordem de betweenness, depois de closeness
				// mas, se mesmo assim não conseguir processa para atingir o maximo de conceitos possível, mesmo que aumente os connected component 
			
			// create the last GEXF file that represent the graph
            buildGexfGraphFile(Config.time.afterSelectionMainConcepts);
			indicateAlgorithmFinalStage(); // building concept map
     		//
			// falta aqui criar o particionamento, ou bem antes!!!
			//
			// create a GEXF file, like as concept map
            buildGexfGraphFile(Config.time.finalGraph);
            parseVocabulary(parser);
			buildRawConceptMapFromStreamGraph();
			upgradeConceptMap_heuristic_01_removeLinkNumber();
			upgradeConceptMap_heuristic_02_vocabularyTable();
			upgradeConceptMap_heuristic_03_categoryInTargetConcept();
			// create txt file of final concept map
			buildGexfGraphFileFromConceptMap();
			buildTxtFileFromConceptMap();
			
			end();
		}
		catch(FileNotFoundException e) {
			System.err.println("Error: file not found.");
			e.printStackTrace();
		}
		catch (IOException e) {
			System.err.println("Error: problem with the persistent file: " + e.getMessage());
			e.printStackTrace();
		}
		catch(TokenMgrError e) {
			System.err.println("Lexical error: " + e.getMessage());
			e.printStackTrace();
		}
		catch(SemanticException e) {
			System.err.println("Semantic error: " + e.getMessage());
			e.printStackTrace();
		}
		catch(ParseException e) {
			System.err.println("Sintax error: " + e.getMessage());
			e.printStackTrace();
		}
		// get the another errs
		catch(Exception e) {
			System.err.println("Other error: " + e.getMessage());
			e.printStackTrace();
		}
		// if error then close all log files
		Log.close();
	}
	
	public static void start() throws Exception {
		Log.initFiles();
		Log.consoleln("- Starting.");
	}
	public static void parseTerms(Wrapterms parser) throws Exception {
		Log.consoleln("- Parsing user terms.");
		parser = new Wrapterms(new FileInputStream(Config.nameUserTermsFile));
		WholeSystem.insertListSetQuerySparql(new SetQuerySparql());
		parser.parseUserTerms(WholeSystem.getListSetQuerySparql().getFirst());
		WholeSystem.initQuantityOriginalConcepts(WholeSystem.getConceptsRegister().size());
		WholeSystem.initGoalMaxConceptsQuantity();
		String sameReport = "Quantity of terms parsed: " + WholeSystem.getQuantityOriginalConcepts() + 
				            " (file: "+Config.nameUserTermsFile+")\n"; 
		Log.outFileCompleteReport(sameReport + WholeSystem.getConceptsRegister().getOriginalConcepts().toStringLong());
		Log.outFileShortReport(sameReport + WholeSystem.getConceptsRegister().getOriginalConcepts().toString());
	}
	public static void indicateIterationNumber() throws Exception {
		Log.consoleln("*** Iteration "+iteration+" ***");
		String sameReport = Config.starsLine+"Iteration "+iteration+Config.starsLine;
        Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void indicateAlgorithmIntermediateStage1() throws Exception {
		Log.consoleln("*** Intermediate stage 1 (work only with selected and original concepts) ***");
		String sameReport = Config.starsLine+"Intermediate stage 1 (work only with selected and original concepts)"+Config.starsLine;
        Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void indicateAlgorithmIntermediateStage2() throws Exception {
		Log.consoleln("*** Intermediate stage 2 (selection main concepts) ***" + 
				" - current concepts quantity: "+WholeSystem.getStreamGraphData().getTotalNodes()+" - "+
                "goal of concepts quantity: "+WholeSystem.getGoalConceptsQuantity()+
                " good, "+WholeSystem.getMaxConceptsQuantity()+" maximum.");
		String sameReport = Config.starsLine+"Intermediate stage 2 (selection main concepts)"+Config.starsLine+
				"\nCurrent concepts quantity: "+WholeSystem.getStreamGraphData().getTotalNodes()+"."+
                "\nGoal of concepts quantity: "+WholeSystem.getGoalConceptsQuantity()+
                " good, "+WholeSystem.getMaxConceptsQuantity()+" maximum.";
        Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void indicateAlgorithmFinalStage() throws Exception {
		Log.consoleln("*** Final stage (building concept map) ***");
		String sameReport = Config.starsLine+"Final stage (building concept map)"+Config.starsLine;
        Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void updateCurrentSetQuerySparqlVar() throws Exception {
		currentSetQuerySparql = WholeSystem.getListSetQuerySparql().get(iteration);
	}
	public static void assemblyQueries() throws Exception {
		Log.console("- Assembling queries");
		int num = currentSetQuerySparql.assemblyQueries();
		Log.consoleln(" - "+num+" new queries assembled.");
		String sameReport = "Queries assembled: " + num + "\n\n";
        Log.outFileCompleteReport(sameReport + currentSetQuerySparql.toString());
		Log.outFileShortReport(sameReport + currentSetQuerySparql.toStringShort());
	}
	public static void collectRDFs() throws Exception {
		Log.console("- Collecting RDFs");
		int num =  currentSetQuerySparql.collectRDFs();
		Log.consoleln(" - "+num+" new RDFs triples collected.");
		// extract collected quantity of RDFs to each concept
		StringBuffer conceptsOut = new StringBuffer();
		DecimalFormat formater =  new DecimalFormat("00000");
		for(int i=0; i < currentSetQuerySparql.getTotalConcepts(); i++) {
			conceptsOut.append("\n");
			conceptsOut.append(formater.format(currentSetQuerySparql.getListQuerySparql().get(i).getListRDF().size()));
			conceptsOut.append(" RDFs to concept \"");
			conceptsOut.append(currentSetQuerySparql.getListQuerySparql().get(i).getConcept().getBlankName());
			conceptsOut.append("\"");
		}
		String sameReport = "Total collected RDFs: " + num + "\n" + conceptsOut.toString();
        Log.outFileCompleteReport(sameReport + "\n\n" + currentSetQuerySparql.toString());
		Log.outFileShortReport(sameReport);
	}
	public static void createCurrentSystemGraphData() throws Exception {
		WholeSystem.insertListSystemGraphData(new SystemGraphData());
		currentSystemGraphData = WholeSystem.getListSystemGraphData().get(iteration);
	}
	public static void connectStreamVisualization() throws Exception {
		if(Config.graphStreamVisualization) {
			Log.consoleln("- Connecting Stream Visualization.");
			WholeSystem.getStreamGraphData().getStreamGraph().display(true);
		}
		if(Config.gephiVisualization) {
			Log.consoleln("- Connecting with Gephi.");
			JSONSender sender = new JSONSender("localhost", 8080, Config.nameGephiWorkspace);
			WholeSystem.getStreamGraphData().getStreamGraph().addSink(sender);
		}
	}
	// get RDFs and convert them to StreamGraph, but this fucntion is call: 
	//    in the firt iteration build StreamGraphData and EdgeTable
	//    in the second iteration so foth, just add new data into StreamGraphData and EdgeTable
	public static void buildStreamGraphData_buildEdgeTable_fromRdfs() throws Exception {
		Log.console("- Building Stream Graph Data");
		QuantityNodesEdges quantityNodesEdges = WholeSystem.getStreamGraphData().buildStreamGraphData_buildEdgeTable_fromRdfs(currentSetQuerySparql);
		Log.consoleln(" - "+quantityNodesEdges.getNumNodes()+" new nodes, "+quantityNodesEdges.getNumEdges()+" new edges in the visualization graph.");
		Log.consoleln("- Creating edge hash table - "+WholeSystem.getEdgesTable().size()+" edges.");
		String sameReport = "Stream Graph Data created (graph used in the preview): \n" + 
		        quantityNodesEdges.getNumNodes() + " new nodes, " + 
				quantityNodesEdges.getNumEdges() + " new edges in the visualization graph.\n" +
		        WholeSystem.getStreamGraphData().getRealTotalNodes() + " total nodes, " +
		        WholeSystem.getStreamGraphData().getRealTotalEdges() + " total edges.";
		String sameReport2 = "\n\nEdge hash table created:" + 
				"\n("+WholeSystem.getEdgesTable().size()+" edges).";
        Log.outFileCompleteReport(sameReport + WholeSystem.getStreamGraphData().toString() + sameReport2 + "\n"+WholeSystem.getEdgesTable().toString());
		Log.outFileShortReport(sameReport + WholeSystem.getStreamGraphData().toStringShort() + sameReport2);
	}
	public static void showQuantitiesStreamGraph() throws Exception {
		Log.consoleln("- Quantities Stream Graph built: "+WholeSystem.getStreamGraphData().getRealTotalNodes()+
				" nodes, "+WholeSystem.getStreamGraphData().getRealTotalEdges()+" edges.");
	}
	// if it is second iteration so forth, copy all objects (ListQuerySparql) of the last iteration
	public static void copyAllObjectsLastIteration() throws Exception {
		Log.console("- Second iteration or more: copying old elements of the last iteration");
		int n = currentSetQuerySparql.insertListQuerySparql(WholeSystem.getListSetQuerySparql().get(iteration-1).getListQuerySparql());
		Log.consoleln(" - "+n+" elements copied.");
		String sameReport = "Second iteration or more: "+n+" old elements copied from last iteration.\n";
        Log.outFileCompleteReport(sameReport + WholeSystem.getListSetQuerySparql().get(iteration-1).toString());
		Log.outFileShortReport(sameReport + WholeSystem.getListSetQuerySparql().get(iteration-1).toStringShort());
	}
	public static void applyNDegreeFilterTrigger() throws Exception {
		Log.console("- Starting "+Config.nDegreeFilter+"-degree filter algorithm "+
				"(iteration " + Config.iterationTriggerApplyNDegreeFilterAlgorithm + ", quantity of nodes greater than " + 
				Config.quantityNodesToApplyNdegreeFilter + ")");
			
		int numOldNodes = WholeSystem.getStreamGraphData().getRealTotalNodes();
		int numOldEdges = WholeSystem.getStreamGraphData().getRealTotalEdges();
		// call algorithm:
		int numDeletedOriginalConcepts = WholeSystem.getStreamGraphData().applyNdegreeFilterTrigger(Config.nDegreeFilter);
		int numCurrentNodes = WholeSystem.getStreamGraphData().getRealTotalNodes();
		int numCurrentEdges = WholeSystem.getStreamGraphData().getRealTotalEdges();
		Log.console(" - "+ (numOldNodes - numCurrentNodes) +" deleted nodes");
		Log.console(" ("+ numDeletedOriginalConcepts +" selected concepts)");
		Log.consoleln(" and "+ (numOldEdges - numCurrentEdges) +" deleted edges.");
		Log.consoleln("- Remained Stream Graph: "+numCurrentNodes+" nodes, "+numCurrentEdges+" edges.");
		String sameReport = "Runned "+Config.nDegreeFilter+"-degree filter algorithm "+
				"(triggered: iteration " + Config.iterationTriggerApplyNDegreeFilterAlgorithm + " or more, and quantity of nodes greater than " + 
				Config.quantityNodesToApplyNdegreeFilter + ")\n" +
				(numOldNodes - numCurrentNodes) +" deleted nodes" +
				"("+ numDeletedOriginalConcepts +" selected concepts)" + 
				" and "+ (numOldEdges - numCurrentEdges) +" deleted edges" +
				"\nOld Stream Graph: "+numOldNodes+" nodes, "+numOldEdges+" edges." +
				"\nRemained Stream Graph: "+numCurrentNodes+" nodes, "+numCurrentEdges+" edges.";
        Log.outFileCompleteReport(sameReport + "\n\n" + WholeSystem.getStreamGraphData().toString() );
		Log.outFileShortReport(sameReport);
	}			
	public static void applyKCoreFilterTrigger() throws Exception {
		Log.console("- Starting "+Config.kCoreFilter+"-core filter algorithm ");
		int numOldNodes = WholeSystem.getStreamGraphData().getRealTotalNodes();
		int numOldEdges = WholeSystem.getStreamGraphData().getRealTotalEdges();
		// call algorithm:
		int numDeletedOriginalConcepts = WholeSystem.getStreamGraphData().applyKCoreFilterTrigger(Config.kCoreFilter);
		int numCurrentNodes = WholeSystem.getStreamGraphData().getRealTotalNodes();
		int numCurrentEdges = WholeSystem.getStreamGraphData().getRealTotalEdges();
		Log.console(" - "+ (numOldNodes - numCurrentNodes) +" deleted nodes");
		Log.console(" ("+ numDeletedOriginalConcepts +" selected concepts)");
		Log.consoleln(" and "+ (numOldEdges - numCurrentEdges) +" deleted edges");
		Log.consoleln("- Remained Stream Graph: "+numCurrentNodes+" nodes, "+numCurrentEdges+" edges.");
		String sameReport = "Runned "+Config.kCoreFilter+"-core filter algorithm\n" +
				(numOldNodes - numCurrentNodes) +" deleted nodes" +
				"("+ numDeletedOriginalConcepts +" selected concepts)" + 
				" and "+ (numOldEdges - numCurrentEdges) +" deleted edges" +
				"\nOld Stream Graph: "+numOldNodes+" nodes, "+numOldEdges+" edges." +
				"\nRemained Stream Graph: "+numCurrentNodes+" nodes, "+numCurrentEdges+" edges.";
        Log.outFileCompleteReport(sameReport + "\n\n" + WholeSystem.getStreamGraphData().toString() );
		Log.outFileShortReport(sameReport);
	}
			
	public static void buildGephiGraphData_NodesTableHash_NodesTableArray_fromStreamGraph() throws Exception {
		Log.console("- Building Gephi Graph Data, Nodes Table Hash and Nodes Table Array from Stream Graph");
		// call function:
		QuantityNodesEdges quantityNodesEdges = currentSystemGraphData.buildGephiGraphData_NodesTableHash_NodesTableArray_fromStreamGraph();
		Log.consoleln(" - "+quantityNodesEdges.getNumNodes()+" nodes, "+quantityNodesEdges.getNumEdges()+" edges in the graph structure.");
		String sameReport = "Built Gephi Graph Data, Nodes Table Hash and Nodes Table Array from Stream Graph\n"+
				quantityNodesEdges.getNumNodes()+" nodes, "+quantityNodesEdges.getNumEdges()+" edges in the graph structure." +
				"\nReal quantities: "+currentSystemGraphData.getGephiGraphData().getRealQuantityNodesEdges().toString();
		Log.outFileCompleteReport(sameReport + "\n" + currentSystemGraphData.getGephiGraphData().toString());
		Log.outFileShortReport(sameReport);
	}
	public static void clearStreamGraphSink() throws Exception {
		if(Config.gephiVisualization)  
			WholeSystem.getStreamGraphData().getStreamGraph().clearSinks();
	}
	public static void calculateDistanceMeasuresWholeNetwork() throws Exception {
		Log.console("- Calculating distance measures of the whole network");
		currentSystemGraphData.getGephiGraphData().calculateGephiGraphDistanceMeasures();
		Log.consoleln(" - "+currentSystemGraphData.getGephiGraphData().getRealQuantityNodesEdges().toString() + ".");
		String sameReport = "Distance measures of the whole network calculated." + 
				"\n(betweenness and closeness to "+currentSystemGraphData.getGephiGraphData().getRealQuantityNodesEdges().toString()+")";
        Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void calculateEigenvectorMeasureWholeNetwork() throws Exception {
		Log.console("- Calculating eigenvector measure of the whole network");
		currentSystemGraphData.getGephiGraphData().calculateGephiGraphEigenvectorMeasure();
		Log.consoleln(" - "+currentSystemGraphData.getGephiGraphData().getRealQuantityNodesEdges().toString() + ".");
		String sameReport = "Eigenvector measure of the whole network calculated." + 
				"\n(eigenvector to "+currentSystemGraphData.getGephiGraphData().getRealQuantityNodesEdges().toString()+")";
        Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void storeDistanceMeasuresWholeNetworkToMainNodeTable() throws Exception {
		Log.consoleln("- Storing distance measures of the whole network to main node table.");
		currentSystemGraphData.storeDistanceMeasuresWholeNetwork();
		String sameReport = "Stored distance measures of the whole network to main node table.";
        Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	} 
	public static void storeEigenvectorMeasuresWholeNetworkToMainNodeTable() throws Exception {
		Log.consoleln("- Storing eigenvector measures of the whole network to main node table.");
		currentSystemGraphData.storeEigenvectorMeasuresWholeNetwork();
		String sameReport = "Stored eigenvector measures of the whole network to main node table.";
        Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	} 
	public static void sortMeasuresWholeNetwork() throws Exception {
		Log.consoleln("- Sorting measures of the whole network.");
		currentSystemGraphData.sortBetweennessWholeNetwork();
		currentSystemGraphData.sortClosenessWholeNetwork();
		currentSystemGraphData.sortEigenvectorWholeNetwork();
		String sameReport = "Sorted measures of the whole network.";
        Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void classifyConnectedComponent() throws Exception {
		Log.console("- Classifying connected component");
		int num = currentSystemGraphData.getGephiGraphData().classifyConnectedComponent();
		currentSystemGraphData.setConnectedComponentsCount(num);
		Log.consoleln(" - quantity of connected components: " + num + ".");
		String sameReport = "Connected component classified\n" + 
				num + " connected components.";
        Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void buildSubGraphsRanks() throws Exception {
		Log.consoleln("- Building sub-graphs ranks.");
		currentSystemGraphData.buildSubGraphRanks();
		String sameReport = "Sub-graphs ranks built.";
        Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void buildGexfGraphFile(Config.time time) throws Exception {
		String nameFileGexf = null;
		if(time == Config.time.whileIteration) 
			nameFileGexf = Config.nameFileGexfGraph + "_iteration" + (iteration<=9?"0"+iteration:iteration) + ".gexf";
		else if(time == Config.time.afterIteration)
	   		nameFileGexf = Config.nameFileGexfGraph + "_after_iterations.gexf";
		else if(time == Config.time.afterSelectionMainConcepts)
	   		nameFileGexf = Config.nameFileGexfGraph + "_after_selection_main_concepts.gexf";
		else if(time == Config.time.finalGraph)
			nameFileGexf = Config.nameFileGexfGraph + "_final.gexf";	
		Log.console("- Building GEXF Graph File");
		currentSystemGraphData.getGephiGraphData().buildGexfGraphFile(nameFileGexf);
		Log.consoleln(" (generated file: " + nameFileGexf + ").");
		String sameReport = "GEXF graph file generated: " + nameFileGexf;
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void buildSubGraphsTablesInConnectedComponents() throws Exception {
		Log.consoleln("- Building sub-graphs tables belong to connected components.");
		currentSystemGraphData.buildSubGraphsTablesInConnectedComponents();
		String sameReport = "Sub-graphs tables belong to connected components built.";
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void sortConnectedComponentsRanks() throws Exception {
		Log.consoleln("- Sorting connected components ranks.");
		currentSystemGraphData.sortConnectecComponentRanks();
		String sameReport = "Connected components ranks sorted.";
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void selectLargestNodesByBetweennessCloseness() throws Exception {
		Log.console("- Selecting largest nodes by betweenness+closeness");
		int num = currentSystemGraphData.selectLargestNodesBetweennessCloseness(iteration);
		Log.consoleln(" - "+num+" new selected concepts.");
		String sameReport = "Largest nodes by betweenness+closeness: " + num + " nodes.";
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void selectLargestNodesByEigenvector() throws Exception {
		Log.console("- Selecting largest nodes by eigenvector");
		int num = currentSystemGraphData.selectLargestNodesEigenvector(iteration);
		Log.consoleln(" - "+num+" new selected concepts.");
		String sameReport = "Largest nodes by eigenvector: " + num + " nodes."; 
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void reportSelectedNodesToNewIteration() throws Exception {
		Log.consoleln("- Reporting selected nodes to new iteration.");
		Log.outFileCompleteReport("Iteration "+iteration);
		Log.outFileShortReport("Iteration "+iteration);
		Log.outFileCompleteReport(currentSystemGraphData.toString());
		Log.outFileShortReport(currentSystemGraphData.toStringShort(Config.quantityNodesShortReport));
		String sameReport = currentSystemGraphData.reportSelectedNodes(iteration);			
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void duplicateConceptsWithoutCategory(int iteration) throws Exception {
		Log.console("- Duplicating concepts with \"Category:\" subword after to remove it");
		GroupConcept newConcepts = WholeSystem.getConceptsRegister().duplicateConceptsWithoutCategory(iteration);
		Log.consoleln(" - "+newConcepts.size()+" new concepts inserted.");
		String sameReport = "Duplicated "+newConcepts.size()+" concepts with \"Category:\" subword after to remove it:\n"
				            + newConcepts.toString(); 
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}	
	public static boolean breakIteration() throws Exception {
		boolean isBreak = false;
		// verify the iteration limit				
		if(iteration == Config.maxIteration-1) {
			isBreak = true;
			Log.consoleln("- Ending loop, maximum iteration quantity "+(Config.maxIteration)+" reached.");
			String sameReport = "Loop ended, maximum iteration quantity "+(Config.maxIteration)+" reached.";
			Log.outFileCompleteReport(sameReport);
			Log.outFileShortReport(sameReport);
		}
		// at least x iterations are necessary
		else if(iteration < Config.minIteration-1) {
			isBreak = false;
		}
		// checks if there is only one connected component 
		else if(currentSystemGraphData.getConnectedComponentsCount() == 1) {
			isBreak = true;
			Log.consoleln("- Ending loop, 1 connected component reached and at least "+(Config.minIteration)+" iterations.");
			String sameReport = "Loop ended, 1 connected component reached and at least "+(Config.minIteration)+" iterations.";
			Log.outFileCompleteReport(sameReport);
			Log.outFileShortReport(sameReport);
		}
		return isBreak;
	}
	public static void prepareDataToNewIteration() throws Exception {
		// preparation to a new iteration
		Log.console("- Preparing data to new iteration");
		// extract new selected concepts
		GroupConcept newGroupConcept = WholeSystem.getConceptsRegister().getSelectedConcepts(iteration);
		// put the new concepts into the new instance of SetQuerySparql and add it in WholeSystem 
		SetQuerySparql newSetQuerySparql = new SetQuerySparql();
		newSetQuerySparql.insertListConcept(newGroupConcept);
		WholeSystem.insertListSetQuerySparql(newSetQuerySparql);
		Log.consoleln(" - "+newGroupConcept.size()+" concepts inserted in the set of query Sparql.");
		String sameReport = "Data to new iteration prepared.\n" +
				newGroupConcept.size()+" concepts inserted in the set of query Sparql.\n";
        Log.outFileCompleteReport(sameReport + newGroupConcept.toStringLong());
		Log.outFileShortReport(sameReport + newGroupConcept.toString());
	}
	public static void deleteCommonNodes_remainOriginalAndSelectedConcepts() throws Exception {
		Log.console("- Deleting common nodes, remain only original and selected concepts");
		int numOldNodes = WholeSystem.getStreamGraphData().getRealTotalNodes();
		int numOldEdges = WholeSystem.getStreamGraphData().getRealTotalEdges();
		// call algorithm:
		WholeSystem.getStreamGraphData().deleteCommonNodes_remainOriginalAndSelectedConcepts();
		int numCurrentNodes = WholeSystem.getStreamGraphData().getRealTotalNodes();
		int numCurrentEdges = WholeSystem.getStreamGraphData().getRealTotalEdges();
		Log.console(" - "+ (numOldNodes - numCurrentNodes) +" deleted nodes ");
		Log.consoleln(" and "+ (numOldEdges - numCurrentEdges) +" deleted edges.");
		Log.consoleln("- Remained Stream Graph: "+numCurrentNodes+" nodes, "+numCurrentEdges+" edges.");
		String sameReport = "Deleted all common nodes (remain only original and selected concepts) \n" +
				(numOldNodes - numCurrentNodes) +" deleted nodes" +
				" and "+ (numOldEdges - numCurrentEdges) +" deleted edges" +
				"\nOld Stream Graph: "+numOldNodes+" nodes, "+numOldEdges+" edges." +
				"\nRemained Stream Graph: "+numCurrentNodes+" nodes, "+numCurrentEdges+" edges.";
		Log.outFileCompleteReport(sameReport + "\n\n" + WholeSystem.getStreamGraphData().toString() );
		Log.outFileShortReport(sameReport);
	}
	
	// get group of selected concepts and copy them to a new NodesTableArray
	// sort this table e store it in WholeSystem.sortAverageSelectedConcepts
	// (do not enter: original concepts, concepts that already were category or concepts with zero rdfs)
	public static void createSortAverageOnlySelectedConcepts() throws Exception {
		Log.console("- Creating sort table (average) of selected concepts");
		int n = currentSystemGraphData.createSortAverageOnlySelectedConcepts();
		Log.consoleln(" - "+WholeSystem.getSortAverageSelectedConcepts().getCount()+" nodes stored and sorted ("+
		              n + " concepts did not insert: that already were category or concepts with zero rdfs).");
		String sameReport = "Table of selected nodes created and sorted (average)\n"+WholeSystem.getSortAverageSelectedConcepts().toString()+
				      n + " concepts did not insert: that already were category or concepts with zero rdfs.";
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);	
		// zero remain concepts: stop right now
		if(WholeSystem.getSortAverageSelectedConcepts().getCount() == 0) {
			Log.consoleln("- Stoping. It's not possible to continue with zero selected concepts.");
			sameReport = "Algorithm stoped. It's not possible to continue with zero selected concepts.";
			Log.outFileCompleteReport(sameReport);
			Log.outFileShortReport(sameReport);	
			end();
			System.exit(0);
		}
	}
	public static void parseVocabulary(Wrapterms parser) throws Exception {
		Log.console("- Parsing vocabulary");
		parser = new Wrapterms(new FileInputStream(Config.nameVocabularyFile));
		parser.parseSystemVocabulary(WholeSystem.getVocabularyTable());
		Log.consoleln(" - " + WholeSystem.getVocabularyTable().size() + " sentences parsed.");
		String sameReport = "Quantity of vocabulary sentences parsed: " + WholeSystem.getVocabularyTable().size() +   
                " (file: "+Config.nameVocabularyFile+")\n" +
				"\nVocabulary table parsed:\n" + WholeSystem.getVocabularyTable().toString();
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void buildRawConceptMapFromStreamGraph()  throws Exception {
		Log.console("- Building raw propositions of the concept map");
		int n =currentSystemGraphData.buildRawConceptMapFromStreamGraph();
		Log.consoleln(" - "+WholeSystem.getConceptMap().size()+" proposition created (" + n + " repeated propositions - eliminated).");
		String sameReport = "Built "+WholeSystem.getConceptMap().size()+" raw propositions of the concept map (" + 
		                     n + " repeated propositions - eliminated):\n" + WholeSystem.getConceptMap().toString();
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);		
	}
	public static void upgradeConceptMap_heuristic_01_removeLinkNumber()  throws Exception {
		Log.console("- Upgrading the concept map with first heuristic (remove link number)");
		int n = WholeSystem.getConceptMap().upgradeConceptMap_heuristic_01_removeLinkNumber();
		Log.consoleln(" - " + n + " propositions changed.");
		String sameReport = "Heuristic 01: upgraded "+n+" concept map propositions with remove of link number:\n" + WholeSystem.getConceptMap().toString();
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);		
	}
	public static void upgradeConceptMap_heuristic_02_vocabularyTable()  throws Exception {
		Log.console("- Upgrading the concept map with second heuristic (change links with vocabulary table)");
		int n = WholeSystem.getConceptMap().upgradeConceptMap_heuristic_02_vocabularyTable();
		Log.consoleln(" - " + n + " links name changed.");
		String sameReport = "Heuristic 02: upgraded "+n+" concept map propositions with use of link vocabulary table:\n" + WholeSystem.getConceptMap().toString();
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);		
	}
	public static void upgradeConceptMap_heuristic_03_categoryInTargetConcept()  throws Exception {
		Log.console("- Upgrading the concept map with first heuristic");
		int n = WholeSystem.getConceptMap().upgradeConceptMap_heuristic_03_categoryInTargetConcept();
		Log.consoleln(" - " + n + " propositions changed.");
		String sameReport = "Heuristic 03: upgraded "+n+" concept map propositions with change of category in target concept:\n" + WholeSystem.getConceptMap().toString();
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);		
	}
	
	public static void buildGexfGraphFileFromConceptMap() throws Exception {
		String nameFileGexf = Config.nameFileGexfGraph + "_concept_map.gexf";
		Log.console("- Building GEXF Graph File from final concept map");
		WholeSystem.getConceptMap().buildGexfGraphFileFromConceptMap(nameFileGexf);
		Log.consoleln(" (generated file: " + nameFileGexf + ").");
		String sameReport = "GEXF graph file generated: " + nameFileGexf;
        Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void buildTxtFileFromConceptMap() throws Exception {
		Log.console("- Building TXT final Concept Map");
		WholeSystem.getConceptMap().buildTxtFileFromConceptMap(Config.nameFileTxtConceptMap);
		Log.consoleln(" (generated file: " + Config.nameFileTxtConceptMap + ").");
		String sameReport = "TXT concept map generated: " + Config.nameFileTxtConceptMap;
        Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	
	
	public static void end() throws Exception {
		Log.consoleln("- Closing.");
		if(Config.graphStreamVisualization) 
			WholeSystem.getStreamGraphData().getStreamGraph().clear();
		String sameReport = "Closed.\nOk!";
        Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
		Log.consoleln("- Ok!");
		Log.close();
	}
}

