package myClasses;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.IdAlreadyInUseException;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

public class GraphData {
	private Graph graph;
	private static int modifier = 0;

	public GraphData() {
		this.graph = new MultiGraph(Constants.nameGraph,
				                    true,                   // non-fatal error throws an exception = verify duplicated nodes
				                    false,                  // auto-create
				                    Constants.totalNodes,
				                    Constants.totalEdges);
	}
	
	public Graph getGraph() {
		return this.graph;
	}
	
	public static String getStrModifier() {
		return String.valueOf(GraphData.modifier); 
	}
	
	public static void incModifier() {
		GraphData.modifier++;
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
	
	public NumAdded insertRDF(ItemRDF itemRDF) { 
		// split elements of RDF:
		SubjectRDF subjectRDF     = itemRDF.getSubject() ;
		PredicateRDF predicateRDF = itemRDF.getPredicate();
		ObjectRDF objectRDF       = itemRDF.getObject();
		Node node = null;
		Edge edge = null;
		NumAdded numAdded = new NumAdded();
		try {
			node = this.graph.addNode(subjectRDF.getValue());
			Debug.DEBUG("inserted node:", subjectRDF.getValue());
			numAdded.numNodes++;
			node.addAttribute("label", subjectRDF.getValue());
		}
		catch(IdAlreadyInUseException e) {
			// repeated node, do nothing
			node = this.graph.getNode(subjectRDF.getValue());
			Debug.DEBUG("not inserted node:", subjectRDF.getValue());
		}
		if(predicateRDF.getValue().equals("http://relationship.org/homepage"))
			node.addAttribute("homepage", objectRDF.getValue());
		else {
			try {
				node = this.graph.addNode(objectRDF.getValue());
				numAdded.numNodes++;
				Debug.DEBUG("inserted node:", objectRDF.getValue());
				node.addAttribute("label", objectRDF.getValue());
			}
			catch(IdAlreadyInUseException e) {
				// repeated node, do nothing
				node = this.graph.getNode(objectRDF.getValue());
				Debug.DEBUG("not inserted node:", objectRDF.getValue());
			}
			try {
				edge = this.graph.addEdge(predicateRDF.getValue(), subjectRDF.getValue(), objectRDF.getValue(),true);
				numAdded.numEdges++;
				edge.addAttribute("label", predicateRDF.getValue());
				Debug.DEBUG("inserted edge:", predicateRDF.getValue()+" - "+subjectRDF.getValue()+" - "+objectRDF.getValue());
			}
			catch(IdAlreadyInUseException e) {
				// repeated edge, insert a different element in the identifying string and try again
				GraphData.incModifier();
				edge = this.graph.addEdge(predicateRDF.getValue()+" - "+GraphData.getStrModifier(), subjectRDF.getValue(), objectRDF.getValue(),true);
				numAdded.numEdges++;
				edge.addAttribute("label", predicateRDF.getValue()+" - "+GraphData.getStrModifier());
				Debug.DEBUG("inserted edge (changed):", predicateRDF.getValue()+" - "+GraphData.getStrModifier()+" - "+subjectRDF.getValue()+" - "+objectRDF.getValue());
			}
		}
		return numAdded;
	}
	
	public String toString() {
		return "\ngraph = ...";
	}
}
