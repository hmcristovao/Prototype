package myClasses;

public class MeasuresRanks {
	private int connectedComponentNumber;  // 0 correspond total network
	private NodesTableArray betweenness;
	private NodesTableArray closeness;
	private NodesTableArray eingenvector;
	
	public int getConnectedComponentNumber() {
		return this.connectedComponentNumber;
	}
	public void setConnectedComponentNumber(int connectedComponentNumber) {
		this.connectedComponentNumber = connectedComponentNumber;
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
