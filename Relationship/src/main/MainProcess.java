// v3.3 - new organization in MainProcess. Working, but edges do not been deleted in N-degree filter

package main;

import graph.QuantityNodesEdges;
import graph.SystemGraphData;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.graphstream.stream.gephi.JSONSender;

import rdf.SetQuerySparql;
import basic.*;
import user.*;

public class MainProcess {	
	public static WholeSystem wholeSystem = new WholeSystem();
	public static int iteration = 0;
	public static SetQuerySparql  currentSetQuerySparql;
	public static SystemGraphData currentSystemGraphData;
	public static SetQuerySparql  newSetQuerySparql;
		
	public static void body(Wrapterms parser) throws Exception {
		try {
			start();
			parseTerms(parser);
			do {
				showIterationNumber();
				createCurrentSetQuerySparql();
				assemblingQueries();
				collectRDFs();
				createCurrentSystemGraphData();
				connectStreamVisualization();
				buildStreamGraphData();
				showQuantitiesStreamGraph();
				copyAllObjectsLastIteration();
				applyNDegreeFilterTrigger();
				buildGephiGraphData_NodesTableHash_NodesTableArray();
				clearStreamGraphSink();
				calculateDistanceMeasuresWholeNetwork();
				calculateEigenvectorMeasureWholeNetwork();
				sortMeasureWholeNetwork();
				classifyConnectedComponent_BuildSubGraphs();
                buildSubGraphsRanks();
                buildGephiGraphFile();
				buildSubGraphsTablesInConnectedComponents();
				sortConnectedComponentsRanks();
				selectLargestNodesByBetweennessCloseness();
				selectLargestNodesByEigenvector();
				reportSelectedNodesToNewIteration();
				// conditionals set to continue the iteration
				if(breakIteration())
					break;
				prepareDataToNewIteration();
				iteration++;
			} while(true);			
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
		Log.consoleln("- Starting.");
		Log.init();
	}
	public static void parseTerms(Wrapterms parser) throws Exception {
		Log.consoleln("- Parsing terms.");
		parser = new Wrapterms(new FileInputStream(Config.nameFileInput));
		wholeSystem.insertListSetQuerySparql(new SetQuerySparql());
		parser.start(wholeSystem.getListSetQuerySparql().getFirst());
		WholeSystem.initGoalMaxConceptsQuantity();
		Log.outFileCompleteReport("");
		Log.outFileShortReport("");
	}
	public static void showIterationNumber() throws Exception {
		Log.consoleln("*** Iteration "+iteration+" ***");
		Log.outFileCompleteReport("");
		Log.outFileShortReport("");
	}
	public static void createCurrentSetQuerySparql() throws Exception {
		currentSetQuerySparql = wholeSystem.getListSetQuerySparql().get(iteration);
		Log.outFileCompleteReport("");
		Log.outFileShortReport("");
	}
	public static void assemblingQueries() throws Exception {
		Log.console("- Assembling queries");
		int num = currentSetQuerySparql.fillQuery();
		Log.consoleln(" - "+num+" new querys assembled.");
		Log.outFileCompleteReport("");
		Log.outFileShortReport("");
	}
	public static void collectRDFs() throws Exception {
		Log.console("- Collecting RDFs");
		int num =  currentSetQuerySparql.fillRDFs();
		Log.consoleln(" - "+num+" new RDFs triples collected.");
		Log.outFileCompleteReport("");
		Log.outFileShortReport("");
	}
	public static void createCurrentSystemGraphData() throws Exception {
		wholeSystem.insertListSystemGraphData(new SystemGraphData());
		currentSystemGraphData = wholeSystem.getListSystemGraphData().get(iteration);
		Log.outFileCompleteReport("");
		Log.outFileShortReport("");
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
		Log.outFileCompleteReport("");
		Log.outFileShortReport("");
	}
	public static void buildStreamGraphData() throws Exception {
		Log.console("- Building Stream Graph Data");
		QuantityNodesEdges quantityNodesEdges = WholeSystem.getStreamGraphData().buildStreamGraphData(currentSetQuerySparql);
		Log.consoleln(" - "+quantityNodesEdges.getNumNodes()+" new nodes, "+quantityNodesEdges.getNumEdges()+" new edges in the visualization graph.");
		Log.outFileCompleteReport("");
		Log.outFileShortReport("");
	}
	public static void showQuantitiesStreamGraph() throws Exception {
		Log.consoleln("- Quantities Stream Graph built: "+WholeSystem.getStreamGraphData().getRealTotalNodes()+" nodes, "+WholeSystem.getStreamGraphData().getRealTotalEdges()+" edges.");
		Log.outFileCompleteReport("");
		Log.outFileShortReport("");
	}
	public static void copyAllObjectsLastIteration() throws Exception {
		// if it is second iteration so forth, copy all objects of the last iteration
		if(iteration >= 1)
			currentSetQuerySparql.insertListQuerySparql(wholeSystem.getListSetQuerySparql().get(iteration-1).getListQuerySparql());
		Log.outFileCompleteReport("");
		Log.outFileShortReport("");
	}
	public static void applyNDegreeFilterTrigger() throws Exception {
		// conditional to apply n-degree filter trigger
		if(WholeSystem.getStreamGraphData().getTotalNodes() > Config.quantityNodesToApplyNdegreeFilter) {
			Log.console("- Starting "+Config.kCoreN+"-degree filter algoritm (quantity of nodes greater than " + Config.quantityNodesToApplyNdegreeFilter + ")");
			int numOldNodes = WholeSystem.getStreamGraphData().getRealTotalNodes();
			int numOldEdges = WholeSystem.getStreamGraphData().getRealTotalEdges();
			int numDeletedOriginalConcepts = WholeSystem.getStreamGraphData().applyNdegreeFilterTrigger();
			int numCurrentNodes = WholeSystem.getStreamGraphData().getRealTotalNodes();
			int numCurrentEdges = WholeSystem.getStreamGraphData().getRealTotalEdges();
			Log.console(" - "+ (numOldNodes - numCurrentNodes) +" deleted nodes");
			Log.console(" ("+ numDeletedOriginalConcepts +" selected concepts)");
			Log.consoleln(" and "+ (numOldEdges - numCurrentEdges) +" deleted edges");
			Log.consoleln("- Remained Stream Graph: "+numCurrentNodes+" nodes, "+numCurrentEdges+" edges.");
		}
		Log.outFileCompleteReport("");
		Log.outFileShortReport("");
	}			
	public static void buildGephiGraphData_NodesTableHash_NodesTableArray() throws Exception {
		Log.console("- Building Gephi Graph Data, Nodes Table Hash and Nodes Table Array");
		QuantityNodesEdges quantityNodesEdges = currentSystemGraphData.buildGephiGraphData_NodesTableHash_NodesTableArray();
		Log.consoleln(" - "+quantityNodesEdges.getNumNodes()+" nodes, "+quantityNodesEdges.getNumEdges()+" edges in the graph structure.");
		Log.outFileCompleteReport("");
		Log.outFileShortReport("");
	}
	public static void clearStreamGraphSink() throws Exception {
		if(Config.gephiVisualization)  
			WholeSystem.getStreamGraphData().getStreamGraph().clearSinks();
		Log.outFileCompleteReport("");
		Log.outFileShortReport("");
	}
	public static void calculateDistanceMeasuresWholeNetwork() throws Exception {
		Log.consoleln("- Calculating distance measures of the whole network.");
		currentSystemGraphData.getGephiGraphData().calculateGephiGraphDistanceMeasures();
		Log.outFileCompleteReport("");
		Log.outFileShortReport("");
	}
	public static void calculateEigenvectorMeasureWholeNetwork() throws Exception {
		Log.consoleln("- Calculating eigenvector measure of the whole network.");
		currentSystemGraphData.getGephiGraphData().calculateGephiGraphEigenvectorMeasure();
		Log.outFileCompleteReport("");
		Log.outFileShortReport("");
	}
	public static void sortMeasureWholeNetwork() throws Exception {
		Log.consoleln("- Sorting measures of the whole network.");
		currentSystemGraphData.sortMeasuresWholeNetwork();
		Log.outFileCompleteReport("");
		Log.outFileShortReport("");
	}
	public static void classifyConnectedComponent_BuildSubGraphs() throws Exception {
		Log.console("- Classifying connected component and building sub graphs");
		int num = currentSystemGraphData.getGephiGraphData().classifyConnectedComponent();
		currentSystemGraphData.setConnectedComponentsCount(num);
		Log.consoleln(" - quantity of connected conponents: " + num + ".");
		Log.outFileCompleteReport("");
		Log.outFileShortReport("");
	}
	public static void buildSubGraphsRanks() throws Exception {
		Log.consoleln("- Building sub-graphs ranks.");
		currentSystemGraphData.buildSubGraphRanks();
		Log.outFileCompleteReport("");
		Log.outFileShortReport("");
	}
	public static void buildGephiGraphFile() throws Exception {
		String nameFileGexf = Config.nameGEXFGraph + "_iteration" + (iteration<=9?"0"+iteration:iteration) + ".gexf";
		Log.consoleln("- Building Gephi Graph File (generated file: " + nameFileGexf + ").");
		currentSystemGraphData.getGephiGraphData().buildGephiGraphFile(nameFileGexf);
		Log.outFileCompleteReport("");
		Log.outFileShortReport("");
	}
	public static void buildSubGraphsTablesInConnectedComponents() throws Exception {
		Log.consoleln("- Building sub-graphs tables belong to connected components.");
		currentSystemGraphData.buildBasicTableGroupOriginalConceptsSubGraph();
		Log.outFileCompleteReport("");
		Log.outFileShortReport("");
	}
	public static void sortConnectedComponentsRanks() throws Exception {
		Log.consoleln("- Sorting connected componets ranks.");
		currentSystemGraphData.sortConnectecComponentRanks();
		Log.outFileCompleteReport("");
		Log.outFileShortReport("");
	}
	public static void selectLargestNodesByBetweennessCloseness() throws Exception {
		Log.console("- Selecting largest nodes by betweenness+closeness");
		int num = currentSystemGraphData.selectLargestNodesBetweennessCloseness(iteration);
		Log.consoleln(" - "+num+" new selected concepts.");
		Log.outFileCompleteReport("");
		Log.outFileShortReport("");
	}
	public static void selectLargestNodesByEigenvector() throws Exception {
		Log.console("- Selecting largest nodes by eigenvector");
		int num = currentSystemGraphData.selectLargestNodesEigenvector(iteration);
		Log.consoleln(" - "+num+" new selected concepts.");
		Log.outFileCompleteReport("");
		Log.outFileShortReport("");
	}
	public static void reportSelectedNodesToNewIteration() throws Exception {
		Log.consoleln("- Reporting selected nodes to new interation.");
		Log.outFileCompleteReport("Iteration "+iteration);
		Log.outFileShortReport("Iteration "+iteration);
		Log.outFileCompleteReport(currentSetQuerySparql.toString());
		Log.outFileShortReport(currentSetQuerySparql.toStringShort());
		Log.outFileCompleteReport(WholeSystem.getStreamGraphData().toString());
		Log.outFileShortReport(WholeSystem.getStreamGraphData().toStringShort());
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
		}
		// at least x iterations are necessary
		else if(iteration < Config.minIteration-1) {
			isBreak = false;
		}
		// checks if there is only one connected component 
		else if(currentSystemGraphData.getRanks().getCount() <= 1)
			isBreak = true;
		Log.outFileCompleteReport("");
		Log.outFileShortReport("");
		return isBreak;
	}
	public static void prepareDataToNewIteration() throws Exception {
		// preparation to a new iteration
		Log.consoleln("- Preparing data to new iteration.");
		// extract new selected concepts
		GroupConcept newGroupConcept = WholeSystem.getConceptsRegister().getSelectedConcepts(iteration);
		// put the new concepts into the new instance of SetQuerySparql and add it in WholeSystem 
		newSetQuerySparql = new SetQuerySparql();
		newSetQuerySparql.insertListConcept(newGroupConcept);
		wholeSystem.insertListSetQuerySparql(newSetQuerySparql);
		Log.outFileCompleteReport("");
		Log.outFileShortReport("");
	}			
	public static void end() throws Exception {
		Log.consoleln("- Closing.");
		if(Config.graphStreamVisualization) 
			WholeSystem.getStreamGraphData().getStreamGraph().clear();
		Log.close();
		Log.consoleln("- Ok!");
		Log.outFileCompleteReport("Closing.\nOk!");
		Log.outFileShortReport("Closing.\nOk!");
	}
}

