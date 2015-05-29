package myClasses;
import org.graphstream.algorithm.BetweennessCentrality;
import org.graphstream.algorithm.measure.ClosenessCentrality;
import org.graphstream.algorithm.measure.EigenvectorCentrality;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.IdAlreadyInUseException;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

public class StreamGraphData {
	private Graph streamGraph;


	public StreamGraphData() {
		this.streamGraph = new MultiGraph(Constants.nameGraph,
				                    true,                   // non-fatal error throws an exception = verify duplicated nodes
				                    false,                  // auto-create
				                    Constants.totalNodes,
				                    Constants.totalEdges);
	}
	
	public Graph getStreamGraph() {
		return this.streamGraph;
	}


	public boolean insertNode(String strNode) {
		try {
			NodeData node;
			node = this.streamGraph.addNode(strNode);
			node.addAttribute("label", strNode);
			return true;
		}
		catch(IdAlreadyInUseException e) {
			return false;
		}
	}
	
	public boolean insertEdge(String strLink, String strSourceNode, String strTargetNode) {
		try {
			Edge edge;
			edge = this.streamGraph.addEdge(strLink, strSourceNode, strTargetNode);
			edge.addAttribute("label", strLink);
			return true;
		}
		catch(IdAlreadyInUseException e) {
			return false;
		}
	}
	

	
	public void computeBetweennessCentrality() {
		BetweennessCentrality betweenness = new BetweennessCentrality();
		betweenness.init(this.getStreamGraph());
		//betweenness.setUnweighted();
		betweenness.setCentralityAttributeName("betweenness");
		betweenness.compute();
	}
	
	public void computeClosenessCentrality() {
		ClosenessCentrality closeness = new ClosenessCentrality();
		closeness.init(this.getStreamGraph());
		closeness.setCentralityAttribute("closeness");
		closeness.compute();
	}
	
	public void computeEigenvectorCentrality() {
		EigenvectorCentrality eingenvector = new EigenvectorCentrality();
		eingenvector.init(this.getStreamGraph());
		eingenvector.setCentralityAttribute("eingenvector");
		eingenvector.compute();	
	}
	
	public static String nodeToString(Node node) {
		StringBuffer str = new StringBuffer();
		str.append("\nID: ");
		str.append(node.toString());
		str.append("\nShort name: ");
		str.append(node.getAttribute("shortname"));
		str.append("\nDegree: ");
		str.append(node.getDegree());
		str.append("\nIn degree: ");
		str.append(node.getInDegree());
		str.append("\nOutDegree: ");
		str.append(node.getOutDegree());
		if(node.getAttribute("label") != null) {
			str.append("\nLabel: ");
			str.append(node.getAttribute("label"));
		}
		if(node.getAttribute("homepage") != null) {
			str.append("\nHomepage: ");
			str.append(node.getAttribute("homepage"));
		}
		if(node.getAttribute("abstract") != null) {
			str.append("\nAbstract: ");
			str.append(node.getAttribute("abstract"));
		}
		if(node.getAttribute("comment") != null) {
			str.append("\nComment: ");
			str.append(node.getAttribute("comment"));
		}
		if(node.getAttribute("image") != null) {
			str.append("\nImage: ");
			str.append(node.getAttribute("image"));
		}
		str.append("\nBetweenness centrality: ");
		str.append(node.getAttribute("betweenness"));
		str.append("\nCloseness centrality: ");
		str.append(node.getAttribute("closeness"));
		str.append("\nEingenvector centrality: ");
		str.append(node.getAttribute("eigenvector"));
		return str.toString();
	}
 
	
	public String toStringGraph() {
		StringBuffer str = new StringBuffer();
		Graph graph = this.getStreamGraph();
		for( Node node : graph.getEachNode() ) {
			str.append(StreamGraphData.nodeToString(node));
			str.append("\n");
		}
		return str.toString();
	}
	
	public String toString() {
		return  this.toStringGraph();
	}
}
