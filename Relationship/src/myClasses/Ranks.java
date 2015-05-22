package myClasses;

public class Ranks {
	private RankNodes betweenness;
	private RankNodes closeness;
	private RankNodes eingenvector;
	
	public Ranks() {
		this.betweenness  = new RankNodes();
		this.closeness    = new RankNodes();
		this.eingenvector = new RankNodes();
	}

	public RankNodes getBetweenness() {
		return this.betweenness;
	}

	public RankNodes getCloseness() {
		return this.closeness;
	}

	public RankNodes getEingenvector() {
		return this.eingenvector;
	}
	
	public String toString() {
		return  this.getBetweenness().toString() +
				this.getCloseness().toString() +
				this.getEingenvector().toString();
	}

}
