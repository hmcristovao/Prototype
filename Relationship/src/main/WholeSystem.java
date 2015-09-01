package main;

import graph.*;
import rdf.*;
import user.Concept;
import user.ConceptsGroup;
import map.*;

import java.util.LinkedList;

public class WholeSystem {
	private static UselessConceptsTable uselessConceptsTable = new UselessConceptsTable();
	private static StreamGraphData streamGraphData = new StreamGraphData();  // It manages the Gephi graph visualization, just in time.  Only one to store all iterations.
	private static ConceptsGroup conceptsRegister = new ConceptsGroup();
	private static int quantityOriginalConcepts;  // it will be filled from method MainProcess.parseTerms()
	private static EdgesTableHash edgesTable = new EdgesTableHash(); 
	private static LinkedList<SetQuerySparql> listSetQuerySparql = new LinkedList<SetQuerySparql>();
	private static LinkedList<SystemGraphData> listSystemGraphData = new LinkedList<SystemGraphData>();;
	private static int goalConceptsQuantity; // it will be calculated by WholeSystem.initGoalmaxConceptsQualtity() from MainProcess.parseTerms()
	private static int maxConceptsQuantity;  // it will be calculated by WholeSystem.initGoalmaxConceptsQualtity() from MainProcess.parseTerms()
	private static NodesTableArray sortEccentricityAndAverageSelectedConcepts;  // it will be filled at algorithm final fase 
	private static NodesTableArray sortEccentricityAndAverageRemainingConcepts; // it will be filled at algorithm final fase 
	private static VocabularyTable vocabularyTable = new VocabularyTable();
	private static ConceptMap conceptMap = new ConceptMap();;
	
	public static void initQuantityOriginalConcepts(int quantity) {
		WholeSystem.quantityOriginalConcepts = quantity;
	}
	public static int getQuantityOriginalConcepts() {
		return WholeSystem.quantityOriginalConcepts;
	}
	
	// goalConcepts = log2(1 / #original_concepts) * 2 + factor) + #original_concepts
	public static void initGoalMaxConceptsQuantity() {
		int originalConceptsQuantity = WholeSystem.getQuantityOriginalConcepts();
		WholeSystem.goalConceptsQuantity = (int)( ( Math.log(1.0/(double)originalConceptsQuantity)/Math.log(2.0) )
				                           * 2.0 + Config.conceptsQuantityCalulationFactor) + originalConceptsQuantity;
		WholeSystem.maxConceptsQuantity = WholeSystem.goalConceptsQuantity + Config.conceptsMinMaxRange;
	}
	public static StreamGraphData getStreamGraphData() {
		return WholeSystem.streamGraphData;
	}
	public static ConceptsGroup getConceptsRegister() {
		return WholeSystem.conceptsRegister;
	}
	public static EdgesTableHash getEdgesTable() {
		return WholeSystem.edgesTable;
	}

	public static LinkedList<SetQuerySparql> getListSetQuerySparql() {
		return WholeSystem.listSetQuerySparql;
	}
	public static LinkedList<SystemGraphData> getListSystemGraphData() {
		return WholeSystem.listSystemGraphData;
	}
	public static ConceptMap getConceptMap() {
		return WholeSystem.conceptMap;
	}
	public static VocabularyTable getVocabularyTable() {
		return WholeSystem.vocabularyTable;
	}
	public static UselessConceptsTable getUselessConceptsTable() {
		return WholeSystem.uselessConceptsTable;
	}
	public static int getGoalConceptsQuantity() {
		return WholeSystem.goalConceptsQuantity; 
	}
	public static int getMaxConceptsQuantity() {
		return WholeSystem.maxConceptsQuantity; 
	}
	public static NodesTableArray getSortEccentricityAndAverageSelectedConcepts() {
		return WholeSystem.sortEccentricityAndAverageSelectedConcepts;
	}
	public static void setSortEccentricityAndAverageSelectedConcepts(NodesTableArray nodesTableArray) {
		WholeSystem.sortEccentricityAndAverageSelectedConcepts = nodesTableArray;
	}
	public static NodesTableArray getSortEccentricityAndAverageRemainingConcepts() {
		return WholeSystem.sortEccentricityAndAverageRemainingConcepts;
	}
	public static void setSortEccentricityAndAverageRemainingConcepts(NodesTableArray nodesTableArray) {
		WholeSystem.sortEccentricityAndAverageRemainingConcepts = nodesTableArray;
	}
	public static void insertListSetQuerySparql(SetQuerySparql setQuerySparql) {
		WholeSystem.listSetQuerySparql.add(setQuerySparql);
	}
	public static void insertListSystemGraphData(SystemGraphData systemGraphData) {
		WholeSystem.listSystemGraphData.add(systemGraphData);
	}
}
