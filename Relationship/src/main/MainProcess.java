// v3.2 - added n-degree filter algorithm. Working!

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
	public static String report;
	public static boolean isContinue = false;
	public static int iteration = 0;
	public static SetQuerySparql  currentSetQuerySparql;
	public static SystemGraphData currentSystemGraphData;
	public static SetQuerySparql  newSetQuerySparql;
		
	public static void body(Wrapterms parser) throws Exception {
		try {
			Log.consoleln("- Starting.");
			Log.init();	
			Log.consoleln("- Parsing terms.");
			parser = new Wrapterms(new FileInputStream(Config.nameFileInput));
			wholeSystem.insertListSetQuerySparql(new SetQuerySparql());
			parser.start(wholeSystem.getListSetQuerySparql().getFirst());
			int num;
			QuantityNodesEdges quantityNodesEdges = null;
			do {
				currentSetQuerySparql = wholeSystem.getListSetQuerySparql().get(iteration);
				Log.consoleln("*** Iteration "+iteration+" ***");
				
				Log.console("- Assembling queries");
				num = currentSetQuerySparql.fillQuery();
				Log.consoleln(" - "+num+" new querys assembled.");
				
				Log.console("- Collecting RDFs");
				num = currentSetQuerySparql.fillRDFs();
				Log.consoleln(" - "+num+" new RDFs triples collected.");
								
				wholeSystem.insertListSystemGraphData(new SystemGraphData());
				currentSystemGraphData = wholeSystem.getListSystemGraphData().get(iteration);
				if(Config.graphStreamVisualization) {
					Log.consoleln("- Connecting Stream Visualization.");
					WholeSystem.getStreamGraphData().getStreamGraph().display(true);
				}
				if(Config.gephiVisualization) {
					Log.consoleln("- Connecting with Gephi.");
					JSONSender sender = new JSONSender("localhost", 8080, Config.nameGephiWorkspace);
					WholeSystem.getStreamGraphData().getStreamGraph().addSink(sender);
				}

				Log.console("- Building Stream Graph Data");
				quantityNodesEdges = WholeSystem.getStreamGraphData().buildStreamGraphData(currentSetQuerySparql);
				Log.consoleln(" - "+quantityNodesEdges.getNumNodes()+" new nodes, "+quantityNodesEdges.getNumEdges()+" new edges in the visualization graph.");
				
				Log.consoleln("- Total Stream Graph built: "+WholeSystem.getStreamGraphData().getRealTotalNodes()+" nodes, "+WholeSystem.getStreamGraphData().getRealTotalEdges()+" edges.");
				
				// if it is second iteration so forth, copy all objects of the last iteration
				if(iteration >= 1)
					currentSetQuerySparql.insertListQuerySparql(wholeSystem.getListSetQuerySparql().get(iteration-1).getListQuerySparql());
				
				/*
				// conditional to apply K-core n
				if(WholeSystem.getStreamGraphData().getTotalNodes() > Config.quantityNodesToApplyKcoreN) {
					Log.console("- Starting K-core "+Config.kCoreN+" algoritm (quantity of nodes greater than " + Config.quantityNodesToApplyKcoreN + ")");
					int numNodesOld = WholeSystem.getStreamGraphData().getRealTotalNodes();
					int numEdgesOld = WholeSystem.getStreamGraphData().getRealTotalEdges();
					num = WholeSystem.getStreamGraphData().applyKcoreN();
					Log.console(" - "+ (numNodesOld - WholeSystem.getStreamGraphData().getRealTotalNodes()) +" deleted nodes");
					Log.console(" ("+ num +" selected concepts)");
					Log.consoleln(" and "+ (numEdgesOld - WholeSystem.getStreamGraphData().getRealTotalEdges()) +" deleted edges");
					Log.consoleln("- Remained Stream Graph: "+WholeSystem.getStreamGraphData().getRealTotalNodes()+" nodes, "+WholeSystem.getStreamGraphData().getRealTotalEdges()+" edges.");
				}
				*/
				// conditional to apply n-degree filter
				if(WholeSystem.getStreamGraphData().getTotalNodes() > Config.quantityNodesToApplyNdegreeFilter) {
					Log.console("- Starting "+Config.kCoreN+"-degree filter algoritm (quantity of nodes greater than " + Config.quantityNodesToApplyNdegreeFilter + ")");
					int numNodesOld = WholeSystem.getStreamGraphData().getRealTotalNodes();
					int numEdgesOld = WholeSystem.getStreamGraphData().getRealTotalEdges();
					num = WholeSystem.getStreamGraphData().applyNdegreeFilter();
					Log.console(" - "+ (numNodesOld - WholeSystem.getStreamGraphData().getRealTotalNodes()) +" deleted nodes");
					Log.console(" ("+ num +" selected concepts)");
					Log.consoleln(" and "+ (numEdgesOld - WholeSystem.getStreamGraphData().getRealTotalEdges()) +" deleted edges");
					Log.consoleln("- Remained Stream Graph: "+WholeSystem.getStreamGraphData().getRealTotalNodes()+" nodes, "+WholeSystem.getStreamGraphData().getRealTotalEdges()+" edges.");
				}
							
				Log.console("- Building Gephi Graph Data, Nodes Table Hash and Nodes Table Array");
				quantityNodesEdges = currentSystemGraphData.buildGephiGraphData_NodesTableHash_NodesTableArray();
				Log.consoleln(" - "+quantityNodesEdges.getNumNodes()+" nodes, "+quantityNodesEdges.getNumEdges()+" edges in the graph structure.");

				if(Config.gephiVisualization)  WholeSystem.getStreamGraphData().getStreamGraph().clearSinks();

				Log.consoleln("- Calculating distance measures of the whole network.");
				currentSystemGraphData.getGephiGraphData().calculateGephiGraphDistanceMeasures();

				Log.consoleln("- Calculating eigenvector measure of the whole network.");
				currentSystemGraphData.getGephiGraphData().calculateGephiGraphEigenvectorMeasure();

				Log.consoleln("- Sorting measures of the whole network.");
				currentSystemGraphData.sortMeasuresWholeNetwork();

				Log.console("- Classifying connected component and building sub graphs");
				num = currentSystemGraphData.getGephiGraphData().classifyConnectedComponent();
				currentSystemGraphData.setConnectedComponentsCount(num);
                Log.consoleln(" - quantity of connected conponents: " + num + ".");

                Log.consoleln("- Building sub-graphs ranks.");
            	currentSystemGraphData.buildSubGraphRanks();
            	
				String nameFileGexf = Config.nameGEXFGraph + "_iteration" + (iteration<=9?"0"+iteration:iteration) + ".gexf";
				Log.consoleln("- Building Gephi Graph File (generated file: " + nameFileGexf + ").");
				currentSystemGraphData.getGephiGraphData().buildGephiGraphFile(nameFileGexf);

				Log.consoleln("- Building sub-graphs tables belong to connected components.");
				currentSystemGraphData.buildBasicTableGroupOriginalConceptsSubGraph();

				Log.consoleln("- Sorting connected componets ranks.");
				currentSystemGraphData.sortConnectecComponentRanks();

				Log.console("- Selecting largest nodes by betweenness+closeness");
				num = currentSystemGraphData.selectLargestNodesBetweennessCloseness(iteration);
				Log.consoleln(" - "+num+" new selected concepts.");
				
				Log.console("- Selecting largest nodes by eigenvector");
				num = currentSystemGraphData.selectLargestNodesEigenvector(iteration);
				Log.consoleln(" - "+num+" new selected concepts.");

				Log.consoleln("- Reporting selected nodes to new interation.");
				
				Log.outFileCompleteReport("Iteration "+iteration);
				Log.outFileShortReport("Iteration "+iteration);

				Log.outFileCompleteReport(currentSetQuerySparql.toString());
				Log.outFileShortReport(currentSetQuerySparql.toStringShort());
				
				Log.outFileCompleteReport(WholeSystem.getStreamGraphData().toString());
				Log.outFileShortReport(WholeSystem.getStreamGraphData().toStringShort());
				
				Log.outFileCompleteReport(currentSystemGraphData.toString());
				Log.outFileShortReport(currentSystemGraphData.toStringShort(Config.quantityNodesShortReport));
				
				report = currentSystemGraphData.reportSelectedNodes(iteration);			
				Log.outFileCompleteReport(report);
				Log.outFileShortReport(report);

				// conditionals to continue the iteration
				// verify the iteration limit				
				if(iteration == Config.maxIteration-1) {
					break;
				}
				// at least x iterations are necessary
				else if(iteration < Config.minIteration-1) {
					;
				}
				// checks if there is only one connected component 
				else if(currentSystemGraphData.getRanks().getCount() <= 1)
					break;
				
				// preparation to a new iteration
				Log.consoleln("- Preparing data to new iteration.");
				
				// extract new selected concepts
				GroupConcept newGroupConcept = WholeSystem.getConceptsRegister().getSelectedConcepts(iteration);
				// put the new concepts into the new instance of SetQuerySparql and add it in WholeSystem 
				newSetQuerySparql = new SetQuerySparql();
				newSetQuerySparql.insertListConcept(newGroupConcept);
				wholeSystem.insertListSetQuerySparql(newSetQuerySparql);
				
				// upgrade StreamGraph with label of new concepts - do not work!
				// WholeSystem.getStreamGraphData().addNewConceptsLabel(newGroupConcept);
				
				iteration++;
				isContinue = true;
			} while(isContinue);
			
			//Log.console("- Applying K-core 2 in the whole graph");
			//n = currentSystemGraphData.selectLargestNodesBetweennessCloseness(iteration);
			//Log.consoleln(" - "+n+" remainder nodes.");
			
			Log.consoleln("- Closing.");
		    if(Config.graphStreamVisualization) 
		    	WholeSystem.getStreamGraphData().getStreamGraph().clear();
			Log.close();
			Log.consoleln("- Ok!");
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
}
	
