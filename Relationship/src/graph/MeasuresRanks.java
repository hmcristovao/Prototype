package graph;

import java.util.LinkedList;

import main.Config;
import user.Concept;

public class MeasuresRanks {
	private int connectedComponentNumber;  
	private GephiGraphData gephiGraphData; 
	private NodesTableArray basicTable; // without order, to be used in the building of the other tables
	private int originalQuantity;
	private NodesTableArray betweenness; 
	private NodesTableArray closeness;
	private NodesTableArray eigenvector;
	private NodesTableArray betweennessCloseness;
	
	private LinkedList<Concept> originalConcepts;
	private LinkedList<Concept> betweennessClosenessConcepts;
	private LinkedList<Concept> eigenvectorConcepts;
	
	public MeasuresRanks(int number) {
		this.connectedComponentNumber     = number;
		this.gephiGraphData               = new GephiGraphData();
		this.basicTable                   = null; // will be fill before to sort the ranks
		this.betweenness                  = null; // will be fill when happen the sorts to the ranks
		this.closeness                    = null;
		this.eigenvector                  = null;
		this.betweennessCloseness     	  = null;
		this.originalConcepts             = new LinkedList<Concept>();
		this.betweennessClosenessConcepts = new LinkedList<Concept>();
		this.eigenvectorConcepts          = new LinkedList<Concept>();	
	}
	
	public int getConnectedComponentNumber() {
		return this.connectedComponentNumber;
	}
	public void setConnectedComponentNumber(int connectedComponentNumber) {
		this.connectedComponentNumber = connectedComponentNumber;
	}
	public GephiGraphData getGephiGraphData() {
		return this.gephiGraphData;
	}
	public void setGephiGraphData(GephiGraphData gephiGraphData) {
		this.gephiGraphData = gephiGraphData;
	}
	public NodesTableArray getBasicTable() {
		return this.basicTable;
	}
	public void setBasicTable(NodesTableArray basicTable) {
		this.basicTable = basicTable;
	}
	public int getOriginalQuantity() {
		return this.originalQuantity;
	}
	public void setOriginalQuantity(int originalQuantity) {
		this.originalQuantity = originalQuantity;
	}
	public NodesTableArray getBetweenness() {
		return this.betweenness;
	}
	public void setBetweenness(NodesTableArray betweenness) {
		this.betweenness = betweenness;
	}
	public NodesTableArray getCloseness() {
		return this.closeness;
	}
	public void setCloseness(NodesTableArray closeness) {
		this.closeness = closeness;
	}
	public NodesTableArray getEigenvector() {
		return this.eigenvector;
	}
	public void setEigenvector(NodesTableArray eigenvector) {
		this.eigenvector = eigenvector;
	}
	public NodesTableArray getBetweennessCloseness() {
		return this.betweennessCloseness;
	}
	public void setBetweennessCloseness(NodesTableArray betweennessCloseness) {
		this.betweennessCloseness = betweennessCloseness;
	}

	public void insertOriginalConcepts(Concept concept) {
		this.originalConcepts.add(concept);
	}
 	public void insertOriginalConcept(String stringNewConcept) { 
		Concept objectNewConcept = new Concept(stringNewConcept, false);  // false: not cool
		this.originalConcepts.add(objectNewConcept);
	}
	public LinkedList<Concept> getListOriginalConcepts() {
		return this.originalConcepts;
	}

	public void insertBetweennessClosenessConcept(Concept concept) {
		this.betweennessClosenessConcepts.add(concept);
	}
	public void insertBetweennessClosenessConcept(String stringNewConcept) {
		Concept objectNewConcept = new Concept(stringNewConcept, false);   // false: not cool
		this.betweennessClosenessConcepts.add(objectNewConcept);
	}
	public LinkedList<Concept> getListBetweennessClosenessConcept() {
		return this.betweennessClosenessConcepts;
	}

	public void insertEigenvectorConcept(Concept concept) {
		this.eigenvectorConcepts.add(concept);
	}
	public void insertEigenvectorConcept(String stringNewConcept) {
		Concept objectNewConcept = new Concept(stringNewConcept, false);   // false: not cool
		this.eigenvectorConcepts.add(objectNewConcept);
	}
	public LinkedList<Concept> getListEigenvectorConcept() {
		return this.eigenvectorConcepts;
	}

	public String toStringShort(int connectedComponentNumber, int quantityNodes) {
		return  "\n"+Config.doubleLine+"Table array (betweenness sorted) - Connected component number: "
				+ connectedComponentNumber + " (only the first "+quantityNodes+" nodes)"+Config.singleLine  
				+ this.getBetweenness().toStringShort(quantityNodes) 
		        + "\n"+Config.doubleLine+"Table array (closeness sorted) - Connected component number: "
		        + connectedComponentNumber + " (only the first "+quantityNodes+" nodes)"+Config.singleLine  
		        + this.getCloseness().toStringShort(quantityNodes) 
		        + "\n"+Config.doubleLine+"Table array (eingenvector sorted) - Connected component number: "
		        + connectedComponentNumber + " (only the first "+quantityNodes+" nodes)"+Config.singleLine  
		        + this.getEigenvector().toStringShort(quantityNodes)		
				+ "\n"+Config.doubleLine+"Table array (betweenness+closeness sorted) - Connected component number: "
		        + connectedComponentNumber + " (only the first "+quantityNodes+" nodes)"+Config.singleLine  
		        + this.getBetweennessCloseness().toStringShort(quantityNodes);		
	}

	public String toString(int connectedComponentNumber) {
		return  "\n"+Config.doubleLine+"Table array (betweenness sorted) - Connected component number: "
				+ connectedComponentNumber + Config.singleLine  
				+ this.getBetweenness().toString() 
		        + "\n"+Config.doubleLine+"Table array (closeness sorted) - Connected component number: "
		        + connectedComponentNumber + Config.singleLine  
		        + this.getCloseness().toString() 
		        + "\n"+Config.doubleLine+"Table array (eingenvector sorted) - Connected component number: "
		        + connectedComponentNumber + Config.singleLine  
		        + this.getEigenvector().toString()		
				+ "\n"+Config.doubleLine+"Table array (betweenness+closeness sorted) - Connected component number: "
		        + connectedComponentNumber + Config.singleLine  
		        + this.getBetweennessCloseness().toString();		
	}
}
