package main;

import graph.*;
import rdf.*;
import user.Concept;
import user.GroupConcept;
import map.*;

import java.util.LinkedList;

public class WholeSystem {
	private static GroupConcept conceptsRegister;
	private LinkedList<SetQuerySparql> listSetQuerySparql;
	private LinkedList<SystemGraphData> listSystemGraphData;
	private static StreamGraphData streamGraphData;  // It manages the Gephi graph visualization, just in time.  Only one to store all iterations.
	private ConceptMap conceptMap;
	
	public WholeSystem() {
		WholeSystem.conceptsRegister = new GroupConcept(); 
		this.listSetQuerySparql      = new LinkedList<SetQuerySparql>();
		this.listSystemGraphData     = new LinkedList<SystemGraphData>();
		WholeSystem.streamGraphData  = new StreamGraphData();
		this.conceptMap              = new ConceptMap();
	}

	public static StreamGraphData getStreamGraphData() {
		return WholeSystem.streamGraphData;
	}

	public static GroupConcept getConceptsRegister() {
		return WholeSystem.conceptsRegister;
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
