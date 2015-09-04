// v5.3 - under construction... level of relationship between original concepts  

package main;

import graph.NodeData;
import graph.QuantityNodesEdges;
import graph.StreamGraphData.DeletedStatus;
import graph.SystemGraphData;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.stream.gephi.JSONSender;

import parse.*;
import rdf.SetQuerySparql;
import user.*;

// this class manages the main process and it does system logs
public class MainProcess {	
	public static int iteration, baseConnectedComponentCount, nodeDataPos;
	public static SetQuerySparql  currentSetQuerySparql;
	public static SystemGraphData currentSystemGraphData, oldSystemGraphData;
	public static NodeData nodeDataWithLeastEccentricityAndAverage;  // used for selectedConcepts(off) and remainigConcepts
	public static Node currentNode;
	public static List<Edge> currentEdgeSet;
	public static Concept currentConcept;
	
	public enum Time {whileIteration, afterIteration, afterSelectionMainConcepts, finalGraph };
	
	public static void body(Wrapterms parser) throws Exception {
		try {
			iteration = 0;
			start();
			parseTerms(parser);
			parseUselessConcepts(parser);
			parseVocabulary(parser);
			readRdfsFileNameToRdfsFileTable();
			do {
				indicateIterationNumber();
				updateCurrentSetQuerySparqlVar();
				assemblyQueries();
				collectRDFsAllQueries();
				removeConceptsWithZeroRdfs();  // exit whether original concept has zero RDFs
				createCurrentSystemGraphData();
				connectStreamVisualization();
				buildStreamGraphData_buildEdgeTable_fromRdfs();
				showQuantitiesStreamGraph();
				if(iteration >= 1) 
					copyAllObjectsLastIteration();
				if(iteration >= Config.iterationTriggerApplyNDegreeFilterAlgorithm 
				   && WholeSystem.getStreamGraphData().getRealTotalNodes() > Config.quantityNodesToApplyNdegreeFilter) 
 					applyNDegreeFilterTrigger(Config.nDegreeFilter, false);
//				if(iteration >= Config.iterationTriggerApplyKCoreFilterAlgorithm) 
//					applyKCoreFilterTrigger(Config.kCoreFilter);
				buildGephiGraphData_NodesTableHash_NodesTableArray_fromStreamGraph();
				clearStreamGraphSink();
				classifyConnectedComponent();
				
				calculateRelationshipLevelBetweenOriginalConcepts();  // somente se connect > 1
				
                buildGexfGraphFile(Time.whileIteration);
				// conditionals set to continue the iteration
				if(breakIteration())
					break;
				calculateDistanceMeasuresWholeNetwork();
				storeDistanceMeasuresWholeNetworkToMainNodeTable();
				calculateEigenvectorMeasureWholeNetwork();
				storeEigenvectorMeasuresWholeNetworkToMainNodeTable();
				sortMeasuresWholeNetwork();
				buildSubGraphsRanks();
				buildSubGraphsTablesInConnectedComponents();
				sortConnectedComponentsRanks();
				selectLargestNodesByBetweennessCloseness();
				selectLargestNodesByEigenvector();
				reportSelectedNodesToNewIteration();
				if(Config.additionNewConceptWithoutCategory)
					duplicateConceptsWithoutCategory(iteration);				
				prepareDataToNewIteration();
				iteration++;
			} while(true);
			indicateAlgorithmIntermediateStage1(); // after iterations loop

//			// while count nodes > goal * 20, apply k-core 
//			for(int k=2; WholeSystem.getStreamGraphData().getRealTotalNodes() > WholeSystem.getGoalConceptsQuantity()*20; k++) 
//				applyKCoreFilterTrigger(k); 

			for(int n=2; WholeSystem.getStreamGraphData().getRealTotalNodes() > Config.quantityNodesToApplyNdegreeFilter_levelHardToDiscardRelationship; n++) 
				applyNDegreeFilterTrigger(n, false); 
			
			for(int n=2; WholeSystem.getStreamGraphData().getRealTotalNodes() > Config.quantityNodesToApplyNdegreeFilter_levelSoftToApplyRelationship; n++) 
				applyNDegreeFilterTrigger(n, true); 

			iteration++;
			createCurrentSystemGraphData();
			buildGephiGraphData_NodesTableHash_NodesTableArray_fromStreamGraph();
			calculateDistanceMeasuresWholeNetwork();
			storeDistanceMeasuresWholeNetworkToMainNodeTable();
			calculateEigenvectorMeasureWholeNetwork();
			storeEigenvectorMeasuresWholeNetworkToMainNodeTable();
			classifyConnectedComponent();
 			buildGexfGraphFile(Time.afterIteration);
			indicateAlgorithmIntermediateStage2(); // selection main concepts through calculate connected componentes
			// createSortEccentricityAndAverageOnlySelectedConcepts(); // exit whether zero selected concepts
			createSortEccentricityAndAverageOnlyRemainingConcepts();
			baseConnectedComponentCount = currentSystemGraphData.getConnectedComponentsCount();
			nodeDataPos = 0;
			// loop while:
			// 1. quantity of selected concepts + original concepts > goal of total concepts, AND
			// 2. there are not concepts to try the remotion (because the quantity of componentes connected is improving)
			while(WholeSystem.getSortEccentricityAndAverageRemainingConcepts().getCount() + WholeSystem.getQuantityOriginalConcepts() > WholeSystem.getGoalConceptsQuantity()
				  && nodeDataPos < WholeSystem.getSortEccentricityAndAverageRemainingConcepts().getCount()) {
			
				//getNodeDataWithLeastEccentricityAndAverageFromSelectedConcepts();
				getNodeDataWithLeastEccentricityAndAverageFromRemainingConcepts();
				storeCurrentInformationsAboutEnvironmentAndNodeWillBeDeleted();
				// delete the node in the stream graph
				currentConcept = WholeSystem.getConceptsRegister().getConcept(currentNode.getId());
				DeletedStatus deletedStatus = WholeSystem.getStreamGraphData().deleteNode(currentNode, false);
				// create new environment (with a new gephi graph) and 
				// calculate the new connected component
				iteration++;
				createCurrentSystemGraphData();
				buildGephiGraphData_NodesTableHash_NodesTableArray_fromStreamGraph();				
				classifyConnectedComponent(); 
				// if connected component quantity improved then recover the deleted node, edges (to stream graph)
				if(currentSystemGraphData.getConnectedComponentsCount() > baseConnectedComponentCount) {
					recoverEnvironmentAndNodeAndEdges(deletedStatus);
					nodeDataPos++;
					iteration--;
				}
				// if ok then start at the first node again (the least average node)
				else {
					calculateDistanceMeasuresWholeNetwork();
					storeDistanceMeasuresWholeNetworkToMainNodeTable();
					calculateEigenvectorMeasureWholeNetwork();
					storeEigenvectorMeasuresWholeNetworkToMainNodeTable();
					nodeDataPos = 0;
					createSortEccentricityAndAverageOnlyRemainingConcepts(); 
				}
			} 
			// verify whether the goal was achieved: quantity of selected concepts + original concepts == goal of total concepts
			if(WholeSystem.getSortEccentricityAndAverageRemainingConcepts().getCount() + WholeSystem.getQuantityOriginalConcepts() <= WholeSystem.getGoalConceptsQuantity())
				Log.consoleln("- Goal achieved!!!"); 
			else {
				// aqui entra  um processo para eliminar nós segundo o Realtionship, que é mais fraco que connected component
				Log.consoleln("- Goal did not achieve.");
				indicateAlgorithmIntermediateStage3(); // selection main concepts through calculate of level of relationship between original concepts

				iteration++;
				createCurrentSystemGraphData();
				buildGephiGraphData_NodesTableHash_NodesTableArray_fromStreamGraph();
				calculateDistanceMeasuresWholeNetwork();
				storeDistanceMeasuresWholeNetworkToMainNodeTable();
				calculateEigenvectorMeasureWholeNetwork();
				storeEigenvectorMeasuresWholeNetworkToMainNodeTable();
				
				// createSortEccentricityAndAverageOnlySelectedConcepts(); // exit whether zero selected concepts
				createSortEccentricityAndAverageOnlyRemainingConcepts();
				nodeDataPos = 0;
				// loop while:
				// 1. quantity of selected concepts + original concepts > goal of total concepts, AND
				// 2. there are not concepts to try the remotion (because the quantity of level of relationship is improving)
				while(WholeSystem.getSortEccentricityAndAverageRemainingConcepts().getCount() + WholeSystem.getQuantityOriginalConcepts() > WholeSystem.getGoalConceptsQuantity()
						&& nodeDataPos < WholeSystem.getSortEccentricityAndAverageRemainingConcepts().getCount()) {

					getNodeDataWithLeastEccentricityAndAverageFromRemainingConcepts();
					storeCurrentInformationsAboutEnvironmentAndNodeWillBeDeleted();
					// delete the node in the stream graph
					currentConcept = WholeSystem.getConceptsRegister().getConcept(currentNode.getId());
					DeletedStatus deletedStatus = WholeSystem.getStreamGraphData().deleteNode(currentNode, true);
					// create new environment (with a new gephi graph) and 
					// calculate the new connected component
					iteration++;
					createCurrentSystemGraphData();
					buildGephiGraphData_NodesTableHash_NodesTableArray_fromStreamGraph();				
					// if connected component quantity improved then recover the deleted node, edges (to stream graph)
					if(deletedStatus == DeletedStatus.no_RecoveredNode) {
						recoverEnvironment();
						nodeDataPos++;
						iteration--;
					}
					// if ok then start at the first node again (the least average node)
					else {
						calculateDistanceMeasuresWholeNetwork();
						storeDistanceMeasuresWholeNetworkToMainNodeTable();
						calculateEigenvectorMeasureWholeNetwork();
						storeEigenvectorMeasuresWholeNetworkToMainNodeTable();
						nodeDataPos = 0;
						createSortEccentricityAndAverageOnlyRemainingConcepts(); 
					}
				} 
				// verify whether the goal was achieved: quantity of selected concepts + original concepts == goal of total concepts
				if(WholeSystem.getSortEccentricityAndAverageRemainingConcepts().getCount() + WholeSystem.getQuantityOriginalConcepts() <= WholeSystem.getGoalConceptsQuantity())
					Log.consoleln("- Goal achieved!!!"); 
				else {
					if(WholeSystem.getSortEccentricityAndAverageRemainingConcepts().getCount() + WholeSystem.getQuantityOriginalConcepts() <= WholeSystem.getMaxConceptsQuantity())
						Log.consoleln("   (However, it is less than the maximum "+WholeSystem.getMaxConceptsQuantity()+" nodes)");
					else
						Log.consoleln("- Goal did not achieve, after use of two methods (connected component and relationship between original concepts).");
				}
			}
			// reportAfterSelectionMainConcepts_selectedConcepts();
			reportAfterSelectionMainConcepts_remainingConcepts();

			// create the last GEXF file that represent the graph
            buildGexfGraphFile(Time.afterSelectionMainConcepts);
			indicateAlgorithmFinalStage(); // building concept map
     		//
			// falta aqui criar o particionamento, ou bem antes!!!
			//
			// create a GEXF file, like as concept map
            buildGexfGraphFile(Time.finalGraph);
            showUselessConceptsStatistic();
            buildRawConceptMapFromStreamGraph();
			upgradeConceptMap_heuristic_01_removeLinkNumber();
			upgradeConceptMap_heuristic_02_vocabularyTable();
			upgradeConceptMap_heuristic_03_categoryInTargetConcept();
			upgradeConceptMap_heuristic_04_categoryInSourceConcept();
            upgradeConceptMap_heuristic_05_removeSelfReference();
            upgradeConceptMap_heuristic_06_createOriginalConceptsWithZeroDegree();
            
			// create txt file of final concept map
			buildGexfGraphFileFromConceptMap();
			buildTxtFileFromConceptMap();
			
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
		Log.initFiles();
		Log.consoleln("- Starting.");
	}
	public static void parseTerms(Wrapterms parser) throws Exception {
		Log.console("- Parsing user terms");
		parser = new Wrapterms(new FileInputStream(Config.nameUserTermsFile));
		WholeSystem.insertListSetQuerySparql(new SetQuerySparql());
		parser.parseUserTerms(WholeSystem.getListSetQuerySparql().getFirst());
		WholeSystem.initQuantityOriginalConcepts(WholeSystem.getConceptsRegister().size());
		WholeSystem.initGoalMaxConceptsQuantity();
		WholeSystem.setOriginalConcepts(WholeSystem.getConceptsRegister().getOriginalConcepts());
		Log.consoleln(" - " + WholeSystem.getQuantityOriginalConcepts() + " terms parsed.");
		String sameReport = "Quantity of terms parsed: " + WholeSystem.getQuantityOriginalConcepts() + 
				            " (file: "+Config.nameUserTermsFile+")\n"; 
		Log.outFileCompleteReport(sameReport + WholeSystem.getOriginalConcepts().toStringLong());
		Log.outFileShortReport(sameReport + WholeSystem.getOriginalConcepts().toString());
	}
	public static void parseUselessConcepts(Wrapterms parser) throws Exception {
		Log.console("- Parsing useless concepts");
		parser = new Wrapterms(new FileInputStream(Config.nameUselessConceptsFile));
		parser.parseUselessConcepts(WholeSystem.getUselessConceptsTable());
		Log.consoleln(" - " + WholeSystem.getUselessConceptsTable().size() + " concepts parsed.");
		String sameReport = "Quantity of useless concepts parsed: " + WholeSystem.getUselessConceptsTable().size() +   
                " (file: "+Config.nameUselessConceptsFile+")\n" +
				"\nUseless concepts parsed:\n" + WholeSystem.getUselessConceptsTable().toString();
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void parseVocabulary(Wrapterms parser) throws Exception {
		Log.console("- Parsing vocabulary");
		parser = new Wrapterms(new FileInputStream(Config.nameVocabularyFile));
		parser.parseSystemVocabulary(WholeSystem.getVocabularyTable());
		Log.consoleln(" - " + WholeSystem.getVocabularyTable().size() + " sentences parsed.");
		String sameReport = "Quantity of vocabulary sentences parsed: " + WholeSystem.getVocabularyTable().size() +   
                " (file: "+Config.nameVocabularyFile+")\n" +
				"\nVocabulary table parsed:\n" + WholeSystem.getVocabularyTable().toString();
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void readRdfsFileNameToRdfsFileTable() throws Exception {
		Log.console("- Reading RDFs files names to put in table");
		WholeSystem.getRdfsFileTable().init(Config.dirRdfsPersistenceFiles);  
 		Log.consoleln(" - " + WholeSystem.getRdfsFileTable().size() + " RDFs files identified.");
		String sameReport = "RDFs files identified: "+WholeSystem.getRdfsFileTable().size()+".";
		Log.outFileCompleteReport(sameReport+"\n"+WholeSystem.getRdfsFileTable());
		Log.outFileShortReport(sameReport);			
	}
	
	public static void indicateIterationNumber() throws Exception {
		Log.consoleln("\n*** Iteration "+iteration+" ***");
		String sameReport = Config.starsLine+"Iteration "+iteration+Config.starsLine;
        Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void indicateAlgorithmIntermediateStage1() throws Exception {
		Log.consoleln("\n*** Intermediate stage 1 (start the cut in nodes graph) ***");
		String sameReport = Config.starsLine+"Intermediate stage 1 (start the cut in nodes graph)"+Config.starsLine;
        Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void indicateAlgorithmIntermediateStage2() throws Exception {
		String sameReport = "Current concepts quantity: "+
		        WholeSystem.getStreamGraphData().getTotalNodes()+" stream graph  ("+
		        WholeSystem.getQuantityOriginalConcepts() + " original concepts).  "+
                "Goal of concepts quantity: "+
		        WholeSystem.getGoalConceptsQuantity()+ " good, "+
                WholeSystem.getMaxConceptsQuantity()+" maximum.";
		Log.consoleln("\n*** Intermediate stage 2 (selection main concepts) ***\n- " + sameReport);
		String sameReport2 = Config.starsLine+"Intermediate stage 2 (selection main concepts)"+Config.starsLine;
		Log.outFileCompleteReport(sameReport2+sameReport);
		Log.outFileShortReport(sameReport2+sameReport);
	}
	public static void indicateAlgorithmIntermediateStage3() throws Exception {
		String sameReport = "Current concepts quantity: "+
		        WholeSystem.getStreamGraphData().getTotalNodes()+" stream graph  ("+
		        WholeSystem.getQuantityOriginalConcepts() + " original concepts).  "+
                "Goal of concepts quantity: "+
		        WholeSystem.getGoalConceptsQuantity()+ " good, "+
                WholeSystem.getMaxConceptsQuantity()+" maximum.";
		Log.consoleln("\n*** Intermediate stage 3 (selection remaining overage concepts by calculate of level relationship between original concepts ***\n- "+ sameReport);
		String sameReport2 = Config.starsLine+"Intermediate stage 3 (selection remaining overage concepts by calculate of level relationship between original concepts)"+Config.starsLine;
		Log.outFileCompleteReport(sameReport2+sameReport);
		Log.outFileShortReport(sameReport2+sameReport);
	}
	
	public static void indicateAlgorithmFinalStage() throws Exception {
		Log.consoleln("\n*** Final stage (building concept map) ***");
		String sameReport = Config.starsLine+"Final stage (building concept map)"+Config.starsLine;
        Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void updateCurrentSetQuerySparqlVar() throws Exception {
		currentSetQuerySparql = WholeSystem.getListSetQuerySparql().get(iteration);
	}
	public static void assemblyQueries() throws Exception {
		Log.console("- Assembling queries");
		int num = currentSetQuerySparql.assemblyQueries();
		Log.consoleln(" - "+num+" new queries assembled.");
		String sameReport = "Queries assembled: " + num + "\n";
        Log.outFileCompleteReport(sameReport + currentSetQuerySparql.toString());
		Log.outFileShortReport(sameReport + currentSetQuerySparql.toStringShort());
	}
	public static void collectRDFsAllQueries() throws Exception {
		Log.console("- Collecting RDFs");
		Count numRdfsInInternet = new Count(0);
		Count numRdfsInFile     = new Count(0);
		int num =  currentSetQuerySparql.collectRDFsAllQueries(numRdfsInInternet, numRdfsInFile);
		Log.consoleln(" - "+num+" new RDFs triples collected ("+numRdfsInInternet+" in internet, "+numRdfsInFile+" in file).");
		// extract collected quantity of RDFs to each concept
		StringBuffer conceptsOut = new StringBuffer();
		DecimalFormat formater =  new DecimalFormat("00000");
		for(int i=0; i < currentSetQuerySparql.getTotalConcepts(); i++) {
			conceptsOut.append("\n");
			conceptsOut.append(formater.format(currentSetQuerySparql.getListQuerySparql().get(i).getListRDF().size()));
			conceptsOut.append(" RDFs to concept \"");
			conceptsOut.append(currentSetQuerySparql.getListQuerySparql().get(i).getConcept().getBlankName());
			conceptsOut.append("\"");
		}
		String sameReport = "Total collected RDFs: " + num + " ("+numRdfsInInternet+" in internet, "+numRdfsInFile+" in file)\n" + conceptsOut.toString();
        Log.outFileCompleteReport(sameReport + "\n\n" + currentSetQuerySparql.toString());
		Log.outFileShortReport(sameReport);
	}
	public static void removeConceptsWithZeroRdfs() throws Exception {
		Log.console("- Looking for concepts with zero RDFs");
		ConceptsGroup excludedConcepts = currentSetQuerySparql.removeConceptsWithZeroRdfs();
		if(excludedConcepts.size() == 0)
			Log.consoleln(" - neither concept found.");
		else
			Log.consoleln(" - " + excludedConcepts.size() + " concepts found and excluded.");
		String sameReport = "Concepts with zero RDFs: ";
		if(excludedConcepts.size() == 0) 
			sameReport += "neither";
		else
			sameReport += "\n" + excludedConcepts.toString();
	    Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
		// verify whether there is any original concept with zero RDFs
		if(excludedConcepts.getQuantityOriginalConcept() > 0) {
			String sameReport2 = "*** Algorithm stoped - there is original concept with zero RDFs\n";
			sameReport2 += excludedConcepts.getOriginalConcepts().toString();
			Log.consoleln(sameReport2);
			Log.outFileCompleteReport(sameReport2);
			Log.outFileShortReport(sameReport2);
			end();
			System.exit(1);
		}
	}	
	public static void createCurrentSystemGraphData() throws Exception {
		WholeSystem.insertListSystemGraphData(new SystemGraphData());
		currentSystemGraphData = WholeSystem.getListSystemGraphData().get(iteration);
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
	// get RDFs and convert them to StreamGraph, but this fucntion is call: 
	//    in the firt iteration build StreamGraphData and EdgeTable
	//    in the second iteration so foth, just add new data into StreamGraphData and EdgeTable
	// discard useless concepts (use WholeSystem.uselessConceptsTable to do this operation)
	public static void buildStreamGraphData_buildEdgeTable_fromRdfs() throws Exception {
		Log.console("- Building Stream Graph Data");
		Count countUselessRDFs = new Count(0);
		QuantityNodesEdges quantityNodesEdges = WholeSystem.getStreamGraphData().buildStreamGraphData_buildEdgeTable_fromRdfs(currentSetQuerySparql, countUselessRDFs);
		Log.consoleln(" - "+quantityNodesEdges.getNumNodes()+" new nodes, "+quantityNodesEdges.getNumEdges()+" new edges in Stream Graph - " +
				countUselessRDFs + " useless RDFs.");
		Log.consoleln("- Creating edge hash table - "+WholeSystem.getEdgesTable().size()+" edges.");
		String sameReport = "Stream Graph Data created (graph used in the preview): \n" + 
		        quantityNodesEdges.getNumNodes() + " new nodes, " + 
				quantityNodesEdges.getNumEdges() + " new edges in the visualization graph.\n" +
		        WholeSystem.getStreamGraphData().getRealTotalNodes() + " total nodes, " +
		        WholeSystem.getStreamGraphData().getRealTotalEdges() + " total edges\n" +
		        countUselessRDFs + " useless RDFs.";
		String sameReport2 = "\n\nEdge hash table created:" + 
				"\n("+WholeSystem.getEdgesTable().size()+" edges).";
        Log.outFileCompleteReport(sameReport + WholeSystem.getStreamGraphData().toString() + sameReport2 + "\n"+WholeSystem.getEdgesTable().toString());
		Log.outFileShortReport(sameReport + WholeSystem.getStreamGraphData().toStringShort() + sameReport2);
	}
	public static void showQuantitiesStreamGraph() throws Exception {
		Log.consoleln("- Quantities Stream Graph built: "+WholeSystem.getStreamGraphData().getRealTotalNodes()+
				" nodes, "+WholeSystem.getStreamGraphData().getRealTotalEdges()+" edges.");
	}
	// if it is second iteration so forth, copy all objects (ListQuerySparql) of the last iteration
	public static void copyAllObjectsLastIteration() throws Exception {
		Log.console("- Second iteration or more: copying old elements of the last iteration");
		int n = currentSetQuerySparql.insertListQuerySparql(WholeSystem.getListSetQuerySparql().get(iteration-1).getListQuerySparql());
		Log.consoleln(" - "+n+" elements copied.");
		String sameReport = "Second iteration or more: "+n+" old elements copied from last iteration.\n";
        Log.outFileCompleteReport(sameReport + WholeSystem.getListSetQuerySparql().get(iteration-1).toString());
		Log.outFileShortReport(sameReport + WholeSystem.getListSetQuerySparql().get(iteration-1).toStringShort());
	}
	public static void applyNDegreeFilterTrigger(int n, boolean isForcedRelationship) throws Exception {
		Log.console("- Starting "+n+"-degree filter algorithm "+
				(isForcedRelationship ? "(" : "(non ") + "forced relationship between original concepts)");
		Count quantityDeletedSelectedConcepts = new Count(0);
		Count quantityRecoveredNodes = new Count(0);
		int numOldNodes = WholeSystem.getStreamGraphData().getRealTotalNodes();
		int numOldEdges = WholeSystem.getStreamGraphData().getRealTotalEdges();
		// call algorithm:
		WholeSystem.getStreamGraphData().applyNdegreeFilterTrigger(n, isForcedRelationship, quantityDeletedSelectedConcepts, quantityRecoveredNodes);
		int numCurrentNodes = WholeSystem.getStreamGraphData().getRealTotalNodes();
		int numCurrentEdges = WholeSystem.getStreamGraphData().getRealTotalEdges();
		Log.console(" - "+ quantityRecoveredNodes + " recovered nodes");
		Log.console(" - "+ (numOldNodes - numCurrentNodes) +" deleted nodes");
		Log.console(" ("+ quantityDeletedSelectedConcepts +" selected concepts)");
		Log.consoleln(" and "+ (numOldEdges - numCurrentEdges) +" deleted edges.");
		Log.consoleln("- Remained Stream Graph: "+numCurrentNodes+" nodes, "+numCurrentEdges+" edges.");
		String sameReport = "Runned "+n+"-degree filter algorithm "+
				(isForcedRelationship ? "(" : "(non ") + "forced relationship between original concepts)" +
				quantityRecoveredNodes + " recovered nodes - " +
				(numOldNodes - numCurrentNodes) +" deleted nodes" +
				"("+ quantityDeletedSelectedConcepts +" selected concepts)" + 
				" and "+ (numOldEdges - numCurrentEdges) +" deleted edges" +
				"\nOld Stream Graph: "+numOldNodes+" nodes, "+numOldEdges+" edges." +
				"\nRemained Stream Graph: "+numCurrentNodes+" nodes, "+numCurrentEdges+" edges.";
        Log.outFileCompleteReport(sameReport + "\n\n" + WholeSystem.getStreamGraphData().toString() );
		Log.outFileShortReport(sameReport);
	}			
	public static void applyKCoreFilterTrigger(int k, boolean isForcedRelationship) throws Exception {
		Log.console("- Starting "+ k +"-core filter algorithm ");
		Count quantityDeletedSelectedConcepts = new Count(0);	
		Count quantityRecoveredNodes = new Count(0);
		int numOldNodes = WholeSystem.getStreamGraphData().getRealTotalNodes();
		int numOldEdges = WholeSystem.getStreamGraphData().getRealTotalEdges();
		// call algorithm:
		WholeSystem.getStreamGraphData().applyKCoreFilterTrigger(k, isForcedRelationship, quantityDeletedSelectedConcepts, quantityRecoveredNodes);
		int numCurrentNodes = WholeSystem.getStreamGraphData().getRealTotalNodes();
		int numCurrentEdges = WholeSystem.getStreamGraphData().getRealTotalEdges();
		Log.console(" - "+ quantityRecoveredNodes + " recovered nodes");
		Log.console(" - "+ (numOldNodes - numCurrentNodes) +" deleted nodes");
		Log.console(" ("+ quantityDeletedSelectedConcepts +" selected concepts)");
		Log.consoleln(" and "+ (numOldEdges - numCurrentEdges) +" deleted edges");
		Log.consoleln("- Remained Stream Graph: "+numCurrentNodes+" nodes, "+numCurrentEdges+" edges.");
		String sameReport = "Runned " + k + "-core filter algorithm\n" +
				(isForcedRelationship ? "(" : "(non ") + "forced relationship between original concepts)" +
				quantityRecoveredNodes + " recovered nodes - " +
				(numOldNodes - numCurrentNodes) +" deleted nodes" +
				"("+ quantityDeletedSelectedConcepts +" selected concepts)" + 
				" and "+ (numOldEdges - numCurrentEdges) +" deleted edges" +
				"\nOld Stream Graph: "+numOldNodes+" nodes, "+numOldEdges+" edges." +
				"\nRemained Stream Graph: "+numCurrentNodes+" nodes, "+numCurrentEdges+" edges.";
        Log.outFileCompleteReport(sameReport + "\n\n" + WholeSystem.getStreamGraphData().toString() );
		Log.outFileShortReport(sameReport);
	}
			
	public static void buildGephiGraphData_NodesTableHash_NodesTableArray_fromStreamGraph() throws Exception {
		Log.console("- Building Gephi Graph Data, Nodes Table Hash and Nodes Table Array from Stream Graph");
		// call function:
		QuantityNodesEdges quantityNodesEdges = currentSystemGraphData.buildGephiGraphData_NodesTableHash_NodesTableArray_fromStreamGraph();
		Log.consoleln(" - "+quantityNodesEdges.getNumNodes()+" nodes, "+quantityNodesEdges.getNumEdges()+" edges in the graph structure.");
		String sameReport = "Built Gephi Graph Data, Nodes Table Hash and Nodes Table Array from Stream Graph\n"+
				quantityNodesEdges.getNumNodes()+" nodes, "+quantityNodesEdges.getNumEdges()+" edges in the graph structure." +
				"\nReal quantities: "+currentSystemGraphData.getGephiGraphData().getRealQuantityNodesEdges().toString();
		Log.outFileCompleteReport(sameReport + "\n" + currentSystemGraphData.getGephiGraphData().toString());
		Log.outFileShortReport(sameReport);
	}
	public static void clearStreamGraphSink() throws Exception {
		if(Config.gephiVisualization)  
			WholeSystem.getStreamGraphData().getStreamGraph().clearSinks();
	}
	public static void calculateDistanceMeasuresWholeNetwork() throws Exception {
		Log.console("- Calculating distance measures of the whole network");
		currentSystemGraphData.getGephiGraphData().calculateGephiGraphDistanceMeasures();
		Log.consoleln(" - "+currentSystemGraphData.getGephiGraphData().getRealQuantityNodesEdges().toString() + ".");
		String sameReport = "Distance measures of the whole network calculated." + 
				"\n(betweenness and closeness to "+currentSystemGraphData.getGephiGraphData().getRealQuantityNodesEdges().toString()+")";
        Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void calculateEigenvectorMeasureWholeNetwork() throws Exception {
		Log.console("- Calculating eigenvector measure of the whole network");
		currentSystemGraphData.getGephiGraphData().calculateGephiGraphEigenvectorMeasure();
		Log.consoleln(" - "+currentSystemGraphData.getGephiGraphData().getRealQuantityNodesEdges().toString() + ".");
		String sameReport = "Eigenvector measure of the whole network calculated." + 
				"\n(eigenvector to "+currentSystemGraphData.getGephiGraphData().getRealQuantityNodesEdges().toString()+")";
        Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void storeDistanceMeasuresWholeNetworkToMainNodeTable() throws Exception {
		Log.consoleln("- Storing distance measures of the whole network to main node table.");
		currentSystemGraphData.storeDistanceMeasuresWholeNetwork();
		String sameReport = "Stored distance measures of the whole network to main node table.";
        Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	} 
	public static void storeEigenvectorMeasuresWholeNetworkToMainNodeTable() throws Exception {
		Log.consoleln("- Storing eigenvector measures of the whole network to main node table.");
		currentSystemGraphData.storeEigenvectorMeasuresWholeNetwork();
		String sameReport = "Stored eigenvector measures of the whole network to main node table.";
        Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	} 
	public static void sortMeasuresWholeNetwork() throws Exception {
		Log.consoleln("- Sorting measures of the whole network.");
		currentSystemGraphData.sortBetweennessWholeNetwork();
		currentSystemGraphData.sortClosenessWholeNetwork();
		currentSystemGraphData.sortEccentricityWholeNetwork();
		currentSystemGraphData.sortEigenvectorWholeNetwork();
		String sameReport = "Sorted measures of the whole network.";
        Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void calculateRelationshipLevelBetweenOriginalConcepts() throws Exception {
		Log.console("- Calculating level of relationship between original concepts - ");
		int num = 0;
		if(currentSystemGraphData.getConnectedComponentsCount() > 1)
			num = WholeSystem.getStreamGraphData().calculateRelationshipLevelBetweenOriginalConcepts();
		int maximumLevel=0;
		for(int i=1; i < WholeSystem.getQuantityOriginalConcepts(); i++)
			maximumLevel += i;
		String sameReport = "level: " + num + "/" + maximumLevel + " ("+ (int)(((double)(maximumLevel-num)/maximumLevel)*100)+"% complete).";
		Log.consoleln(sameReport);
		String sameReport2 = "Calculated level of relationship between original concepts\n";
        Log.outFileCompleteReport(sameReport+sameReport2);
		Log.outFileShortReport(sameReport+sameReport2);
	}	
	// work with current gephi graph (wherefore is better before to use: buildGephiGraphData_NodesTableHash_NodesTableArray_fromStreamGraph())
	public static void classifyConnectedComponent() throws Exception {
		Log.console("- Classifying connected component");
		int num = currentSystemGraphData.getGephiGraphData().classifyConnectedComponent();
		currentSystemGraphData.setConnectedComponentsCount(num);
		Log.consoleln(" - quantity of connected components: " + num + ".");
		String sameReport = "Connected component classified\n" + 
				num + " connected components.";
        Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void buildSubGraphsRanks() throws Exception {
		Log.consoleln("- Building sub-graphs ranks.");
		currentSystemGraphData.buildSubGraphRanks();
		String sameReport = "Sub-graphs ranks built.";
        Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void buildGexfGraphFile(Time time) throws Exception {
		String nameFileGexf = null;
		if(time == Time.whileIteration) 
			nameFileGexf = Config.nameGexfGraphFile + "_iteration" + (iteration<=9?"0"+iteration:iteration) + ".gexf";
		else if(time == Time.afterIteration)
	   		nameFileGexf = Config.nameGexfGraphFile + "_after_iterations.gexf";
		else if(time == Time.afterSelectionMainConcepts)
	   		nameFileGexf = Config.nameGexfGraphFile + "_after_selection_main_concepts.gexf";
		else if(time == Time.finalGraph)
			nameFileGexf = Config.nameGexfGraphFile + "_final.gexf";	
		Log.console("- Building GEXF Graph File");
		currentSystemGraphData.getGephiGraphData().buildGexfGraphFile(nameFileGexf);
		Log.consoleln(" (generated file: " + nameFileGexf + ").");
		String sameReport = "GEXF graph file generated: " + nameFileGexf;
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void buildSubGraphsTablesInConnectedComponents() throws Exception {
		Log.consoleln("- Building sub-graphs tables belong to connected components.");
		currentSystemGraphData.buildSubGraphsTablesInConnectedComponents();
		String sameReport = "Sub-graphs tables belong to connected components built.";
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void sortConnectedComponentsRanks() throws Exception {
		Log.consoleln("- Sorting connected components ranks.");
		currentSystemGraphData.sortConnectecComponentRanks();
		String sameReport = "Connected components ranks sorted.";
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void selectLargestNodesByBetweennessCloseness() throws Exception {
		Log.console("- Selecting largest nodes by betweenness+closeness");
		int num = currentSystemGraphData.selectLargestNodesBetweennessCloseness(iteration);
		Log.consoleln(" - "+num+" new selected concepts.");
		String sameReport = "Largest nodes by betweenness+closeness: " + num + " nodes.";
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void selectLargestNodesByEigenvector() throws Exception {
		Log.console("- Selecting largest nodes by eigenvector");
		int num = currentSystemGraphData.selectLargestNodesEigenvector(iteration);
		Log.consoleln(" - "+num+" new selected concepts.");
		String sameReport = "Largest nodes by eigenvector: " + num + " nodes."; 
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void reportSelectedNodesToNewIteration() throws Exception {
		Log.consoleln("- Reporting selected nodes to new iteration.");
		Log.outFileCompleteReport("Iteration "+iteration);
		Log.outFileShortReport("Iteration "+iteration);
		Log.outFileCompleteReport(currentSystemGraphData.toString());
		// Log.outFileShortReport(currentSystemGraphData.toStringShort(Config.quantityNodesShortReport));
		String sameReport = currentSystemGraphData.reportSelectedNodes(iteration);			
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void duplicateConceptsWithoutCategory(int iteration) throws Exception {
		Log.console("- Duplicating concepts with \"ConceptCategory:\" subword");
		ConceptsGroup newConcepts = WholeSystem.getConceptsRegister().duplicateConceptsWithoutCategory(iteration);
		Log.consoleln(" - "+newConcepts.size()+" new concepts inserted.");
		String sameReport = "Duplicated "+newConcepts.size()+" concepts with \"ConceptCategory:\" subword:\n"
				            + newConcepts.toString(); 
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}	
	public static boolean breakIteration() throws Exception {
		boolean isBreak = false;
		// verify the iteration limit				
		if(iteration == Config.maxIteration-1) {
			isBreak = true;
			Log.consoleln("- Ending loop, maximum iteration quantity "+(Config.maxIteration)+" reached.");
			String sameReport = "Loop ended, maximum iteration quantity "+(Config.maxIteration)+" reached.";
			Log.outFileCompleteReport(sameReport);
			Log.outFileShortReport(sameReport);
		}
		// at least x iterations are necessary
		else if(iteration < Config.minIteration-1) {
			isBreak = false;
		}
		// checks if there is only one connected component 
		else if(currentSystemGraphData.getConnectedComponentsCount() == 1) {
			isBreak = true;
			Log.consoleln("- Ending loop, 1 connected component reached and at least "+(Config.minIteration)+" iterations.");
			String sameReport = "Loop ended, 1 connected component reached and at least "+(Config.minIteration)+" iterations.";
			Log.outFileCompleteReport(sameReport);
			Log.outFileShortReport(sameReport);
		}
		return isBreak;
	}
	public static void prepareDataToNewIteration() throws Exception {
		// preparation to a new iteration
		Log.console("- Preparing data to new iteration");
		// extract new selected concepts
		ConceptsGroup newGroupConcept = WholeSystem.getConceptsRegister().getSelectedConcepts(iteration);
		// put the new concepts into the new instance of SetQuerySparql and add it in WholeSystem 
		SetQuerySparql newSetQuerySparql = new SetQuerySparql();
		newSetQuerySparql.insertListConcept(newGroupConcept);
		WholeSystem.insertListSetQuerySparql(newSetQuerySparql);
		Log.consoleln(" - "+newGroupConcept.size()+" concepts inserted in the set of query Sparql.");
		String sameReport = "Data to new iteration prepared.\n" +
				newGroupConcept.size()+" concepts inserted in the set of query Sparql.\n";
        Log.outFileCompleteReport(sameReport + newGroupConcept.toStringLong());
		Log.outFileShortReport(sameReport + newGroupConcept.toString());
	}
	public static void deleteCommonNodes_remainOriginalAndSelectedConcepts() throws Exception {
		Log.console("- Deleting common nodes, remain only original and selected concepts");
		int numOldNodes = WholeSystem.getStreamGraphData().getRealTotalNodes();
		int numOldEdges = WholeSystem.getStreamGraphData().getRealTotalEdges();
		// call algorithm:
		WholeSystem.getStreamGraphData().deleteCommonNodes_remainOriginalAndSelectedConcepts();
		int numCurrentNodes = WholeSystem.getStreamGraphData().getRealTotalNodes();
		int numCurrentEdges = WholeSystem.getStreamGraphData().getRealTotalEdges();
		Log.console(" - "+ (numOldNodes - numCurrentNodes) +" deleted nodes ");
		Log.consoleln(" and "+ (numOldEdges - numCurrentEdges) +" deleted edges.");
		Log.consoleln("- Remained Stream Graph: "+numCurrentNodes+" nodes, "+numCurrentEdges+" edges.");
		String sameReport = "Deleted all common nodes (remain only original and selected concepts) \n" +
				(numOldNodes - numCurrentNodes) +" deleted nodes" +
				" and "+ (numOldEdges - numCurrentEdges) +" deleted edges" +
				"\nOld Stream Graph: "+numOldNodes+" nodes, "+numOldEdges+" edges." +
				"\nRemained Stream Graph: "+numCurrentNodes+" nodes, "+numCurrentEdges+" edges.";
		Log.outFileCompleteReport(sameReport + "\n\n" + WholeSystem.getStreamGraphData().toString() );
		Log.outFileShortReport(sameReport);
	}
	
	// get group of selected concepts and copy them to a new NodesTableArray
	// sort this table e store it in WholeSystem.sortEccentricityAndAverageSelectedConcepts
	// (do not enter: original concepts, concepts that already were category or concepts with zero rdfs)
	public static void createSortEccentricityAndAverageOnlySelectedConcepts() throws Exception {
		Log.console("- Creating eccentricity and average sort table of selected concepts");
		currentSystemGraphData.createSortEccentricityAndAverageOnlySelectedConcepts();
		Log.consoleln(" - "+WholeSystem.getSortEccentricityAndAverageSelectedConcepts().getCount()+" nodes stored and sorted.");
		String sameReport = "Table of selected nodes created and eccentricity and average (betweenness, closeness, eigenvector) sorted.\n";
		Log.outFileCompleteReport(sameReport+WholeSystem.getSortEccentricityAndAverageSelectedConcepts().toString());
		Log.outFileShortReport(sameReport);	
		// zero remain concepts: stop right now
		if(WholeSystem.getSortEccentricityAndAverageSelectedConcepts().getCount() == 0) {
			Log.consoleln("- Stoping. It's not possible to continue with zero selected concepts.");
			sameReport = "Algorithm stoped. It's not possible to continue with zero selected concepts.";
			Log.outFileCompleteReport(sameReport);
			Log.outFileShortReport(sameReport);	
			end();
			System.exit(0);
		}
	}

	// get group of remaining concepts in Stream Graph (after iterations) and copy them to a new NodesTableArray
	// sort this table e store it in WholeSystem.sortEccentricityAndAverageRemainingConcepts
	// (do not enter: original concepts, concepts that already were category or concepts with zero rdfs)
	public static void createSortEccentricityAndAverageOnlyRemainingConcepts() throws Exception {
		Log.console("- Creating eccentricity and average sort table of remaining concepts");
		currentSystemGraphData.createSortEccentricityAndAverageOnlyRemainingConcepts();
		Log.consoleln(" - "+WholeSystem.getSortEccentricityAndAverageRemainingConcepts().getCount()+" nodes stored and sorted.");
		String sameReport = "Table of remaining nodes created and eccentricity and average (betweenness, closeness, eigenvector) sorted.\n";
		Log.outFileCompleteReport(sameReport+WholeSystem.getSortEccentricityAndAverageRemainingConcepts().toString());
		Log.outFileShortReport(sameReport);	
	}
	
	
	public static void getNodeDataWithLeastEccentricityAndAverageFromSelectedConcepts() throws Exception {
		nodeDataWithLeastEccentricityAndAverage = WholeSystem.getSortEccentricityAndAverageSelectedConcepts().getNodeData(nodeDataPos);
		String sameReport = "Node data with least eccentricity and average: "
	               +nodeDataWithLeastEccentricityAndAverage.getShortName()
	               +" (position in group: "+nodeDataPos+"/"+WholeSystem.getSortEccentricityAndAverageRemainingConcepts().getCount()+")";
		Log.consoleln("- "+sameReport);
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);	
	}
	public static void getNodeDataWithLeastEccentricityAndAverageFromRemainingConcepts() throws Exception {
		nodeDataWithLeastEccentricityAndAverage = WholeSystem.getSortEccentricityAndAverageRemainingConcepts().getNodeData(nodeDataPos);
		String sameReport = "Node data with least eccentricity and average: "
	               +nodeDataWithLeastEccentricityAndAverage.getShortName()
	               +" (position in group: "+nodeDataPos+"/"+WholeSystem.getSortEccentricityAndAverageRemainingConcepts().getCount()+")";
		Log.consoleln("- "+sameReport);
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);	
	}
	public static void storeCurrentInformationsAboutEnvironmentAndNodeWillBeDeleted() {
		// store the current informations of the environment and node that will be deleted (because it can be recovered)
		oldSystemGraphData = currentSystemGraphData;
		currentNode = nodeDataWithLeastEccentricityAndAverage.getStreamNode();
		currentEdgeSet = new ArrayList<Edge>();
		for(Edge currentEdge : currentNode.getEdgeSet()) {
			currentEdgeSet.add(currentEdge);
		}
	}
	
	public static void recoverEnvironmentAndNodeAndEdges(DeletedStatus deletedStatus) throws Exception {
		String sameReport = "Node did not exclude: "+nodeDataWithLeastEccentricityAndAverage.getShortName()
				      +" (connected component improves "+baseConnectedComponentCount+" to "+currentSystemGraphData.getConnectedComponentsCount()+")";
		Log.consoleln("- "+sameReport);
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);	
		WholeSystem.getStreamGraphData().insert(currentNode,currentEdgeSet);
		// recover the last environment
		WholeSystem.getListSystemGraphData().remove(iteration);
		// whether excludedConcept is selected concept, then insert it again into WholeSystem.conceptsRegister
		if(deletedStatus == DeletedStatus.yes_SelectedConcept)
			WholeSystem.getConceptsRegister().add(currentConcept);	
		currentSystemGraphData = oldSystemGraphData;
	}
	public static void recoverEnvironment() throws Exception {
		String sameReport = "Node did not exclude: "+nodeDataWithLeastEccentricityAndAverage.getShortName()
				      +" (level of relationship between original concepts improves)";
		Log.consoleln("- "+sameReport);
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);	
		// recover the last environment
		WholeSystem.getListSystemGraphData().remove(iteration);
		// whether excludedConcept is selected concept, then insert it again into WholeSystem.conceptsRegister
		currentSystemGraphData = oldSystemGraphData;
	}	
	public static void reportAfterSelectionMainConcepts_selectedConcepts() throws Exception {
		String sameReport = "Total concepts:\n"  
				+ "  "+WholeSystem.getSortEccentricityAndAverageSelectedConcepts().getCount() + " selected concepts + " 
				+ WholeSystem.getQuantityOriginalConcepts() + " original concepts = " 
				+ (WholeSystem.getSortEccentricityAndAverageSelectedConcepts().getCount() + WholeSystem.getQuantityOriginalConcepts()) + " total concepts"
				+ "  (goal "+WholeSystem.getGoalConceptsQuantity()
				+ " to " + WholeSystem.getMaxConceptsQuantity() + ")\n"
				+ "  Connected component count: " + currentSystemGraphData.getConnectedComponentsCount()
				+ " (base: " + baseConnectedComponentCount + ")\n" 
				+ "  Stream Graph: "+WholeSystem.getStreamGraphData().getRealTotalNodes() + " nodes, " 
				+ WholeSystem.getStreamGraphData().getRealTotalEdges() + " edges";
		Log.consoleln("- "+sameReport);
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);	
	}
	public static void reportAfterSelectionMainConcepts_remainingConcepts() throws Exception {
		String sameReport = "Total concepts:\n"  
				+ "  "+WholeSystem.getSortEccentricityAndAverageRemainingConcepts().getCount() + " remaining concepts + " 
				+ WholeSystem.getQuantityOriginalConcepts() + " original concepts = " 
				+ (WholeSystem.getSortEccentricityAndAverageRemainingConcepts().getCount() + WholeSystem.getQuantityOriginalConcepts()) + " total concepts"
				+ "  (goal "+WholeSystem.getGoalConceptsQuantity()
				+ " to " + WholeSystem.getMaxConceptsQuantity() + ")\n"
				+ "  Connected component count: " + currentSystemGraphData.getConnectedComponentsCount()
				+ " (base: " + baseConnectedComponentCount + ")\n" 
				+ "  Stream Graph: "+WholeSystem.getStreamGraphData().getRealTotalNodes() + " nodes, " 
				+ WholeSystem.getStreamGraphData().getRealTotalEdges() + " edges";
		Log.consoleln("- "+sameReport);
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);	
	}

	public static void showUselessConceptsStatistic() throws Exception {
		Log.consoleln("- Recording in log statistic of useless concepts.");
		String sameReport = "Statistic of useless concepts:\n" + WholeSystem.getUselessConceptsTable().toString();
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	
	public static void buildRawConceptMapFromStreamGraph()  throws Exception {
		Log.console("- Building raw propositions of the concept map");
		int n =currentSystemGraphData.buildRawConceptMapFromStreamGraph();
		Log.consoleln(" - "+WholeSystem.getConceptMap().size()+" proposition created (" + n + " repeated propositions - eliminated).");
		String sameReport = "Built "+WholeSystem.getConceptMap().size()+" raw propositions of the concept map (" + 
		                     n + " repeated propositions - eliminated):\n" + WholeSystem.getConceptMap().toString();
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);		
	}
	public static void buildGexfGraphFileFromConceptMap() throws Exception {
		String nameFileGexf = Config.nameGexfGraphFile + "_concept_map.gexf";
		Log.console("- Building GEXF Graph File from final concept map");
		WholeSystem.getConceptMap().buildGexfGraphFileFromConceptMap(nameFileGexf);
		Log.consoleln(" (generated file: " + nameFileGexf + ").");
		String sameReport = "GEXF graph file generated: " + nameFileGexf;
        Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	public static void buildTxtFileFromConceptMap() throws Exception {
		Log.console("- Building TXT final Concept Map");
		WholeSystem.getConceptMap().buildTxtFileFromConceptMap(Config.nameTxtConceptMapFile);
		Log.consoleln(" (generated file: " + Config.nameTxtConceptMapFile + ").");
		String sameReport = "TXT concept map generated: " + Config.nameTxtConceptMapFile;
        Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
	}
	

	
	public static void upgradeConceptMap_heuristic_01_removeLinkNumber()  throws Exception {
		Log.console("- Upgrading the concept map with first heuristic (remove link id number)");
		int n = WholeSystem.getConceptMap().upgradeConceptMap_heuristic_01_removeLinkNumber();
		Log.consoleln(" - " + n + " propositions changed.");
		String sameReport = "Heuristic 01: upgraded "+n+" concept map propositions with remove of link number:\n" + WholeSystem.getConceptMap().toString();
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);		
	}
	public static void upgradeConceptMap_heuristic_02_vocabularyTable()  throws Exception {
		Log.console("- Upgrading the concept map with second heuristic (change links with vocabulary table)");
		int n = WholeSystem.getConceptMap().upgradeConceptMap_heuristic_02_vocabularyTable();
		Log.consoleln(" - " + n + " links name changed.");
		String sameReport = "Heuristic 02: upgraded "+n+" concept map propositions with use of link vocabulary table:\n" + WholeSystem.getConceptMap().toString();
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);		
	}
	public static void upgradeConceptMap_heuristic_03_categoryInTargetConcept()  throws Exception {
		Log.console("- Upgrading the concept map with third heuristic (change category in target concepts and links)");
		int n = WholeSystem.getConceptMap().upgradeConceptMap_heuristic_03_categoryInTargetConcept();
		Log.consoleln(" - " + n + " propositions changed.");
		String sameReport = "Heuristic 03: upgraded "+n+" concept map propositions with change of category in target concept:\n" + WholeSystem.getConceptMap().toString();
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);		
	}
	public static void upgradeConceptMap_heuristic_04_categoryInSourceConcept()  throws Exception {
		Log.console("- Upgrading the concept map with fourth heuristic (change category in source concept)");
		int n = WholeSystem.getConceptMap().upgradeConceptMap_heuristic_04_categoryInSourceConcept();
		Log.consoleln(" - " + n + " propositions changed.");
		String sameReport = "Heuristic 04: upgraded "+n+" concept map propositions with change of category in source concept:\n" + WholeSystem.getConceptMap().toString();
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);		
	}
	public static void upgradeConceptMap_heuristic_05_removeSelfReference()  throws Exception {
		Log.console("- Upgrading the concept map with fifth heuristic (remove self references)");
		int n = WholeSystem.getConceptMap().upgradeConceptMap_heuristic_05_removeSelfReference();
		Log.consoleln(" - " + n + " propositions changed.");
		String sameReport = "Heuristic 05: upgraded "+n+" concept map propositions with remotions of the self reference:\n" + WholeSystem.getConceptMap().toString();
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);		
	}
	public static void upgradeConceptMap_heuristic_06_createOriginalConceptsWithZeroDegree()  throws Exception {
		Log.console("- Upgrading the concept map with sixth heuristic (create original concepts with zero degrees)");
		ConceptsGroup originalConcepts = WholeSystem.getConceptMap().upgradeConceptMap_heuristic_06_createOriginalConceptsWithZeroDegree(currentSystemGraphData);
		Log.consoleln(" - " + originalConcepts.size() + " propositions changed.");
		String sameReport = "Heuristic 06: upgraded "+originalConcepts.size()+" alone original concepts created:\n" + 
							originalConcepts + "\n" + WholeSystem.getConceptMap().toString();
		Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);		
	}
	
	
	
	public static void end() throws Exception {
		Log.consoleln("- Closing.");
		if(Config.graphStreamVisualization) 
			WholeSystem.getStreamGraphData().getStreamGraph().clear();
		String sameReport = "Closed.\nOk!";
        Log.outFileCompleteReport(sameReport);
		Log.outFileShortReport(sameReport);
		Log.consoleln("- The end.");
		Log.close();
	}
}

