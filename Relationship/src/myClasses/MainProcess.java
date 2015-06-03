package myClasses;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import org.graphstream.graph.Graph;
import org.graphstream.stream.gephi.JSONSender;

import basic.*;

public class MainProcess implements Constants {
	
	public static void head(Wrapterms parser) {
		try {
			SetQuerySparql originalSetQuerySparql = new SetQuerySparql();
			SystemGraphData systemGraphData = new SystemGraphData();

			PrintStream fileConsoleOut = new PrintStream(Constants.nameFileConsoleOut);
			//PrintStream fileConsoleErr = new PrintStream(Constants.nameFileConsoleErr);
			System.setOut(fileConsoleOut);
			//System.setErr(fileConsoleErr);
			
			Debug.err("Parsing terms...");
			parser = new Wrapterms(new FileInputStream(Constants.nameFileInput));
			parser.start(originalSetQuerySparql);
			Debug.out("1",originalSetQuerySparql.toString());
			
			Debug.err("Assembling queries...");
			originalSetQuerySparql.fillQuery();
			Debug.out("2",originalSetQuerySparql.toString());
			
			Debug.err("Collecting RDFs...");
			originalSetQuerySparql.fillRDFs();
			Debug.out("3",originalSetQuerySparql.toString());
			
			Debug.err("Building graph...");
			Graph currentGraph = systemGraphData.getStreamGraphData().getStreamGraph();
			if(Constants.graphStreamVisualization) 
				currentGraph.display(true);
			if(Constants.gephiVisualization) {
				JSONSender sender = new JSONSender("localhost", 8080, Constants.nameGephiWorkspace);
				currentGraph.addSink(sender);
			}
		    systemGraphData.buildSystemGraphData(originalSetQuerySparql);
		    if(Constants.gephiVisualization) {
				currentGraph.clearSinks();
			}
		    
		    Debug.err("Building ranks...");
			systemGraphData.buildRanks();
		    
		    Debug.err("Analysing graph data...");
			systemGraphData.analyseGraphData();

			Debug.err("Closing...");
		    if(Constants.graphStreamVisualization) 
				currentGraph.clear();
			fileConsoleOut.close();
			//fileConsoleErr.close();
			Debug.err("Ok!");
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
	
