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
			int iteration = 1;
			SetQuerySparql  currentSetQuerySparql;
			SystemGraphData currentSystemGraphData;
			Graph           currentGraph;
			SetQuerySparql  newSetQuerySparql;
			do {
				currentSetQuerySparql = wholeSystem.getListSetQuerySparql().get(iteration-1);
				if(iteration == 1)
					Debug.out("Debug 1 (iteration "+iteration+").", currentSetQuerySparql.toString());
				else
					Debug.out("Debug 1 (iteration "+iteration+") - ONLY NEW CONCEPTS.", currentSetQuerySparql.toString());

				Debug.err("- Assembling queries (iteration "+iteration+").");
				currentSetQuerySparql.fillQuery();

				Debug.err("- Collecting RDFs (iteration "+iteration+").");
				if(Config.disableWarningLog4j) 
					System.setErr(new PrintStream(Config.nameFileConsoleWarn)); 
				currentSetQuerySparql.fillRDFs();
				if(Config.disableWarningLog4j) 
					System.setErr(printStreamErr); 

				// if it is second iteration so forth, copy all objects of the last iteration
				if(iteration >= 2)
					currentSetQuerySparql.insertListQuerySparql(wholeSystem.getListSetQuerySparql().get(iteration-2).getListQuerySparql());
				
				Debug.out("Debug 2 (iteration "+iteration+").",currentSetQuerySparql.toString());

				wholeSystem.insertListSystemGraphData(new SystemGraphData(currentSetQuerySparql.getTotalConcepts()));
				currentSystemGraphData = wholeSystem.getListSystemGraphData().get(iteration-1);
				currentGraph = currentSystemGraphData.getStreamGraphData().getStreamGraph();
				if(Config.graphStreamVisualization) {
					Debug.err("- Connecting Stream Visualization (iteration "+iteration+").");
					currentGraph.display(true);
				}
				if(Config.gephiVisualization) {
					Debug.err("- Connecting with Gephi (iteration "+iteration+").");
					JSONSender sender = new JSONSender("localhost", 8080, Config.nameGephiWorkspace);
					currentGraph.addSink(sender);
				}

				Debug.err("- Building Stream Graph Data (iteration "+iteration+").");
				currentSystemGraphData.getStreamGraphData().buildStreamGraphData(currentSetQuerySparql);

				Debug.err("- Building Gephi Graph Data, Nodes Table Hash and Nodes Table Array (iteration "+iteration+").");
				currentSystemGraphData.buildGephiGraphData_NodesTableHash_NodesTableArray();

				Debug.err("- Building Gephi Graph Table (iteration "+iteration+").");
				currentSystemGraphData.getGephiGraphData().buildGephiGraphTable();

				if(Config.gephiVisualization) {
					currentGraph.clearSinks();
				}

				Debug.err("- Calculating measures of the whole network (iteration "+iteration+").");
				currentSystemGraphData.calculateMeasuresWholeNetwork();

				Debug.err("- Sorting measures of the whole network (iteration "+iteration+").");
				currentSystemGraphData.sortMeasuresWholeNetwork();

				Debug.err("- Classifying connected component and building sub graphs (iteration "+iteration+").");
				currentSystemGraphData.classifyConnectedComponent_BuildSubGraph();

				Debug.err("- Building sub-graphs tables belong to connected components (iteration "+iteration+").");
				currentSystemGraphData.buildBasicTableSubGraph();

				Debug.err("- Sorting connected componets ranks (iteration "+iteration+").");
				currentSystemGraphData.sortConnectecComponentRanks();

				Debug.err("- Analysing graph data and selecting candidate nodes (iteration "+iteration+").");
				currentSystemGraphData.analyseGraphData();

				Debug.out("Debug 3 (iteration "+iteration+").",currentSystemGraphData.toString());

				Debug.err("- Resuming selected nodes to new interation (iteration "+iteration+")"
						+ " - Connected conponent: "+currentSystemGraphData.getConnectedComponentsCount()+".");
				currentSystemGraphData.resumeSelectedNodes(currentSetQuerySparql);

				Debug.err("- Reporting selected nodes to new interation (iteration "+iteration+")+"
						+ " - Quantity new concepts/original concepts: "
						+ currentSetQuerySparql.getListNewConcepts().size()+"/"
						+ currentSetQuerySparql.getLinkedListOriginalConcepts().size());
				Debug.out("Iteration "+iteration);
				Debug.out(currentSystemGraphData.reportSelectedNodes(currentSetQuerySparql)+".");			
			
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
	
