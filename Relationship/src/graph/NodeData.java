// It is one raw of the table of nodes (NodesTableArray)
package graph;


public class NodeData {
	// basic data
	private String strIdNode;
	private String shortName;
	private org.graphstream.graph.Node streamNode;
	private org.gephi.graph.api.Node gephiNode;
	private boolean original;

	// extra attributes
	private String homepageAttribute;
	private String abstractAttribute;
	private String commentAttribute;
	private String imageAttribute;
		
	// calculated data
	private int connectedComponent; // 0 indicate the whole network
	private double betweenness;
	private double closeness;
	private double eigenvector;
	private int candidateLevel;
	private int partitioning;
	
	// constructor to permanent data
	public NodeData(String strIdNode, 
					String shortName, 
					org.graphstream.graph.Node streamNode, 
					org.gephi.graph.api.Node gephiNode,
					boolean original) {
		this.strIdNode         = strIdNode;
		this.shortName         = shortName;
		this.streamNode        = streamNode;
		this.gephiNode         = gephiNode;
		this.original          = original;
		this.homepageAttribute = null;
		this.abstractAttribute = null;
		this.commentAttribute  = null;
		this.imageAttribute    = null;
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

	public String getHomepageAttribute() {
		return this.homepageAttribute;
	}
	public void setHomepageAttribute(String homepageAttribute) {
		this.homepageAttribute = homepageAttribute;
	}
	public String getAbstractAttribute() {
		return this.abstractAttribute;
	}
	public void setAbstractAttribute(String abstractAttribute) {
		this.abstractAttribute = abstractAttribute;
	}
	public String getCommentAttribute() {
		return this.commentAttribute;
	}
	public void setCommentAttribute(String commentAttribute) {
		this.commentAttribute = commentAttribute;
	}
	public String getImageAttribute() {
		return this.imageAttribute;
	}
	public void setImageAttribute(String imageAttribute) {
		this.imageAttribute = imageAttribute;
	}

	public double connectedComponent() {
		return this.connectedComponent;
	}
	public void setConnectedComponent(int connectedComponent) {
		this.connectedComponent = connectedComponent;
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
	public double getEigenvector() {
		return this.eigenvector;
	}
	public void setEigenvector(double eigenvector) {
		this.eigenvector = eigenvector;
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

	public int compareTo(NodeData nodeData) {
		return this.strIdNode.compareTo(nodeData.strIdNode);
	}
	
	@Override
	public String toString() {
		return  "Id: " + this.getStrIdNode() +
				"\nShort name: " + this.getShortName() +
				"\nOriginal: " + this.isOriginal() +
				"\nConnected component: " + this.connectedComponent +
				"\nBetweenness: " + this.getBetweenness() +
				"\nCloseness: " + this.getCloseness() +
				"\nEigenvector: " + this.getEigenvector() +
				"\nCandidate level: " + this.getCandidateLevel() +
				"\nPartitioning: " + this.getPartitioning();
	}
}
