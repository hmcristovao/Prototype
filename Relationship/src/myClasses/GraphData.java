package myClasses;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.IdAlreadyInUseException;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

public class GraphData {
	private Graph graph;

	public GraphData() {
		this.graph = new MultiGraph(Constants.nameGraph,
				                    true,                   // non-fatal error throws an exception
				                    false,                  // auto-create
				                    Constants.totalNodes,
				                    Constants.totalEdges);
	}
	
	public Graph getGraph() {
		return this.graph;
	}

	public boolean insertNode(String strNode) {
		try {
			Node node;
			node = this.graph.addNode(strNode);
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
			edge = this.graph.addEdge(strLink, strSourceNode, strTargetNode);
			edge.addAttribute("label", strLink);
			return true;
		}
		catch(IdAlreadyInUseException e) {
			return false;
		}
	}
	
	public String toString() {
		return "\ngraph = ";
	}
}
