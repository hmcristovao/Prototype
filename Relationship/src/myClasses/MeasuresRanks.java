package myClasses;

public class MeasuresRanks {
	private int connectedComponentNumber;  // 0 correspond total network
	private NodesTable betweenness;
	private NodesTable closeness;
	private NodesTable eingenvector;
	
	public int getConnectedComponentNumber() {
		return this.connectedComponentNumber;
	}
	public void setConnectedComponentNumber(int connectedComponentNumber) {
		this.connectedComponentNumber = connectedComponentNumber;
	}
	public NodesTable getBetweenness() {
		return this.betweenness;
	}
	public void setBetweenness(NodesTable betweenness) {
		this.betweenness = betweenness;
	}
	public NodesTable getCloseness() {
		return this.closeness;
	}
	public void setCloseness(NodesTable closeness) {
		this.closeness = closeness;
	}
	public NodesTable getEingenvector() {
		return this.eingenvector;
	}
	public void setEingenvector(NodesTable eingenvector) {
		this.eingenvector = eingenvector;
	}
	public String toString() {
		return  "Component connected: " + this.getConnectedComponentNumber() +
				"Betweenness: " + this.getBetweenness() +
				"Closeness: " + this.getCloseness() +
				"Eingenvector: " + this.getEingenvector();
	}
	
}
