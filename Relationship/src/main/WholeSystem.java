package main;

import graph.*;
import rdf.*;
import user.Concept;
import user.ListConcept;
import map.*;

import java.util.LinkedList;

public class WholeSystem {
	private static ListConcept originalConcepts;
	private LinkedList<SetQuerySparql> listSetQuerySparql;
	private LinkedList<SystemGraphData> listSystemGraphData;
	private ConceptMap conceptMap;
	
	public WholeSystem() {
		WholeSystem.originalConcepts = new ListConcept(); 
		this.listSetQuerySparql      = new LinkedList<SetQuerySparql>();
		this.listSystemGraphData     = new LinkedList<SystemGraphData>();
		this.conceptMap              = new ConceptMap();
	}

	public static ListConcept getOriginalConcepts() {
		return WholeSystem.originalConcepts;
	}
	public static boolean isOriginalConcept(Concept concept) {
		for(Concept x : WholeSystem.originalConcepts.getList()) {
			if(x.equals(concept))
				return true;
		}
		return false;
	}
	
	public static boolean isOriginalConcept(String basicConcept) {
		for(Concept x : WholeSystem.originalConcepts.getList()) {
			if(x.getBasicConcept().equals(basicConcept))
				return true;
		}
		return false;
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

	public void insertListSetQuerySparql(SetQuerySparql setQuerySparql) {
		this.listSetQuerySparql.add(setQuerySparql);
	}
	public void insertListSystemGraphData(SystemGraphData systemGraphData) {
		this.listSystemGraphData.add(systemGraphData);
	}
}
