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

public class MainProcess implements Constants {
	
	public static void head(Wrapterms parser) {
		try {
			Debug.err("- Starting.");
			PrintStream printStreamOut = null, printStreamErr = null;
			if(Constants.outPrintConsole) 
				printStreamOut = System.out;
			else
				printStreamOut = new PrintStream(Constants.nameFileConsoleOut);

			if(Constants.errPrintConsole) 
				printStreamErr = System.err;
			else
				printStreamErr = new PrintStream(Constants.nameFileConsoleErr);
			System.setOut(printStreamOut);
			System.setErr(printStreamErr);
			
			SetQuerySparql originalSetQuerySparql = new SetQuerySparql();
			
			Debug.err("- Parsing terms.");
			parser = new Wrapterms(new FileInputStream(Constants.nameFileInput));
			parser.start(originalSetQuerySparql);
			
			Debug.out("Debug 1",originalSetQuerySparql.toString());
			
			Debug.err("- Assembling queries.");
			originalSetQuerySparql.fillQuery();
			
			Debug.err("- Collecting RDFs.");
			if(disableWarningLog4j) 
				System.setErr(new PrintStream(Constants.nameFileConsoleWarn)); 
			originalSetQuerySparql.fillRDFs();
			if(disableWarningLog4j) 
				System.setErr(printStreamErr); 
			
			Debug.out("Debug 2",originalSetQuerySparql.toString());
			
			SystemGraphData systemGraphData = new SystemGraphData(originalSetQuerySparql.getTotalConcepts());
			Graph currentGraph = systemGraphData.getStreamGraphData().getStreamGraph();
			if(Constants.graphStreamVisualization) {
				Debug.err("- Connecting Stream Visualization.");
				currentGraph.display(true);
			}
			if(Constants.gephiVisualization) {
				Debug.err("- Connecting with Gephi.");
				JSONSender sender = new JSONSender("localhost", 8080, Constants.nameGephiWorkspace);
				currentGraph.addSink(sender);
			}
		    
			Debug.err("- Building Stream Graph Data.");
			systemGraphData.getStreamGraphData().buildStreamGraphData(originalSetQuerySparql);
			
			Debug.err("- Building Gephi Graph Data, Nodes Table Hash and Nodes Table Array.");
			systemGraphData.buildGephiGraphData_NodesTableHash_NodesTableArray();
				
			Debug.err("- Building Gephi Graph Table.");
			systemGraphData.getGephiGraphData().buildGephiGraphTable();
			
		    if(Constants.gephiVisualization) {
				currentGraph.clearSinks();
			}
		    
		    Debug.err("- Calculating measures of the whole network.");
			systemGraphData.calculateMeasuresWholeNetwork();
		    
		    Debug.err("- Sorting measures of the whole network.");
			systemGraphData.sortMeasuresWholeNetwork();
			
			Debug.err("- Classifying connected component and building sub graphs.");
			systemGraphData.classifyConnectedComponent_BuildSubGraph();
		    
			Debug.err("- Building sub-graphs tables belong to connected components.");
			systemGraphData.buildBasicTableSubGraph();

			Debug.err("- Sorting connected componets ranks.");
			systemGraphData.sortConnectecComponentRanks();

			Debug.out("Debug 3",systemGraphData.toString());
		    
		    Debug.err("- Analysing graph data.");
			systemGraphData.analyseGraphData();

			Debug.err("- Closing.");
		    if(Constants.graphStreamVisualization) 
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
	
