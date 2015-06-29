 package graph;
import main.Config;
import main.Log;
import main.WholeSystem;

import org.graphstream.algorithm.BetweennessCentrality;
import org.graphstream.algorithm.measure.ClosenessCentrality;
import org.graphstream.algorithm.measure.EigenvectorCentrality;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.IdAlreadyInUseException;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import rdf.ItemRDF;
import rdf.ListRDF;
import rdf.NodeRDF;
import rdf.OneRDF;
import rdf.QuerySparql;
import rdf.SetQuerySparql;
import user.Concept;
import user.GroupConcept;

public class StreamGraphData {
	private Graph streamGraph;
	private static int modifier = 0;
	private QuantityNodesEdges added;
    private QuantityNodesEdges duplicate;
		

	public StreamGraphData() {
		this.streamGraph = new MultiGraph(
									Config.nameGraph,
				                    true,                   // non-fatal error throws an exception = verify duplicated nodes
				                    false,                  // auto-create
				                    Config.totalNodes,
				                    Config.totalEdges
				                         );
		this.added     = new QuantityNodesEdges();
		this.duplicate = new QuantityNodesEdges();
	}
	
	public Graph getStreamGraph() {
		return this.streamGraph;
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
		return String.valueOf(StreamGraphData.modifier); 
	}
	
	public static void incModifier() {
		StreamGraphData.modifier++;
	}
	
	public QuantityNodesEdges buildStreamGraphData(SetQuerySparql setQuerySparql) {
		QuerySparql querySparql;
		ListRDF listRDF;
		OneRDF oneRDF;
		QuantityNodesEdges quantityNodesEdgesOut = new QuantityNodesEdges();
		QuantityNodesEdges quantityNodesEdges    = new QuantityNodesEdges();
		for(int i=0; i < setQuerySparql.getListQuerySparql().size(); i++) {
			querySparql = setQuerySparql.getListQuerySparql().get(i);
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
				quantityNodesEdgesOut.incNumNodes(quantityNodesEdges.getNumNodes());
				quantityNodesEdgesOut.incNumEdges(quantityNodesEdges.getNumEdges());
			}
		}
		return quantityNodesEdgesOut;
	}

	private void insertRDF(OneRDF oneRDF, QuantityNodesEdges quantityNodesEdges) { 
		// split elements of RDF:
		ItemRDF subjectRDF   = oneRDF.getSubject() ;
		ItemRDF predicateRDF = oneRDF.getPredicate();
		ItemRDF objectRDF    = oneRDF.getObject();
		Node node = null;
		Edge edge = null;
		try {
			node = this.streamGraph.addNode(subjectRDF.getLongName());
			quantityNodesEdges.incNumNodes();
			node.addAttribute("shortunderlinename", subjectRDF.getShortUnderlineName());
			node.addAttribute("shortblankname",     subjectRDF.getShortBlankName());
			if(Config.nodeLabelStreamGephi)
				node.addAttribute("label", subjectRDF.getShortBlankName());
			// if original concept then put label
			if(WholeSystem.getConceptsRegister().isOriginalConcept(subjectRDF.getShortBlankName())) { 
				node.addAttribute("label", subjectRDF.getShortBlankName());
			}		
		}
		catch(IdAlreadyInUseException e) {
			// repeated node, do nothing
			node = this.streamGraph.getNode(subjectRDF.getLongName());
		}
		// if predicate is known, transform it in attributes into node
		if(predicateRDF.getLongName().equals(Config.addressBasic + "homepage"))
			node.addAttribute("homepage", objectRDF.getLongName());
		else if(predicateRDF.getLongName().equals(Config.addressBasic + "comment"))
			node.addAttribute("comment", objectRDF.getLongName());
		else if(predicateRDF.getLongName().equals(Config.addressBasic + "abstract"))
			node.addAttribute("abstract", objectRDF.getLongName());
		else if(predicateRDF.getLongName().equals(Config.addressBasic + "image"))
			node.addAttribute("image", objectRDF.getLongName());
        // insert common predicate (unknown)
		else {
			try {
				node = this.streamGraph.addNode(objectRDF.getLongName());
				quantityNodesEdges.incNumNodes();
				node.addAttribute("shortunderlinename", objectRDF.getShortUnderlineName());
				node.addAttribute("shortblankname",     objectRDF.getShortBlankName());
				if(Config.nodeLabelStreamGephi)
					node.addAttribute("label", objectRDF.getShortBlankName());
				// if original concept then put label
				if(WholeSystem.getConceptsRegister().isOriginalConcept(objectRDF.getShortBlankName())) { 
					node.addAttribute("label", objectRDF.getShortBlankName());
				}
			}
			catch(IdAlreadyInUseException e) {
				// repeated node, do nothing
				node = this.streamGraph.getNode(objectRDF.getLongName());
			}
			try {
				edge = this.streamGraph.addEdge(predicateRDF.getLongName(), subjectRDF.getLongName(), objectRDF.getLongName(),true);
				edge.addAttribute("shortunderlinename", predicateRDF.getShortUnderlineName());
				edge.addAttribute("shortblankname",     predicateRDF.getShortBlankName());
				quantityNodesEdges.incNumEdges();
				if(Config.edgeLabelStreamGephi)
					edge.addAttribute("label", predicateRDF.getShortBlankName());
			}
			catch(IdAlreadyInUseException e) {
				// repeated edge, insert a different element in the identifying string and try again
				StreamGraphData.incModifier();
				edge = this.streamGraph.addEdge(predicateRDF.getLongName()+" - "+StreamGraphData.getStrModifier(), subjectRDF.getLongName(), objectRDF.getLongName(),true);
				quantityNodesEdges.incNumEdges();
				if(Config.edgeLabelStreamGephi)
					// add modifier to differentiate each link into the graph
					edge.addAttribute("label", predicateRDF.getShortBlankName()+" - "+StreamGraphData.getStrModifier());
			}
		}
	}
	
	// don't used (it was used to Gephi Tool Kit)
	public void computeBetweennessCentrality() {
		BetweennessCentrality betweenness = new BetweennessCentrality();
		betweenness.init(this.getStreamGraph());
		//betweenness.setUnweighted();
		betweenness.setCentralityAttributeName("betweenness");
		betweenness.compute();
	}
	// don't used (it was used to Gephi Tool Kit)
	public void computeClosenessCentrality() {
		ClosenessCentrality closeness = new ClosenessCentrality();
		closeness.init(this.getStreamGraph());
		closeness.setCentralityAttribute("closeness");
		closeness.compute();
	}
	// don't used (it was used to Gephi Tool Kit)
	public void computeEigenvectorCentrality() {
		EigenvectorCentrality eingenvector = new EigenvectorCentrality();
		eingenvector.init(this.getStreamGraph());
		eingenvector.setCentralityAttribute("eingenvector");
		eingenvector.compute();	
	}
	
	public void addNewConceptsLabel(GroupConcept newConcepts) {
		Node node;
		for(Concept concept : newConcepts.getList()) {
			node = this.streamGraph.getNode(concept.getFullName());
			// some nodes will not be found because they are from "Category"
			if(node != null) {
			   node.addAttribute("label", concept.getBlankName());  // the process enters here, but this act do not updade the graph visualization!!!
			   //node.changeAttribute("label", concept.getBlankName());  // it is also not working
			}
		}
	}
	
	public static String nodeToString(Node node) {
		StringBuffer str = new StringBuffer();
		str.append("\nID: ");
		str.append(node.toString());
		str.append(" - Short blank name: ");
		str.append(node.getAttribute("shortblankname"));
		str.append("\n[Degree: ");
		str.append(node.getDegree());
		str.append("] [In degree: ");
		str.append(node.getInDegree());
		str.append("] [OutDegree: ");
		str.append(node.getOutDegree());
		str.append("]");
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
		return str.toString();
	}
 
	
	public String toStringGraph() {
		StringBuffer str = new StringBuffer();
		Graph graph = this.getStreamGraph();
		str.append("\nGraph stream:\n");
		for( Node node : graph.getEachNode() ) {
			str.append(StreamGraphData.nodeToString(node));
			str.append("\n");
		}
		return str.toString();
	}
	
	public String toStringShort() {
		return  "\nGraph stream (resume):\n" +
				"\nTotal nodes (counted):  " + this.getTotalNodes() +
				"\nTotal nodes (real):     " + this.getStreamGraph().getNodeCount() +
				"\nTotal edges (counted):  " + this.getTotalEdges() + 
				"\nTotal duplicated nodes: " + this.getTotalNodesDuplicate() +
				"\nTotal duplicated edges: " + this.getTotalEdgesDuplicate();
	}
	
	public String toString() {
		return  this.toStringGraph() +
				Config.singleLine +
				this.toStringShort();
	}
}
