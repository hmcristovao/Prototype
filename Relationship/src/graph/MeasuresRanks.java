package graph;


public class MeasuresRanks {
	private int connectedComponentNumber;  
	private GephiGraphData gephiGraphData; 
	private NodesTableArray basicTable; // without order, to be used in the building of the other tables
	private int originalQuantity;
	private NodesTableArray betweenness; 
	private NodesTableArray closeness;
	private NodesTableArray eigenvector;
	private NodesTableArray betweennessCloseness;
	
	public MeasuresRanks(int number) {
		this.connectedComponentNumber = number;
		this.gephiGraphData           = new GephiGraphData();
		this.basicTable               = null; // will be fill before to sort the ranks
		this.betweenness              = null; // will be fill when happen the sorts to the ranks
		this.closeness                = null;
		this.eigenvector              = null;
		this.betweennessCloseness     = null;
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

	public String toString() {
		return  "\n----------------- Sorted sub network ("+this.getConnectedComponentNumber()+
				") ---------------\nBetweenness: \n\n" + this.getBetweenness() +
				"\n----------------- Sorted sub network ("+this.getConnectedComponentNumber()+
				") ---------------\nCloseness: \n\n" + this.getCloseness() +
				"\n----------------- Sorted sub network ("+this.getConnectedComponentNumber()+
				") ---------------\nEigenvector: \n\n" + this.getEigenvector()+
				"\n----------------- Sorted sub network ("+this.getConnectedComponentNumber()+
				") ---------------\nBetweenness+Closeness: \n\n" + this.getBetweennessCloseness();
	}
	
}
