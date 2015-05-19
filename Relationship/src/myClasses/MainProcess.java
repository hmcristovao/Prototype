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
			GraphData graphData = new GraphData();

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
			Graph currentGraph = graphData.getGraph();
			//currentGraph.display(true);
			JSONSender sender = new JSONSender("localhost", 8080, Constants.nameGephiWorkspace);
		    currentGraph.addSink(sender);

		    Debug.err("Drawing graph...");
		    graphData.buildGraph(originalSetQuerySparql);
			Debug.out("4",graphData.toString());

		    Debug.err("Computing betweenness centrality...");
		    graphData.computeBetweennessCentrality();
		    
			//currentGraph.clear();
			fileConsoleOut.close();
			//fileConsoleErr.close();
		}
		catch(FileNotFoundException e) {
			System.out.println("Error: file not found.");
			e.printStackTrace();
		}
		catch (IOException e) {
			System.out.println("Error: problem with the persistent file: " + e.getMessage());
			e.printStackTrace();
		}
		catch(TokenMgrError e) {
			System.out.println("Lexical error: " + e.getMessage());
			e.printStackTrace();
		}
		catch(SemanticException e) {
			System.out.println("Semantic error: " + e.getMessage());
			e.printStackTrace();
		}
		catch(ParseException e) {
			System.out.println("Sintax error: " + e.getMessage());
			e.printStackTrace();
		}
		// get the rest
		catch(Exception e) {
			System.out.println("Other: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	protected static void showConsoleList(SetQuerySparql list) {
		System.out.println("\n=====================================================\n");
		System.out.println(list);
	}
}
	
