package graph;


public class MeasuresRanks {
	private int connectedComponentNumber;  
	private GephiGraphData gephiGraphData; 
	private NodesTableArray basicTable; // without order, to be used in the building of the other tables 
	private NodesTableArray betweenness; 
	private NodesTableArray closeness;
	private NodesTableArray eingenvector;
	
	public MeasuresRanks(int number) {
		this.connectedComponentNumber = number;
		this.gephiGraphData           = new GephiGraphData();
		this.basicTable               = null; // will be fill before to sort the ranks
		this.betweenness              = null; // will be fill when happen the sorts to the ranks
		this.closeness                = null;
		this.eingenvector             = null;
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
	public NodesTableArray getEingenvector() {
		return this.eingenvector;
	}
	public void setEingenvector(NodesTableArray eingenvector) {
		this.eingenvector = eingenvector;
	}
	
	public String toString() {
		return  "Component connected: " + this.getConnectedComponentNumber() +
				"Betweenness: " + this.getBetweenness() +
				"Closeness: " + this.getCloseness() +
				"Eingenvector: " + this.getEingenvector();
	}
	
}
