package main;

import graph.*;
import rdf.*;
import map.*;
import java.util.LinkedList;

public class WholeSystem {
	private LinkedList<SetQuerySparql> listSetQuerySparql;
	private LinkedList<SystemGraphData> listSystemGraphData;
	private ConceptMap conceptMap;
	
	public WholeSystem() {
		this.listSetQuerySparql  = new LinkedList<SetQuerySparql>();
		this.listSystemGraphData = new LinkedList<SystemGraphData>();
		this.conceptMap          = new ConceptMap();
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
