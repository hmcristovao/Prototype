package myClasses;
import org.graphstream.algorithm.BetweennessCentrality;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.IdAlreadyInUseException;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

public class GraphData {
	private Graph graph;
	private static int modifier = 0;
	private QuantityNodesEdges added;
    private QuantityNodesEdges duplicate;

	public GraphData() {
		this.graph = new MultiGraph(Constants.nameGraph,
				                    true,                   // non-fatal error throws an exception = verify duplicated nodes
				                    false,                  // auto-create
				                    Constants.totalNodes,
				                    Constants.totalEdges);
		this.added = new QuantityNodesEdges();
		this.duplicate = new QuantityNodesEdges();
	}
	
	public Graph getGraph() {
		return this.graph;
	}
	public int getTotalNodes() {
		return this.added.getNumNodes();
	}
	public void incTotalNodes() {
		this.added.incNumNodes();
	}
	public void incTotalNodes(int value) {
		this.added.incNumNodes(value);
	}

	public int getTotalEdges() {
		return this.added.getNumEdges();
	}
	public void incTotalEdges() {
		this.added.incNumEdges();
	}
	public void incTotalEdges(int value) {
		this.added.incNumEdges(value);
	}

	public int getTotalNodesDuplicate() {
		return this.duplicate.getNumNodes();
	}
	public void incTotalNodesDuplicate() {
		this.duplicate.incNumNodes();
	}
	public void incTotalNodesDuplicate(int value) {
		this.duplicate.incNumNodes(value);
	}

	public int getTotalEdgesDuplicate() {
		return this.duplicate.getNumEdges();
	}
	public void incTotalEdgesDuplicate() {
		this.duplicate.incNumEdges();
	}
	public void incTotalEdgesDuplicate(int value) {
		this.duplicate.incNumEdges(value);
	}

	public static String getStrModifier() {
		return String.valueOf(GraphData.modifier); 
	}
	
	public static void incModifier() {
		GraphData.modifier++;
	}
	public void buildGraph(SetQuerySparql setQuerySparql) {
		QuerySparql querySparql;
		ListRDF listRDF;
		OneRDF oneRDF;
		QuantityNodesEdges quantityNodesEdges = new QuantityNodesEdges();
		for(int i=0; i < setQuerySparql.getList().size(); i++) {
			querySparql = setQuerySparql.getList().get(i);
			listRDF = querySparql.getListRDF();
			for(int j=0; j < listRDF.size(); j++) {
				// get RDF elements
				oneRDF = listRDF.getList().get(j);
				// insert into of graph
				quantityNodesEdges.reset();
				this.insertRDF(oneRDF, quantityNodesEdges);
				this.incTotalNodes(quantityNodesEdges.getNumNodes());
				this.incTotalEdges(quantityNodesEdges.getNumEdges());
				this.incTotalNodesDuplicate(2 - quantityNodesEdges.getNumNodes());
				this.incTotalEdgesDuplicate(1 - quantityNodesEdges.getNumEdges());
			}
		}
	}
	public boolean insertNode(String strNode) {
		try {
			NodeData node;
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
		NodeData node = null;
		Edge edge = null;
		try {
			node = (NodeData)this.graph.addNode(subjectRDF.getValue());
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
			node = (NodeData)this.graph.getNode(subjectRDF.getValue());
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
				node = (NodeData)this.graph.addNode(objectRDF.getValue());
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
				node = (NodeData)this.graph.getNode(objectRDF.getValue());
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
	
	public void computeBetweennessCentrality() {
		BetweennessCentrality betweenness = new BetweennessCentrality();
		betweenness.setUnweighted();
		betweenness.setCentralityAttributeName("Betweeness");
		betweenness.init(this.getGraph());
		betweenness.compute();
	}
	
	public String toStringGraph() {
		StringBuffer str = new StringBuffer();
		Graph graph = this.getGraph();
		for( Node node : graph.getEachNode() ) {
			NodeData nodeData = (NodeData)node;
		    str.append(nodeData.toString());
		    str.append("\n");
		}
		return str.toString();
	}
	
	public String toString() {
		return  this.getGraph().toString() +
				"\ntotalNodes = " 			+ this.getTotalNodes() +
				"\ntotalEdges = " 			+ this.getTotalEdges() + 
				"\ntotalNodesDuplicate = " 	+ this.getTotalNodesDuplicate() +
				"\ntotalEdgesDuplicate = " 	+ this.getTotalEdgesDuplicate();
	}
}
