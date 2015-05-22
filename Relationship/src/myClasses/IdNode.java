package myClasses;

public class IdNode {
	private String strIdNode;
	private org.graphstream.graph.Node streamNode;
	private org.gephi.graph.api.Node gephiNode;
	
	public IdNode(String strIdNode, org.graphstream.graph.Node streamNode, org.gephi.graph.api.Node gephiNode) {
		this.strIdNode  = strIdNode;
		this.streamNode = streamNode;
		this.gephiNode  = gephiNode;
	}

	public IdNode(String strIdNode, StreamGraphData streamGraphData, GephiGraphData gephiGraphData) {
		this.strIdNode  = strIdNode;
		org.graphstream.graph.Node streamNode = streamGraphData.getStreamGraph().getNode(strIdNode);
		org.gephi.graph.api.Node gephiNode    = gephiGraphData.getGephiGraph().getNode(strIdNode);
		this.streamNode = streamNode;
		this.gephiNode  = gephiNode;
	}

	public String getStrIdNode() {
		return this.strIdNode;
	}

	public org.graphstream.graph.Node getStreamNode() {
		return this.streamNode;
	}

	public org.gephi.graph.api.Node getGephiNode() {
		return this.gephiNode;
	}

	public String toString() {
		return this.getStrIdNode();
	}	
}
