// It is one raw of the table of nodes (NodesTableArray)
package myClasses;

public class NodeData {
	// permanent data
	private String strIdNode;
	private String shortName;
	private org.graphstream.graph.Node streamNode;
	private org.gephi.graph.api.Node gephiNode;
	private boolean original;

	// calculated data
	private int candidateLevel;
	private int partitioning;
	private double betweenness;
	private double closeness;
	private double eingenvector;
	
	// constructor to permanent data
	public NodeData(String strIdNode, 
					String shortName, 
					org.graphstream.graph.Node streamNode, 
					org.gephi.graph.api.Node gephiNode,
					boolean original) {
		this.strIdNode      = strIdNode;
		this.shortName      = shortName;
		this.streamNode     = streamNode;
		this.gephiNode      = gephiNode;
		this.original       = original;
	}
	public NodeData(String strIdNode, 
					String shortName, 
					StreamGraphData streamGraphData, 
					GephiGraphData gephiGraphData, 
					boolean original) {
		this(strIdNode, shortName, 
			 streamGraphData.getStreamGraph().getNode(strIdNode), 
			 gephiGraphData.getGephiGraph().getNode(strIdNode), 
			 original);
	}
	public String getStrIdNode() {
		return this.strIdNode;
	}
	public String getShortName() {
		return this.shortName;
	}
	public org.graphstream.graph.Node getStreamNode() {
		return this.streamNode;
	}
	public org.gephi.graph.api.Node getGephiNode() {
		return this.gephiNode;
	}
	public boolean isOriginal() {
		return this.original;
	}

	public int getCandidateLevel() {
		return this.candidateLevel;
	}
	public void setCandidateLevel(int candidateLevel) {
		this.candidateLevel = candidateLevel;
	}
	public int getPartitioning() {
		return this.partitioning;
	}
	public void setPartitioning(int partitioning) {
		this.partitioning = partitioning;
	}
	public double getBetweenness() {
		return this.betweenness;
	}
	public void setBetweenness(double betweenness) {
		this.betweenness = betweenness;
	}
	public double getCloseness() {
		return this.closeness;
	}
	public void setCloseness(double closeness) {
		this.closeness = closeness;
	}
	public double getEingenvector() {
		return this.eingenvector;
	}
	public void setEingenvector(double eingenvector) {
		this.eingenvector = eingenvector;
	}

	public int compareTo(NodeData nodeData) {
		return this.strIdNode.compareTo(nodeData.strIdNode);
	}
	
	@Override
	public String toString() {
		return  "Id: " + this.getStrIdNode() +
				"\nShort name: " + this.getShortName() +
				"\nOriginal: " + this.isOriginal() +
				"\nCandidate level: " + this.getCandidateLevel() +
				"\nPartitioning: " + this.getPartitioning() +
				"\nBetweenness: " + this.getBetweenness() +
				"\nCloseness: " + this.getCloseness() +
				"\nEingenvector: " + this.getEingenvector();
	}
}
