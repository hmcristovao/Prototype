package myClasses;

public class Ranks {
	private NodesRank betweenness;
	private NodesRank closeness;
	private NodesRank eingenvector;
	
	public Ranks() {
		this.betweenness  = new NodesRank();
		this.closeness    = new NodesRank();
		this.eingenvector = new NodesRank();
	}

	public NodesRank getBetweenness() {
		return this.betweenness;
	}

	public NodesRank getCloseness() {
		return this.closeness;
	}

	public NodesRank getEingenvector() {
		return this.eingenvector;
	}
	
	public String toString() {
		return  this.getBetweenness().toString() +
				this.getCloseness().toString() +
				this.getEingenvector().toString();
	}

}
