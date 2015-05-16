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
	
	public QuantityNodesEdges insertRDF(OneRDF oneRDF, QuantityNodesEdges quantityNodesEdges) { 
		// split elements of RDF:
		ItemRDF subjectRDF   = oneRDF.getSubject() ;
		ItemRDF predicateRDF = oneRDF.getPredicate();
		ItemRDF objectRDF    = oneRDF.getObject();
		Node node = null;
		Edge edge = null;
		try {
			node = this.graph.addNode(subjectRDF.getValue());
			quantityNodesEdges.incNumNodes();
			if(Constants.nodeLabel)
				node.addAttribute("label", subjectRDF.getShortName());
			// if main node, put label
			if(((NodeRDF)subjectRDF).getLevel() == Constants.Level.originalConcept) { 
				node.addAttribute("label", Concept.underlineToBlank(subjectRDF.getShortName()));
			}		
		}
		catch(IdAlreadyInUseException e) {
			// repeated node, do nothing
			node = this.graph.getNode(subjectRDF.getValue());
		}
		// if predicate is known, transform it in attributes into node
		if(predicateRDF.getValue().equals(Constants.addressBasic + "homepage"))
			node.addAttribute("homepage", objectRDF.getValue());
		else if(predicateRDF.getValue().equals(Constants.addressBasic + "comment"))
			node.addAttribute("comment", objectRDF.getValue());
		else if(predicateRDF.getValue().equals(Constants.addressBasic + "abstract"))
			node.addAttribute("abstract", objectRDF.getValue());
		else if(predicateRDF.getValue().equals(Constants.addressBasic + "image"))
			node.addAttribute("image", objectRDF.getValue());
        // insert common predicate (unknown)
		else {
			try {
				node = this.graph.addNode(objectRDF.getValue());
				quantityNodesEdges.incNumNodes();
				if(Constants.nodeLabel)
					node.addAttribute("label", objectRDF.getShortName());
				// if main node, put label 
				if(((NodeRDF)objectRDF).getLevel() == Constants.Level.originalConcept) { 
					node.addAttribute("label", Concept.underlineToBlank(objectRDF.getShortName()));
				}
			}
			catch(IdAlreadyInUseException e) {
				// repeated node, do nothing
				node = this.graph.getNode(objectRDF.getValue());
			}
			try {
				edge = this.graph.addEdge(predicateRDF.getValue(), subjectRDF.getValue(), objectRDF.getValue(),true);
				quantityNodesEdges.incNumEdges();
				if(Constants.edgeLabel)
					edge.addAttribute("label", predicateRDF.getShortName());
			}
			catch(IdAlreadyInUseException e) {
				// repeated edge, insert a different element in the identifying string and try again
				GraphData.incModifier();
				edge = this.graph.addEdge(predicateRDF.getValue()+" - "+GraphData.getStrModifier(), subjectRDF.getValue(), objectRDF.getValue(),true);
				quantityNodesEdges.incNumEdges();
				if(Constants.edgeLabel)
					// add modifier to differentiate each link into the graph
					edge.addAttribute("label", predicateRDF.getShortName()+" - "+GraphData.getStrModifier());
			}
		}
		return quantityNodesEdges;
	}
	
	public String toString() {
		return this.getGraph().toString();
	}
}
