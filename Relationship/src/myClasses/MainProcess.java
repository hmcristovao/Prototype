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
			PrintStream fileConsoleOut = new PrintStream(Constants.nameFileConsoleOut);
			PrintStream fileConsoleErr = new PrintStream(Constants.nameFileConsoleErr);
			System.setOut(fileConsoleOut);
			System.setErr(fileConsoleErr);
			
			parser = new Wrapterms(new FileInputStream(Constants.nameFileInput));
			Dataset originalDataset  = new Dataset();
			parser.start(originalDataset);
			Debug.DEBUG("1",originalDataset.toString());
			originalDataset.fillQuery();
			Debug.DEBUG("2",originalDataset.toString());
			originalDataset.fillRDFs();
			Debug.DEBUG("3",originalDataset.toString());
			
			Graph currentGraph = originalDataset.getGraph().getGraph();
						
			//currentGraph.display(true);
			
			JSONSender sender = new JSONSender("localhost", 8080, Constants.nameGephiWorkspace);
		    currentGraph.addSink(sender);
		    			
			originalDataset.buildGraph();
			Debug.DEBUG("4",originalDataset.toString());
			
			MainProcess.sleep();
			//currentGraph.clear();
			fileConsoleOut.close();
			fileConsoleErr.close();
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
	
	protected static void showConsoleList(ListQuerySparql list) {
		System.out.println("\n=====================================================\n");
		System.out.println(list);
	}
	
	protected static void sleep() {
		try {
			Thread.sleep(5000);
		} catch (Exception e) {}
	}
}
	
