// v2.0 - restruture in the log files and report

package main;

import graph.SystemGraphData;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.graphstream.graph.Graph;
import org.graphstream.stream.gephi.JSONSender;

import rdf.SetQuerySparql;
import basic.*;

public class MainProcess {	
	public static WholeSystem wholeSystem = new WholeSystem();
	public static String report;
	public static boolean isContinue = false;
	public static int iteration = 1;
	public static SetQuerySparql  currentSetQuerySparql;
	public static SystemGraphData currentSystemGraphData;
	public static Graph           currentGraph;
	public static SetQuerySparql  newSetQuerySparql;
		
	public static void body(Wrapterms parser) {
		try {
			Log.console("- Starting.");
			Log.init();	
			Log.console("- Parsing terms.");
			parser = new Wrapterms(new FileInputStream(Config.nameFileInput));
			wholeSystem.insertListSetQuerySparql(new SetQuerySparql());
			parser.start(wholeSystem.getListSetQuerySparql().getFirst());
			do {
				currentSetQuerySparql = wholeSystem.getListSetQuerySparql().get(iteration-1);
				Log.console("- Assembling queries (iteration "+iteration+").");
				currentSetQuerySparql.fillQuery();
				Log.console("- Collecting RDFs (iteration "+iteration+").");
				currentSetQuerySparql.fillRDFs();
				
				// if it is second iteration so forth, copy all objects of the last iteration
				if(iteration >= 2)
					currentSetQuerySparql.insertListQuerySparql(wholeSystem.getListSetQuerySparql().get(iteration-2).getListQuerySparql());
				
				wholeSystem.insertListSystemGraphData(new SystemGraphData(currentSetQuerySparql.getTotalOriginalConcepts()));
				currentSystemGraphData = wholeSystem.getListSystemGraphData().get(iteration-1);
				currentGraph = currentSystemGraphData.getStreamGraphData().getStreamGraph();
				if(Config.graphStreamVisualization) {
					Log.console("- Connecting Stream Visualization (iteration "+iteration+").");
					currentGraph.display(true);
				}
				if(Config.gephiVisualization) {
					Log.console("- Connecting with Gephi (iteration "+iteration+").");
					JSONSender sender = new JSONSender("localhost", 8080, Config.nameGephiWorkspace);
					currentGraph.addSink(sender);
				}

				Log.console("- Building Stream Graph Data (iteration "+iteration+").");
				currentSystemGraphData.getStreamGraphData().buildStreamGraphData(currentSetQuerySparql);

				Log.console("- Building Gephi Graph Data, Nodes Table Hash and Nodes Table Array (iteration "+iteration+").");
				currentSystemGraphData.buildGephiGraphData_NodesTableHash_NodesTableArray();

				Log.console("- Building Gephi Graph Table (iteration "+iteration+").");
				currentSystemGraphData.getGephiGraphData().buildGephiGraphTable();

				if(Config.gephiVisualization)  currentGraph.clearSinks();

				Log.console("- Calculating measures of the whole network (iteration "+iteration+").");
				currentSystemGraphData.calculateMeasuresWholeNetwork();

				Log.console("- Sorting measures of the whole network (iteration "+iteration+").");
				currentSystemGraphData.sortMeasuresWholeNetwork();

				Log.console("- Classifying connected component and building sub graphs (iteration "+iteration+").");
				currentSystemGraphData.classifyConnectedComponent_BuildSubGraph();

				Log.console("- Building sub-graphs tables belong to connected components (iteration "+iteration+").");
				currentSystemGraphData.buildBasicTableSubGraph();

				Log.console("- Sorting connected componets ranks (iteration "+iteration+").");
				currentSystemGraphData.sortConnectecComponentRanks();

				Log.console("- Analysing graph data and selecting candidate nodes (iteration "+iteration+").");
				currentSystemGraphData.analyseGraphData();

				Log.console("- Resuming selected nodes to new interation (iteration "+iteration+")"
						+ " - Connected conponent: "+currentSystemGraphData.getConnectedComponentsCount()+".");
				currentSystemGraphData.resumeSelectedNodes(currentSetQuerySparql);

				Log.console("- Reporting selected nodes to new interation (iteration "+iteration+")+"
						+ " - Quantity new concepts/original concepts: "
						+ currentSetQuerySparql.getListNewConcepts().size()+"/"
						+ currentSetQuerySparql.getLinkedListOriginalConcepts().size());
				
				Log.outFileCompleteReport("Iteration "+iteration);
				Log.outFileShortReport("Iteration "+iteration);
				Log.outFileCompleteReport(currentSetQuerySparql.toString());
				Log.outFileShortReport(currentSetQuerySparql.toStringShort());
				Log.outFileCompleteReport(currentSystemGraphData.toString());
				Log.outFileShortReport(currentSystemGraphData.toStringShort());
				report = currentSystemGraphData.reportSelectedNodes(currentSetQuerySparql);			
				Log.outFileCompleteReport(report);
				Log.outFileShortReport(report);
				
				// conditionals to continue the iteration
				// verify the iteration limit				
				if(iteration == Config.maxIteration) {
					break;
				}
				// at least x iterations are necessary
				else if(iteration <= Config.minIteration) {
					;
				}
				// checks if there are still more than one connected components 
				else if(currentSystemGraphData.getRanks().getCount() == 1)
					break;
				
				// preparation to a new iteration
				isContinue = true;
				newSetQuerySparql = new SetQuerySparql();
				// copy new concepts
				newSetQuerySparql.insertListConcept(currentSetQuerySparql.getListNewConcepts());
				wholeSystem.insertListSetQuerySparql(newSetQuerySparql);
				iteration++;
			} while(isContinue);
				
			Log.console("- Closing.");
		    if(Config.graphStreamVisualization) 
				currentGraph.clear();
			Log.close();
			Log.console("- Ok!");
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
	}
}
	
