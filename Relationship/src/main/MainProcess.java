package main;

import graph.SystemGraphData;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import org.graphstream.graph.Graph;
import org.graphstream.stream.gephi.JSONSender;

import rdf.SetQuerySparql;
import basic.*;

public class MainProcess {	
	public static WholeSystem wholeSystem = new WholeSystem();
	public static PrintStream printStreamOut = null, printStreamCompleteOut = null, printStreamShortOut = null, printStreamErr = null;
	public static String report;
	public static boolean isContinue = false;
	public static int iteration = 1;
	public static SetQuerySparql  currentSetQuerySparql;
	public static SystemGraphData currentSystemGraphData;
	public static Graph           currentGraph;
	public static SetQuerySparql  newSetQuerySparql;
	
	public static void body(Wrapterms parser) {
		try {
			Log.err("- Starting.");
			MainProcess.configPrintStream();
			Log.err("- Parsing terms.");
			parser = new Wrapterms(new FileInputStream(Config.nameFileInput));
			wholeSystem.insertListSetQuerySparql(new SetQuerySparql());
			parser.start(wholeSystem.getListSetQuerySparql().getFirst());
			do {
				currentSetQuerySparql = wholeSystem.getListSetQuerySparql().get(iteration-1);
				Log.err("- Assembling queries (iteration "+iteration+").");
				currentSetQuerySparql.fillQuery();
				Log.err("- Collecting RDFs (iteration "+iteration+").");
				if(Config.disableWarningLog4j) System.setErr(new PrintStream(Config.nameFileConsoleWarn)); 
				currentSetQuerySparql.fillRDFs();
				if(Config.disableWarningLog4j) System.setErr(printStreamErr); 

				// if it is second iteration so forth, copy all objects of the last iteration
				if(iteration >= 2)
					currentSetQuerySparql.insertListQuerySparql(wholeSystem.getListSetQuerySparql().get(iteration-2).getListQuerySparql());
				
				wholeSystem.insertListSystemGraphData(new SystemGraphData(currentSetQuerySparql.getTotalOriginalConcepts()));
				currentSystemGraphData = wholeSystem.getListSystemGraphData().get(iteration-1);
				currentGraph = currentSystemGraphData.getStreamGraphData().getStreamGraph();
				if(Config.graphStreamVisualization) {
					Log.err("- Connecting Stream Visualization (iteration "+iteration+").");
					currentGraph.display(true);
				}
				if(Config.gephiVisualization) {
					Log.err("- Connecting with Gephi (iteration "+iteration+").");
					JSONSender sender = new JSONSender("localhost", 8080, Config.nameGephiWorkspace);
					currentGraph.addSink(sender);
				}

				Log.err("- Building Stream Graph Data (iteration "+iteration+").");
				currentSystemGraphData.getStreamGraphData().buildStreamGraphData(currentSetQuerySparql);

				Log.err("- Building Gephi Graph Data, Nodes Table Hash and Nodes Table Array (iteration "+iteration+").");
				currentSystemGraphData.buildGephiGraphData_NodesTableHash_NodesTableArray();

				Log.err("- Building Gephi Graph Table (iteration "+iteration+").");
				currentSystemGraphData.getGephiGraphData().buildGephiGraphTable();

				if(Config.gephiVisualization)  currentGraph.clearSinks();

				Log.err("- Calculating measures of the whole network (iteration "+iteration+").");
				currentSystemGraphData.calculateMeasuresWholeNetwork();

				Log.err("- Sorting measures of the whole network (iteration "+iteration+").");
				currentSystemGraphData.sortMeasuresWholeNetwork();

				Log.err("- Classifying connected component and building sub graphs (iteration "+iteration+").");
				currentSystemGraphData.classifyConnectedComponent_BuildSubGraph();

				Log.err("- Building sub-graphs tables belong to connected components (iteration "+iteration+").");
				currentSystemGraphData.buildBasicTableSubGraph();

				Log.err("- Sorting connected componets ranks (iteration "+iteration+").");
				currentSystemGraphData.sortConnectecComponentRanks();

				Log.err("- Analysing graph data and selecting candidate nodes (iteration "+iteration+").");
				currentSystemGraphData.analyseGraphData();

				Log.err("- Resuming selected nodes to new interation (iteration "+iteration+")"
						+ " - Connected conponent: "+currentSystemGraphData.getConnectedComponentsCount()+".");
				currentSystemGraphData.resumeSelectedNodes(currentSetQuerySparql);

				Log.err("- Reporting selected nodes to new interation (iteration "+iteration+")+"
						+ " - Quantity new concepts/original concepts: "
						+ currentSetQuerySparql.getListNewConcepts().size()+"/"
						+ currentSetQuerySparql.getLinkedListOriginalConcepts().size());
				
				Log.out(printStreamCompleteOut, "Iteration "+iteration);
				Log.out(printStreamShortOut,    "Iteration "+iteration);
				Log.out(printStreamCompleteOut, currentSystemGraphData.toString());
				//Log.out(printStreamShortOut,    currentSystemGraphData.toStringShort());
				report = currentSystemGraphData.reportSelectedNodes(currentSetQuerySparql);			
				Log.out(printStreamCompleteOut, report);
				Log.out(printStreamShortOut,    report);
				
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
				
			Log.err("- Closing.");
		    if(Config.graphStreamVisualization) 
				currentGraph.clear();
			
			Log.err("- Ok!");
		    printStreamOut.close();
		    printStreamErr.close();
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
	
	public static void configPrintStream() throws Exception {
		if(Config.outPrintConsole) 
			printStreamOut = System.out;
		else {
			printStreamCompleteOut = new PrintStream(Config.nameFileConsoleCompletOut);
			printStreamShortOut    = new PrintStream(Config.nameFileConsoleCompletOut);				
		}
		if(Config.errPrintConsole) 
			printStreamErr = System.err;
		else
			printStreamErr = new PrintStream(Config.nameFileConsoleErr);
		System.setOut(printStreamOut);
		System.setErr(printStreamErr);
	}

}
	
