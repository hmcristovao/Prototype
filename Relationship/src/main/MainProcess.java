// v4.2 - finalised, but it is still not working.

package main;

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
				
				classifyConnectedComponent_buildSubGraphs();
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
				prepareDataToNewIteration();
				iteration++;
			} while(true);
			indicateAlgorithmIntermediateStage1(); // after iterations loop
			iteration++;
			deleteCommonNodes_remainOriginalAndSelectedConcepts();
			createCurrentSystemGraphData();
			buildGephiGraphData_NodesTableHash_NodesTableArray_fromStreamGraph();
			calculateDistanceMeasuresWholeNetwork();
			storeDistanceMeasuresWholeNetworkToMainNodeTable();
			calculateEigenvectorMeasureWholeNetwork();
			storeEigenvectorMeasuresWholeNetworkToMainNodeTable();
			createSortAverageOnlySelectedConcepts();
			classifyConnectedComponent_buildSubGraphs(); 
			buildGexfGraphFile(Config.time.afterIteration);
			indicateAlgorithmIntermediateStage2(); // selection main concepts
			int baseConnectedComponentCount = currentSystemGraphData.getConnectedComponentsCount();
			int nodeDataPos = 0;
			do {
				// pega o selected concept de menor average dentro do conjunto de nodes
				NodeData nodeDataWithLeastAverage = WholeSystem.getSortAverageSelectedConcepts().getNodeData(nodeDataPos);
				// store the current informations of node that will be deleted (because it can be recovered)
				Node currentNode = nodeDataWithLeastAverage.getStreamNode();
				List<Edge> currentEdgeSet = new ArrayList<Edge>();
				for(Edge currentEdge : currentNode.getEdgeSet()) {
					currentEdgeSet.add(currentEdge);
				}
				// deleta o node do stream graph
				WholeSystem.getStreamGraphData().deleteNode(currentNode);
				// cria novo systemGraphData
				iteration++;
				createCurrentSystemGraphData();
				buildGephiGraphData_NodesTableHash_NodesTableArray_fromStreamGraph();
				calculateDistanceMeasuresWholeNetwork();
				storeDistanceMeasuresWholeNetworkToMainNodeTable();
				calculateEigenvectorMeasureWholeNetwork();
				storeEigenvectorMeasuresWholeNetworkToMainNodeTable();
				classifyConnectedComponent_buildSubGraphs(); 
				// verifica se connected component aumentou
				if(currentSystemGraphData.getConnectedComponentsCount() > baseConnectedComponentCount) {
					// se sim, então recoloca o node e os edges no grafo novamente
					WholeSystem.getStreamGraphData().insert(currentNode,currentEdgeSet);
					// e recupera a iteração anterior
					iteration--;
					nodeDataPos++;
				}
				// se conseguiu excluir, volta a pegar o primeiro novamente
				else
					nodeDataPos = 0;
				
				createSortAverageOnlySelectedConcepts();
				
				// verifica se a qtde de selected concepts == goal
				if(WholeSystem.getSortAverageSelectedConcepts().getCount() <= WholeSystem.getGoalConceptsQuantity())
					break;				
			} while(nodeDataPos < WholeSystem.getSortAverageSelectedConcepts().getCount());
			if(WholeSystem.getSortAverageSelectedConcepts().getCount() <= WholeSystem.getGoalConceptsQuantity())
				// ok, meta atingida;
				Log.consoleln("- Meta atingida!");
			else
				Log.consoleln("- Meta nao atingida!");
			    // meta não atingida
				// tentar retirar seguinda a ordem de betweenness, depois de closeness
				// mas, se mesmo assim não conseguir processa para atingir o maximo de conceitos possível, mesmo que aumente os connected component 
			
            buildGexfGraphFile(Config.time.lastGraph);
			
            // a partir daqui ele irá construir o mapa conceitual (na verdade, proposições em formato de texto)
            indicateAlgorithmFinalStage(); // building concept map
			createCurrentSystemGraphData();
			
			
			buildGephiGraphData_NodesTableHash_NodesTableArray_fromStreamGraph();
			calculateDistanceMeasuresWholeNetwork();
			storeDistanceMeasuresWholeNetworkToMainNodeTable();
			calculateEigenvectorMeasureWholeNetwork();
			storeEigenvectorMeasuresWholeNetworkToMainNodeTable();
			classifyConnectedComponent_buildSubGraphs(); 

			parseVocabulary(parser);
			buildConceptMap();

            buildGexfGraphFile(Config.time.lastGraph);
            showConceptMapPropositions();
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
		Log.outFileCompleteReport("Quantity of terms parsed: " + 
		        WholeSystem.getQuantityOriginalConcepts() +   
                " (file: "+Config.nameUserTermsFile+")\n" +  
		        WholeSystem.getConceptsRegister().getOriginalConcepts().toStringLong());
		Log.outFileShortReport("Quantity of terms parsed: " + 
                WholeSystem.getConceptsRegister().size() + 
                " (file: "+Config.nameUserTermsFile+")\n" +  
                WholeSystem.getConceptsRegister().getOriginalConcepts().toString());
	}
	public static void indicateIterationNumber() throws Exception {
		Log.consoleln("*** Iteration "+iteration+" ***");
		Log.outFileCompleteReport(Config.starsLine+"Iteration "+iteration+Config.starsLine);
		Log.outFileShortReport(Config.starsLine+"Iteration "+iteration+Config.starsLine);
	}
	public static void indicateAlgorithmIntermediateStage1() throws Exception {
		Log.consoleln("*** Intermediate stage 1 (work only with selected and original concepts) ***");
		Log.outFileCompleteReport(Config.starsLine+"Intermediate stage 1 (work only with selected and original concepts)"+Config.starsLine);
		Log.outFileShortReport(Config.starsLine+"Intermediate stage 1 (work only with selected and original concepts)"+Config.starsLine);
	}
	public static void indicateAlgorithmIntermediateStage2() throws Exception {
		Log.consoleln("*** Intermediate stage 2 (selection main concepts) ***");
		Log.outFileCompleteReport(Config.starsLine+"Intermediate stage 2 (selection main concepts)"+Config.starsLine);
		Log.outFileShortReport(Config.starsLine+"Intermediate stage 2 (selection main concepts)"+Config.starsLine);
	}
	public static void indicateAlgorithmFinalStage() throws Exception {
		Log.consoleln("*** Final stage (building concept map) ***");
		Log.outFileCompleteReport(Config.starsLine+"Final stage (building concept map)"+Config.starsLine);
		Log.outFileShortReport(Config.starsLine+"Final stage (building concept map)"+Config.starsLine);
	}
	public static void updateCurrentSetQuerySparqlVar() throws Exception {
		currentSetQuerySparql = WholeSystem.getListSetQuerySparql().get(iteration);
	}
	public static void assemblyQueries() throws Exception {
		Log.console("- Assembling queries");
		int num = currentSetQuerySparql.assemblyQueries();
		Log.consoleln(" - "+num+" new queries assembled.");
		Log.outFileCompleteReport("Queries assembled: " + num + "\n\n" + 
				currentSetQuerySparql.toString());
		Log.outFileShortReport("Queries assembled: " + num + "\n\n" + 
				currentSetQuerySparql.toStringShort());
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
		Log.outFileCompleteReport("Total collected RDFs: " + num + "\n" + conceptsOut.toString() + "\n\n" +
				currentSetQuerySparql.toString());
		Log.outFileShortReport("Total collected RDFs: " + num + "\n" + conceptsOut.toString());
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
		Log.outFileCompleteReport("Stream Graph Data created (graph used in the preview): \n" + 
		        quantityNodesEdges.getNumNodes() + " new nodes, " + 
				quantityNodesEdges.getNumEdges() + " new edges in the visualization graph.\n" +
		        WholeSystem.getStreamGraphData().getRealTotalNodes() + " total nodes, " +
		        WholeSystem.getStreamGraphData().getRealTotalEdges() + " total edges." + 
		        WholeSystem.getStreamGraphData().toString());
		Log.outFileCompleteReport("Edge hash table created:" + 
				"\n("+WholeSystem.getEdgesTable().size()+" edges)." +
				"\n"+WholeSystem.getEdgesTable().toString());
		Log.outFileShortReport("Stream Graph Data created (graph used in the preview): \n" + 
                quantityNodesEdges.getNumNodes() + " new nodes, " + 
                quantityNodesEdges.getNumEdges() + " new edges in the visualization graph.\n" +
                WholeSystem.getStreamGraphData().getRealTotalNodes() + " total nodes, " +
                WholeSystem.getStreamGraphData().getRealTotalEdges() + " total edges." +
        		WholeSystem.getStreamGraphData().toStringShort());				                  
		Log.outFileShortReport("Edge hash table created:" + 
				"\n("+WholeSystem.getEdgesTable().size()+" edges).");
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
		Log.outFileCompleteReport("Second iteration or more: "+n+" old elements copied from last iteration.\n" +
				WholeSystem.getListSetQuerySparql().get(iteration-1).toString());
		Log.outFileShortReport("Second iteration or more: "+n+" old elements copied from last iteration.\n" +
				WholeSystem.getListSetQuerySparql().get(iteration-1).toStringShort());
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
		Log.consoleln(" and "+ (numOldEdges - numCurrentEdges) +" deleted edges");
		Log.consoleln("- Remained Stream Graph: "+numCurrentNodes+" nodes, "+numCurrentEdges+" edges.");
		Log.outFileCompleteReport("Runned "+Config.nDegreeFilter+"-degree filter algorithm "+
				"(triggered: iteration " + Config.iterationTriggerApplyNDegreeFilterAlgorithm + " or more, and quantity of nodes greater than " + 
				Config.quantityNodesToApplyNdegreeFilter + ")\n" +
				(numOldNodes - numCurrentNodes) +" deleted nodes" +
				"("+ numDeletedOriginalConcepts +" selected concepts)" + 
				" and "+ (numOldEdges - numCurrentEdges) +" deleted edges" +
				"\nOld Stream Graph: "+numOldNodes+" nodes, "+numOldEdges+" edges." +
				"\nRemained Stream Graph: "+numCurrentNodes+" nodes, "+numCurrentEdges+" edges." +
				"\n\n" + WholeSystem.getStreamGraphData().toString() );
		Log.outFileShortReport("Triggered "+Config.nDegreeFilter+"-degree filter algorithm "+
				"(triggered: iteration " + Config.iterationTriggerApplyNDegreeFilterAlgorithm + " or more, and quantity of nodes greater than " +					
				Config.quantityNodesToApplyNdegreeFilter + ")\n" +
				(numOldNodes - numCurrentNodes) +" deleted nodes" +
				"("+ numDeletedOriginalConcepts +" selected concepts)" + 
				" and "+ (numOldEdges - numCurrentEdges) +" deleted edges" +
				"\nOld Stream Graph: "+numOldNodes+" nodes, "+numOldEdges+" edges." +
				"\nRemained Stream Graph: "+numCurrentNodes+" nodes, "+numCurrentEdges+" edges.");
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
		Log.outFileCompleteReport("Runned "+Config.kCoreFilter+"-core filter algorithm\n" +
				(numOldNodes - numCurrentNodes) +" deleted nodes" +
				"("+ numDeletedOriginalConcepts +" selected concepts)" + 
				" and "+ (numOldEdges - numCurrentEdges) +" deleted edges" +
				"\nOld Stream Graph: "+numOldNodes+" nodes, "+numOldEdges+" edges." +
				"\nRemained Stream Graph: "+numCurrentNodes+" nodes, "+numCurrentEdges+" edges." +
				"\n\n" + WholeSystem.getStreamGraphData().toString() );
		Log.outFileShortReport("Triggered "+Config.kCoreFilter+"-core filter algorithm\n" +
				(numOldNodes - numCurrentNodes) +" deleted nodes" +
				"("+ numDeletedOriginalConcepts +" selected concepts)" + 
				" and "+ (numOldEdges - numCurrentEdges) +" deleted edges" +
				"\nOld Stream Graph: "+numOldNodes+" nodes, "+numOldEdges+" edges." +
				"\nRemained Stream Graph: "+numCurrentNodes+" nodes, "+numCurrentEdges+" edges.");
	}
			
	public static void buildGephiGraphData_NodesTableHash_NodesTableArray_fromStreamGraph() throws Exception {
		Log.console("- Building Gephi Graph Data, Nodes Table Hash and Nodes Table Array from Stream Graph");
		// call function:
		QuantityNodesEdges quantityNodesEdges = currentSystemGraphData.buildGephiGraphData_NodesTableHash_NodesTableArray_fromStreamGraph();
		Log.consoleln(" - "+quantityNodesEdges.getNumNodes()+" nodes, "+quantityNodesEdges.getNumEdges()+" edges in the graph structure.");
		Log.outFileCompleteReport("Built Gephi Graph Data, Nodes Table Hash and Nodes Table Array from Stram Graph\n"+
				quantityNodesEdges.getNumNodes()+" nodes, "+quantityNodesEdges.getNumEdges()+" edges in the graph structure." +
				"\nReal quantities: "+currentSystemGraphData.getGephiGraphData().getRealQuantityNodesEdges().toString() + 
				"\n" + currentSystemGraphData.getGephiGraphData().toString());
		Log.outFileShortReport("Built Gephi Graph Data, Nodes Table Hash and Nodes Table Array from Stram Graph\n"+
				quantityNodesEdges.getNumNodes()+" nodes, "+quantityNodesEdges.getNumEdges()+" edges in the graph structure." +
				"\nReal quantities: "+currentSystemGraphData.getGephiGraphData().getRealQuantityNodesEdges().toString() );
	}
	public static void clearStreamGraphSink() throws Exception {
		if(Config.gephiVisualization)  
			WholeSystem.getStreamGraphData().getStreamGraph().clearSinks();
	}
	public static void calculateDistanceMeasuresWholeNetwork() throws Exception {
		Log.console("- Calculating distance measures of the whole network");
		currentSystemGraphData.getGephiGraphData().calculateGephiGraphDistanceMeasures();
		Log.consoleln(" - "+currentSystemGraphData.getGephiGraphData().getRealQuantityNodesEdges().toString());
		Log.outFileCompleteReport("Distance measures of the whole network calculated." + 
				"\n(betweenness and closeness to "+currentSystemGraphData.getGephiGraphData().getRealQuantityNodesEdges().toString()+")");
		Log.outFileShortReport("Distance measures of the whole network calculated." + 
				"\n(betweenness and closeness to "+currentSystemGraphData.getGephiGraphData().getRealQuantityNodesEdges().toString()+")");
	}
	public static void calculateEigenvectorMeasureWholeNetwork() throws Exception {
		Log.console("- Calculating eigenvector measure of the whole network");
		currentSystemGraphData.getGephiGraphData().calculateGephiGraphEigenvectorMeasure();
		Log.consoleln(" - "+currentSystemGraphData.getGephiGraphData().getRealQuantityNodesEdges().toString());
		Log.outFileCompleteReport("Eigenvector measure of the whole network calculated." + 
				"\n(eigenvector to "+currentSystemGraphData.getGephiGraphData().getRealQuantityNodesEdges().toString()+")");
		Log.outFileShortReport("Eigenvector measure of the whole network calculated." + 
				"\n(eigenvector to "+currentSystemGraphData.getGephiGraphData().getRealQuantityNodesEdges().toString()+")");
	}
	public static void storeDistanceMeasuresWholeNetworkToMainNodeTable() throws Exception {
		Log.consoleln("- Storing distance measures of the whole network to main node table.");
		currentSystemGraphData.storeDistanceMeasuresWholeNetwork();
		Log.outFileCompleteReport("Stored distance measures of the whole network to main node table.");
		Log.outFileShortReport("Stored distance measures of the whole network to main node table.");
	} 
	public static void storeEigenvectorMeasuresWholeNetworkToMainNodeTable() throws Exception {
		Log.consoleln("- Storing eigenvector measures of the whole network to main node table.");
		currentSystemGraphData.storeEigenvectorMeasuresWholeNetwork();
		Log.outFileCompleteReport("Stored eigenvector measures of the whole network to main node table.");
		Log.outFileShortReport("Stored eigenvector measures of the whole network to main node table.");
	} 
	public static void sortMeasuresWholeNetwork() throws Exception {
		Log.consoleln("- Sorting measures of the whole network.");
		currentSystemGraphData.sortBetweennessWholeNetwork();
		currentSystemGraphData.sortClosenessWholeNetwork();
		currentSystemGraphData.sortEigenvectorWholeNetwork();
		Log.outFileCompleteReport("Sorted measures of the whole network.");
		Log.outFileShortReport("Sorted measures of the whole network.");
	}
	public static void classifyConnectedComponent_buildSubGraphs() throws Exception {
		Log.console("- Classifying connected component and building sub graphs");
		int num = currentSystemGraphData.getGephiGraphData().classifyConnectedComponent();
		currentSystemGraphData.setConnectedComponentsCount(num);
		Log.consoleln(" - quantity of connected components: " + num + ".");
		Log.outFileCompleteReport("Connected component and sub graphs created\n" + 
				num + " connected components.");
		Log.outFileShortReport("Connected component and sub graphs created\n" + 
				num + " connected components.");
	}
	public static void buildSubGraphsRanks() throws Exception {
		Log.consoleln("- Building sub-graphs ranks.");
		currentSystemGraphData.buildSubGraphRanks();
		Log.outFileCompleteReport("Sub-graphs ranks built.");
		Log.outFileShortReport("Sub-graphs ranks built.");
	}
	public static void buildGexfGraphFile(Config.time time) throws Exception {
		String nameFileGexf = null;
		if(time == Config.time.whileIteration) 
			nameFileGexf = Config.nameGEXFGraph + "_iteration" + (iteration<=9?"0"+iteration:iteration) + ".gexf";
		else if(time == Config.time.afterIteration)
	   		nameFileGexf = Config.nameGEXFGraph + "_after_iterations.gexf";
		else if(time == Config.time.lastGraph)
			nameFileGexf = Config.nameGEXFGraph + "_final.gexf";	
		else if(time == Config.time.conceptMap)
			nameFileGexf = Config.nameGEXFGraph + "_concept_map.gexf";	
		Log.consoleln("- Building GEXF Graph File (generated file: " + nameFileGexf + ").");
		currentSystemGraphData.getGephiGraphData().buildGephiGraphFile(nameFileGexf);
		Log.outFileCompleteReport("GEXF graph file generated: " + nameFileGexf);
		Log.outFileShortReport("GEXF graph file generated: " + nameFileGexf);
	}
	public static void buildSubGraphsTablesInConnectedComponents() throws Exception {
		Log.consoleln("- Building sub-graphs tables belong to connected components.");
		currentSystemGraphData.buildSubGraphsTablesInConnectedComponents();
		Log.outFileCompleteReport("Sub-graphs tables belong to connected components built.");
		Log.outFileShortReport("Sub-graphs tables belong to connected components built.");
	}
	public static void sortConnectedComponentsRanks() throws Exception {
		Log.consoleln("- Sorting connected components ranks.");
		currentSystemGraphData.sortConnectecComponentRanks();
		Log.outFileCompleteReport("Connected components ranks sorted.");
		Log.outFileShortReport("Connected components ranks sorted.");
	}
	public static void selectLargestNodesByBetweennessCloseness() throws Exception {
		Log.console("- Selecting largest nodes by betweenness+closeness");
		int num = currentSystemGraphData.selectLargestNodesBetweennessCloseness(iteration);
		Log.consoleln(" - "+num+" new selected concepts.");
		Log.outFileCompleteReport("Largest nodes by betweenness+closeness: " + num + " nodes.");
		Log.outFileShortReport("Largest nodes by betweenness+closeness: " + num + " nodes.");
	}
	public static void selectLargestNodesByEigenvector() throws Exception {
		Log.console("- Selecting largest nodes by eigenvector");
		int num = currentSystemGraphData.selectLargestNodesEigenvector(iteration);
		Log.consoleln(" - "+num+" new selected concepts.");
		Log.outFileCompleteReport("Largest nodes by eigenvector: " + num + " nodes.");
		Log.outFileShortReport("Largest nodes by eigenvector: " + num + " nodes.");
	}
	public static void reportSelectedNodesToNewIteration() throws Exception {
		Log.consoleln("- Reporting selected nodes to new iteration.");
		Log.outFileCompleteReport("Iteration "+iteration);
		Log.outFileShortReport("Iteration "+iteration);
		Log.outFileCompleteReport(currentSystemGraphData.toString());
		Log.outFileShortReport(currentSystemGraphData.toStringShort(Config.quantityNodesShortReport));
		String report = currentSystemGraphData.reportSelectedNodes(iteration);			
		Log.outFileCompleteReport(report);
		Log.outFileShortReport(report);
	}
	public static boolean breakIteration() throws Exception {
		boolean isBreak = false;
		// verify the iteration limit				
		if(iteration == Config.maxIteration-1) {
			isBreak = true;
			Log.consoleln("- Ending loop, maximum iteration quantity "+(Config.maxIteration)+" reached.");
			Log.outFileCompleteReport("Loop ended, maximum iteration quantity "+(Config.maxIteration)+" reached.");
			Log.outFileShortReport("Loop ended, maximum iteration quantity "+(Config.maxIteration)+" reached.");
		}
		// at least x iterations are necessary
		else if(iteration < Config.minIteration-1) {
			isBreak = false;
		}
		// checks if there is only one connected component 
		else if(currentSystemGraphData.getConnectedComponentsCount() == 1) {
			isBreak = true;
			Log.consoleln("- Ending loop, 1 connected component reached and at least "+(Config.minIteration)+" iterations.");
			Log.outFileCompleteReport("Loop ended, 1 connected component reached and at least "+(Config.minIteration)+" iterations.");
			Log.outFileShortReport("Loop ended, 1 connected component reached and at least "+(Config.minIteration)+" iterations.");
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
		Log.consoleln(" - "+newGroupConcept.size()+" concepts inserted in the set of query Sparql");
		Log.outFileCompleteReport("Data to new iteration prepared.\n" +
					newGroupConcept.size()+" concepts inserted in the set of query Sparql.\n" +
					newGroupConcept.toStringLong());
		Log.outFileShortReport("Data to new iteration prepared.\n" +
					newGroupConcept.size()+" concepts inserted in the set of query Sparql.\n" +
					newGroupConcept.toString());
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
		Log.outFileCompleteReport("Deleted all common nodes (remain only original and selected concepts) \n" +
				(numOldNodes - numCurrentNodes) +" deleted nodes" +
				" and "+ (numOldEdges - numCurrentEdges) +" deleted edges" +
				"\nOld Stream Graph: "+numOldNodes+" nodes, "+numOldEdges+" edges." +
				"\nRemained Stream Graph: "+numCurrentNodes+" nodes, "+numCurrentEdges+" edges." +
				"\n\n" + WholeSystem.getStreamGraphData().toString() );
		Log.outFileShortReport("Deleted all common nodes (remain only original and selected concepts) \n" +
				(numOldNodes - numCurrentNodes) +" deleted nodes" +
				" and "+ (numOldEdges - numCurrentEdges) +" deleted edges" +
				"\nOld Stream Graph: "+numOldNodes+" nodes, "+numOldEdges+" edges." +
				"\nRemained Stream Graph: "+numCurrentNodes+" nodes, "+numCurrentEdges+" edges.");
	}
	
	// original concepts do not enter
	public static NodesTableArray createSortAverageOnlySelectedConcepts() throws Exception {
		GroupConcept selectedConcepts = WholeSystem.getConceptsRegister().getSelectedConcepts();
		NodesTableArray nodesTableArray = new NodesTableArray(selectedConcepts.size());
		int quantity = 0;
		for(int i=0; i<selectedConcepts.size(); i++) {
			Concept concept = selectedConcepts.getConcept(i);
			if(concept.getCategory() != Config.Category.was && concept.getQuantityRdfs() != 0) {	
				NodeData nodeData = currentSystemGraphData.getNodeData(concept.getBlankName());
				nodesTableArray.insert(nodeData);
				quantity++;
			}
		}
		nodesTableArray.sortCrescentAverage(quantity);
		WholeSystem.setSortAverageSelectedConcepts(nodesTableArray);
		return nodesTableArray;
	}
	public static void parseVocabulary(Wrapterms parser) throws Exception {
		Log.consoleln("- Parsing vocabulary");
		parser = new Wrapterms(new FileInputStream(Config.nameVocabularyFile));
		parser.parseSystemVocabulary(WholeSystem.getVocabularyTable());
		Log.consoleln(" - " + WholeSystem.getVocabularyTable().size() + " sentences parsed.");
		String sameReport = "Quantity of vocabulary sententes parsed: " + WholeSystem.getVocabularyTable().size() +   
                " (file: "+Config.nameVocabularyFile+")\n" +
				"\nVocabulary table parsed:\n" + WholeSystem.getVocabularyTable().toString();
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void buildConceptMap()  throws Exception {
		Log.console("- building concept map propositions");
		currentSystemGraphData.buildConceptMap();
		Log.consoleln(" - "+WholeSystem.getConceptMap().size()+" proposition created.");
		Log.outFileCompleteReport(WholeSystem.getConceptMap().size() + " propositions built to concept map");
		Log.outFileShortReport(WholeSystem.getConceptMap().size() + " propositions built to concept map");		
	}
	public static void showConceptMapPropositions() throws Exception {
		Log.consoleln("- Showing concept map propositions.");
		Log.outFileCompleteReport("Concept map propositions:\n" + WholeSystem.getConceptMap().toString());
		Log.outFileShortReport("Concept map propositions:\n" + WholeSystem.getConceptMap().toString());
	}
	public static void end() throws Exception {
		Log.consoleln("- Closing.");
		if(Config.graphStreamVisualization) 
			WholeSystem.getStreamGraphData().getStreamGraph().clear();
		Log.outFileCompleteReport("Closed.\nOk!");
		Log.outFileShortReport("Closed.\nOk!");
		Log.consoleln("- Ok!");
		Log.close();
	}
}

