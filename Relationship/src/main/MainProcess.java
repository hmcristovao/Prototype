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
	public static void body(Wrapterms parser) {
		try {
			Debug.err("- Starting.");
			PrintStream printStreamOut = null, printStreamErr = null;
			if(Config.outPrintConsole) 
				printStreamOut = System.out;
			else
				printStreamOut = new PrintStream(Config.nameFileConsoleOut);

			if(Config.errPrintConsole) 
				printStreamErr = System.err;
			else
				printStreamErr = new PrintStream(Config.nameFileConsoleErr);
			System.setOut(printStreamOut);
			System.setErr(printStreamErr);
			
			Debug.err("- Parsing terms.");
			parser = new Wrapterms(new FileInputStream(Config.nameFileInput));
			
			WholeSystem wholeSystem = new WholeSystem();
			wholeSystem.insertListSetQuerySparql(new SetQuerySparql());
			parser.start(wholeSystem.getListSetQuerySparql().getFirst());
			
			boolean isContinue = false;
			int interation = 0;
			SetQuerySparql  currentSetQuerySparql;
			SystemGraphData currentSystemGraphData;
			Graph           currentGraph;
			do {
				currentSetQuerySparql = wholeSystem.getListSetQuerySparql().get(interation);
				Debug.out("Debug 1 (Interaction "+interation+")", currentSetQuerySparql.toString());

				Debug.err("- Assembling queries.");
				currentSetQuerySparql.fillQuery();

				Debug.err("- Collecting RDFs.");
				if(Config.disableWarningLog4j) 
					System.setErr(new PrintStream(Config.nameFileConsoleWarn)); 
				currentSetQuerySparql.fillRDFs();
				if(Config.disableWarningLog4j) 
					System.setErr(printStreamErr); 

				Debug.out("Debug 2 (Interaction "+interation+")",currentSetQuerySparql.toString());

				wholeSystem.insertListSystemGraphData(new SystemGraphData(currentSetQuerySparql.getTotalConcepts()));
				currentSystemGraphData = wholeSystem.getListSystemGraphData().get(interation);
				currentGraph = currentSystemGraphData.getStreamGraphData().getStreamGraph();
				if(Config.graphStreamVisualization) {
					Debug.err("- Connecting Stream Visualization.");
					currentGraph.display(true);
				}
				if(Config.gephiVisualization) {
					Debug.err("- Connecting with Gephi.");
					JSONSender sender = new JSONSender("localhost", 8080, Config.nameGephiWorkspace);
					currentGraph.addSink(sender);
				}

				Debug.err("- Building Stream Graph Data.");
				currentSystemGraphData.getStreamGraphData().buildStreamGraphData(currentSetQuerySparql);

				Debug.err("- Building Gephi Graph Data, Nodes Table Hash and Nodes Table Array.");
				currentSystemGraphData.buildGephiGraphData_NodesTableHash_NodesTableArray();

				Debug.err("- Building Gephi Graph Table.");
				currentSystemGraphData.getGephiGraphData().buildGephiGraphTable();

				if(Config.gephiVisualization) {
					currentGraph.clearSinks();
				}

				Debug.err("- Calculating measures of the whole network.");
				currentSystemGraphData.calculateMeasuresWholeNetwork();

				Debug.err("- Sorting measures of the whole network.");
				currentSystemGraphData.sortMeasuresWholeNetwork();

				Debug.err("- Classifying connected component and building sub graphs.");
				currentSystemGraphData.classifyConnectedComponent_BuildSubGraph();

				Debug.err("- Building sub-graphs tables belong to connected components.");
				currentSystemGraphData.buildBasicTableSubGraph();

				Debug.err("- Sorting connected componets ranks.");
				currentSystemGraphData.sortConnectecComponentRanks();

				Debug.err("- Analysing graph data and selecting candidate nodes.");
				currentSystemGraphData.analyseGraphData();

				Debug.out("Debug 3 (Interaction "+interation+")",currentSystemGraphData.toString());

				Debug.err("- Resuming selected nodes to new interation.");
				currentSystemGraphData.resumeSelectedNodes(currentSetQuerySparql);

				Debug.err("- Reporting selected nodes to new interation.");
				Debug.out(currentSystemGraphData.reportSelectedNodes(currentSetQuerySparql));			
			
				interation++;
				
			} while(isContinue);
				
			Debug.err("- Closing.");
		    if(Config.graphStreamVisualization) 
				currentGraph.clear();
			
			Debug.err("- Ok!");
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
}
	
