package main;

import graph.*;
import rdf.*;
import user.Concept;
import user.GroupConcept;
import map.*;

import java.util.LinkedList;

public class WholeSystem {
	private static GroupConcept conceptsRegister;
	private static EdgesTableHash edgesTable;
	private LinkedList<SetQuerySparql> listSetQuerySparql;
	private LinkedList<SystemGraphData> listSystemGraphData;
	private static StreamGraphData streamGraphData;  // It manages the Gephi graph visualization, just in time.  Only one to store all iterations.
	private static int goalConceptsQuantity;
	private static int maxConceptsQuantity;
	private ConceptMap conceptMap;
	
	public WholeSystem() {
		WholeSystem.conceptsRegister = new GroupConcept(); 
		WholeSystem.edgesTable       = new EdgesTableHash(); 
		this.listSetQuerySparql      = new LinkedList<SetQuerySparql>();
		this.listSystemGraphData     = new LinkedList<SystemGraphData>();
		WholeSystem.streamGraphData  = new StreamGraphData();
		this.conceptMap              = new ConceptMap();
	}

	// goalConcepts = log2(1 / #original_concepts) * 2 + factor) + #original_concepts
	public static void initGoalMaxConceptsQuantity() {
		int originalConceptsQuantity = WholeSystem.conceptsRegister.getQuantityOriginalConcept();
		WholeSystem.goalConceptsQuantity = (int)( ( Math.log(1.0/(double)originalConceptsQuantity)/Math.log(2.0) )
				                           * 2.0 + Config.conceptsQuantityCalulationFactor) + originalConceptsQuantity;
		WholeSystem.maxConceptsQuantity = WholeSystem.goalConceptsQuantity + Config.conceptsMinMaxRange;
	}
	public static StreamGraphData getStreamGraphData() {
		return WholeSystem.streamGraphData;
	}
	public static GroupConcept getConceptsRegister() {
		return WholeSystem.conceptsRegister;
	}
	public static EdgesTableHash getEdgesTable() {
		return WholeSystem.edgesTable;
	}

	public LinkedList<SetQuerySparql> getListSetQuerySparql() {
		return this.listSetQuerySparql;
	}
	public LinkedList<SystemGraphData> getListSystemGraphData() {
		return this.listSystemGraphData;
	}
	public ConceptMap getConceptMap() {
		return this.conceptMap;
	}
	public static int getGoalConceptsQuantity() {
		return WholeSystem.goalConceptsQuantity; 
	}
	public static int getMaxConceptsQuantity() {
		return WholeSystem.maxConceptsQuantity; 
	}
	
	public void insertListSetQuerySparql(SetQuerySparql setQuerySparql) {
		this.listSetQuerySparql.add(setQuerySparql);
	}
	public void insertListSystemGraphData(SystemGraphData systemGraphData) {
		this.listSystemGraphData.add(systemGraphData);
	}
}
