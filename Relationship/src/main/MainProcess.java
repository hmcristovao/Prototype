// v3.4b - found the error about edges to same pair of nodes. not ready!

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
				if(iteration >= 1) 
					copyAllObjectsLastIteration();
				if(iteration >= Config.iterationTriggerApplyNDegreeFilterAlgorithm) 
					applyNDegreeFilterTrigger();
				buildGephiGraphData_NodesTableHash_NodesTableArray();
				clearStreamGraphSink();
				calculateDistanceMeasuresWholeNetwork();
				calculateEigenvectorMeasureWholeNetwork();
				sortMeasureWholeNetwork();
				classifyConnectedComponent_BuildSubGraphs();
                buildSubGraphsRanks();
				buildSubGraphsTablesInConnectedComponents();
				sortConnectedComponentsRanks();
				selectLargestNodesByBetweennessCloseness();
				selectLargestNodesByEigenvector();
				reportSelectedNodesToNewIteration();
                buildGephiGraphFile();
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
		Log.outFileCompleteReport("Quantity of terms parsed: " + 
		        WholeSystem.getConceptsRegister().size() +   
                " (file: "+Config.nameFileInput+")\n" +  
		        WholeSystem.getConceptsRegister().getOriginalConcepts().toStringLong());
		Log.outFileShortReport("Quantity of terms parsed: " + 
                WholeSystem.getConceptsRegister().size() + 
                " (file: "+Config.nameFileInput+")\n" +  
                WholeSystem.getConceptsRegister().getOriginalConcepts().toString());
	}
	public static void showIterationNumber() throws Exception {
		Log.consoleln("*** Iteration "+iteration+" ***");
		Log.outFileCompleteReport(Config.starsLine+"Iteration "+iteration+Config.starsLine);
		Log.outFileShortReport(Config.starsLine+"Iteration "+iteration+Config.starsLine);
	}
	public static void createCurrentSetQuerySparql() throws Exception {
		currentSetQuerySparql = wholeSystem.getListSetQuerySparql().get(iteration);
	}
	public static void assemblingQueries() throws Exception {
		Log.console("- Assembling queries");
		int num = currentSetQuerySparql.fillQuery();
		Log.consoleln(" - "+num+" new querys assembled.");
		Log.outFileCompleteReport("Queries assembled: " + num + "\n\n" + 
				currentSetQuerySparql.toString());
		Log.outFileShortReport("Queries assembled: " + num + "\n\n" + 
				currentSetQuerySparql.toStringShort());
	}
	public static void collectRDFs() throws Exception {
		Log.console("- Collecting RDFs");
		int num =  currentSetQuerySparql.fillRDFs();
		Log.consoleln(" - "+num+" new RDFs triples collected.");
		Log.outFileCompleteReport("RDFs collected: " + num + "\n\n" +
				currentSetQuerySparql.toString());
		Log.outFileShortReport("RDFs collected: " + num);
	}
	public static void createCurrentSystemGraphData() throws Exception {
		wholeSystem.insertListSystemGraphData(new SystemGraphData());
		currentSystemGraphData = wholeSystem.getListSystemGraphData().get(iteration);
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
	public static void buildStreamGraphData() throws Exception {
		Log.console("- Building Stream Graph Data");
		QuantityNodesEdges quantityNodesEdges = WholeSystem.getStreamGraphData().buildStreamGraphData(currentSetQuerySparql);
		Log.consoleln(" - "+quantityNodesEdges.getNumNodes()+" new nodes, "+quantityNodesEdges.getNumEdges()+" new edges in the visualization graph.");
		Log.outFileCompleteReport("Stream Graph Data created (graph used in the preview): \n" + 
		        quantityNodesEdges.getNumNodes() + " new nodes, " + 
				quantityNodesEdges.getNumEdges() + " new edges in the visualization graph.\n" +
		        WholeSystem.getStreamGraphData().getRealTotalNodes() + " total nodes, " +
		        WholeSystem.getStreamGraphData().getRealTotalEdges() + " total edges." + 
		        WholeSystem.getStreamGraphData().toString());				                  
		Log.outFileShortReport("Stream Graph Data created (graph used in the preview): \n" + 
                quantityNodesEdges.getNumNodes() + " new nodes, " + 
                quantityNodesEdges.getNumEdges() + " new edges in the visualization graph.\n" +
                WholeSystem.getStreamGraphData().getRealTotalNodes() + " total nodes, " +
                WholeSystem.getStreamGraphData().getRealTotalEdges() + " total edges." +
        		WholeSystem.getStreamGraphData().toStringShort());				                  
	}
	public static void showQuantitiesStreamGraph() throws Exception {
		Log.consoleln("- Quantities Stream Graph built: "+WholeSystem.getStreamGraphData().getRealTotalNodes()+
				" nodes, "+WholeSystem.getStreamGraphData().getRealTotalEdges()+" edges.");
	}
	// if it is second iteration so forth, copy all objects of the last iteration
	public static void copyAllObjectsLastIteration() throws Exception {
		Log.console("- Second iteration or more: copying old elements of the last iteration");
		int n = currentSetQuerySparql.insertListQuerySparql(wholeSystem.getListSetQuerySparql().get(iteration-1).getListQuerySparql());
		Log.consoleln(" - "+n+" elements copied.");
		Log.outFileCompleteReport("Second iteration or more: "+n+" old elements copied from last iteration.\n" +
				wholeSystem.getListSetQuerySparql().get(iteration-1).toString());
		Log.outFileShortReport("Second iteration or more: "+n+" old elements copied from last iteration.\n" +
				wholeSystem.getListSetQuerySparql().get(iteration-1).toStringShort());
	}
	public static void applyNDegreeFilterTrigger() throws Exception {
		// conditional to apply n-degree filter trigger
		if(WholeSystem.getStreamGraphData().getTotalNodes() > Config.quantityNodesToApplyNdegreeFilter) {
			Log.console("- Starting "+Config.kCoreN+"-degree filter algoritm "+
					"(iteration " + Config.iterationTriggerApplyNDegreeFilterAlgorithm + ", quantity of nodes greater than " + 
					Config.quantityNodesToApplyNdegreeFilter + ")");
			
			int numOldNodes = WholeSystem.getStreamGraphData().getRealTotalNodes();
			int numOldEdges = WholeSystem.getStreamGraphData().getRealTotalEdges();
			int numDeletedOriginalConcepts = WholeSystem.getStreamGraphData().applyNdegreeFilterTrigger();
			int numCurrentNodes = WholeSystem.getStreamGraphData().getRealTotalNodes();
			int numCurrentEdges = WholeSystem.getStreamGraphData().getRealTotalEdges();
			Log.console(" - "+ (numOldNodes - numCurrentNodes) +" deleted nodes");
			Log.console(" ("+ numDeletedOriginalConcepts +" selected concepts)");
			Log.consoleln(" and "+ (numOldEdges - numCurrentEdges) +" deleted edges");
			Log.consoleln("- Remained Stream Graph: "+numCurrentNodes+" nodes, "+numCurrentEdges+" edges.");
			Log.outFileCompleteReport("Runned "+Config.kCoreN+"-degree filter algorithm "+
					"(triggered: iteration " + Config.iterationTriggerApplyNDegreeFilterAlgorithm + " or more, and quantity of nodes greater than " + 
					Config.quantityNodesToApplyNdegreeFilter + ")\n" +
					(numOldNodes - numCurrentNodes) +" deleted nodes" +
					"("+ numDeletedOriginalConcepts +" selected concepts)" + 
					" and "+ (numOldEdges - numCurrentEdges) +" deleted edges" +
					"\nOld Stream Graph: "+numOldNodes+" nodes, "+numOldEdges+" edges." +
					"\nRemained Stream Graph: "+numCurrentNodes+" nodes, "+numCurrentEdges+" edges." +
					"\n\n" + WholeSystem.getStreamGraphData().toString() );
			Log.outFileShortReport("Triggered "+Config.kCoreN+"-degree filter algorithm "+
					"(triggered: iteration " + Config.iterationTriggerApplyNDegreeFilterAlgorithm + " or more, and quantity of nodes greater than " +					
					Config.quantityNodesToApplyNdegreeFilter + ")\n" +
					(numOldNodes - numCurrentNodes) +" deleted nodes" +
					"("+ numDeletedOriginalConcepts +" selected concepts)" + 
					" and "+ (numOldEdges - numCurrentEdges) +" deleted edges" +
					"\nOld Stream Graph: "+numOldNodes+" nodes, "+numOldEdges+" edges." +
					"\nRemained Stream Graph: "+numCurrentNodes+" nodes, "+numCurrentEdges+" edges.");
		}
	}			
	public static void buildGephiGraphData_NodesTableHash_NodesTableArray() throws Exception {
		Log.console("- Building Gephi Graph Data, Nodes Table Hash and Nodes Table Array from Stream Graph");
		QuantityNodesEdges quantityNodesEdges = currentSystemGraphData.buildGephiGraphData_NodesTableHash_NodesTableArray();
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
		Log.consoleln("- Calculating distance measures of the whole network.");
		currentSystemGraphData.getGephiGraphData().calculateGephiGraphDistanceMeasures();
		Log.outFileCompleteReport("Distance measures of the whole network calculated.");
		Log.outFileShortReport("Distance measures of the whole network calculated.");
	}
	public static void calculateEigenvectorMeasureWholeNetwork() throws Exception {
		Log.consoleln("- Calculating eigenvector measure of the whole network.");
		currentSystemGraphData.getGephiGraphData().calculateGephiGraphEigenvectorMeasure();
		Log.outFileCompleteReport("Eigenvector measure of the whole network calculated.");
		Log.outFileShortReport("Eigenvector measure of the whole network calculated.");
	}
	public static void sortMeasureWholeNetwork() throws Exception {
		Log.consoleln("- Sorting measures of the whole network.");
		currentSystemGraphData.sortMeasuresWholeNetwork();
		Log.outFileCompleteReport("Measures of the whole network sorted.");
		Log.outFileShortReport("Measures of the whole network sorted.");
	}
	public static void classifyConnectedComponent_BuildSubGraphs() throws Exception {
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
	public static void buildGephiGraphFile() throws Exception {
		String nameFileGexf = Config.nameGEXFGraph + "_iteration" + (iteration<=9?"0"+iteration:iteration) + ".gexf";
		Log.consoleln("- Building Gephi Graph File (generated file: " + nameFileGexf + ").");
		currentSystemGraphData.getGephiGraphData().buildGephiGraphFile(nameFileGexf);
		Log.outFileCompleteReport("Gephi graph file generated: " + nameFileGexf);
		Log.outFileShortReport("Gephi graph file generated: " + nameFileGexf);
	}
	public static void buildSubGraphsTablesInConnectedComponents() throws Exception {
		Log.consoleln("- Building sub-graphs tables belong to connected components.");
		currentSystemGraphData.buildBasicTableGroupOriginalConceptsSubGraph();
		Log.outFileCompleteReport("Sub-graphs tables belong to connected components built.");
		Log.outFileShortReport("Sub-graphs tables belong to connected components built.");
	}
	public static void sortConnectedComponentsRanks() throws Exception {
		Log.consoleln("- Sorting connected componets ranks.");
		currentSystemGraphData.sortConnectecComponentRanks();
		Log.outFileCompleteReport("Connected componets ranks sorted.");
		Log.outFileShortReport("Connected componets ranks sorted.");
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
		Log.consoleln("- Reporting selected nodes to new interation.");
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
		else if(currentSystemGraphData.getRanks().getCount() <= 1) {
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
		newSetQuerySparql = new SetQuerySparql();
		newSetQuerySparql.insertListConcept(newGroupConcept);
		wholeSystem.insertListSetQuerySparql(newSetQuerySparql);
		Log.consoleln(" - "+newGroupConcept.size()+" concepts inserted in the set of query Sparql");
		Log.outFileCompleteReport("Data to new iteration prepared.\n" +
					newGroupConcept.size()+" concepts inserted in the set of query Sparql.\n" +
					newGroupConcept.toStringLong());
		Log.outFileShortReport("Data to new iteration prepared.\n" +
					newGroupConcept.size()+" concepts inserted in the set of query Sparql.\n" +
					newGroupConcept.toString());
	}			
	public static void end() throws Exception {
		Log.consoleln("- Closing.");
		if(Config.graphStreamVisualization) 
			WholeSystem.getStreamGraphData().getStreamGraph().clear();
		Log.outFileCompleteReport("Closed.\nOk!");
		Log.outFileShortReport("Closed.\nOk!");
		Log.close();
		Log.consoleln("- Ok!");
	}
}

