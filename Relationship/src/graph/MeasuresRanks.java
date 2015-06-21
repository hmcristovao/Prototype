package graph;

import main.Config;
import user.Concept;
import user.GroupConcept;

public class MeasuresRanks {
	private int connectedComponentNumber;  
	private GephiGraphData gephiGraphData; 
	private NodesTableArray basicTable; // without order, to be used in the building of the other tables
	private NodesTableArray betweenness; 
	private NodesTableArray closeness;
	private NodesTableArray eigenvector;
	private NodesTableArray betweennessCloseness;
	
	private GroupConcept originalConcepts;
	private GroupConcept currentConcepts;
	private GroupConcept betweennessClosenessConcepts;
	private GroupConcept eigenvectorConcepts;
	
	public MeasuresRanks(int number) {
		this.connectedComponentNumber     = number;
		this.gephiGraphData               = new GephiGraphData();
		this.basicTable                   = null; // will be fill before to sort the ranks
		this.betweenness                  = null; // will be fill when happen the sorts to the ranks
		this.closeness                    = null;
		this.eigenvector                  = null;
		this.betweennessCloseness     	  = null;
		this.originalConcepts             = new GroupConcept();
		this.currentConcepts              = new GroupConcept();
		this.betweennessClosenessConcepts = new GroupConcept();
		this.eigenvectorConcepts          = new GroupConcept();	
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
 	public void insertOriginalConcept(NodeData nodeData) { 
		Concept objectNewConcept = new Concept(nodeData.getShortName(),nodeData.getStatus());  
		this.originalConcepts.add(objectNewConcept);
	}
	public GroupConcept getListOriginalConcepts() {
		return this.originalConcepts;
	}

	public void insertCurrentConcepts(Concept concept) {
		this.currentConcepts.add(concept);
	}
 	public void insertCurrentConcept(NodeData nodeData) {
		Concept objectNewConcept = new Concept(nodeData.getShortName(),nodeData.getStatus());  
		this.currentConcepts.add(objectNewConcept);
	}
	public GroupConcept getListCurrentConcepts() {
		return this.currentConcepts;
	}

	public void insertBetweennessClosenessConcept(Concept concept) {
		this.betweennessClosenessConcepts.add(concept);
	}
	public void insertBetweennessClosenessConcept(NodeData nodeData) {
		Concept objectNewConcept = new Concept(nodeData.getShortName(),nodeData.getStatus());  
		this.betweennessClosenessConcepts.add(objectNewConcept);
	}
	public GroupConcept getListBetweennessClosenessConcept() {
		return this.betweennessClosenessConcepts;
	}

	public void insertEigenvectorConcept(Concept concept) {
		this.eigenvectorConcepts.add(concept);
	}
	public void insertEigenvectorConcept(NodeData nodeData) {
		Concept objectNewConcept = new Concept(nodeData.getShortName(),nodeData.getStatus());  
		this.eigenvectorConcepts.add(objectNewConcept);
	}
	public GroupConcept getListEigenvectorConcept() {
		return this.eigenvectorConcepts;
	}

	public String toStringShort(int connectedComponentNumber, int quantityNodes) {
		return  Config.doubleLine+"Table array (betweenness sorted) - Connected component number: "
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
		return  Config.doubleLine+"Table array (betweenness sorted) - Connected component number: "
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
