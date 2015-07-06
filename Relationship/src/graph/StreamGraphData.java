 package graph;
import java.util.LinkedList;

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
	private QuantityNodesEdges total;
    private QuantityNodesEdges duplicated;
    private QuantityNodesEdges deleted;
		

	public StreamGraphData() {
		this.streamGraph = new MultiGraph(
									Config.nameGraph,
				                    true,                   // non-fatal error throws an exception = verify duplicated nodes
				                    false,                  // auto-create
				                    Config.maxNodes,
				                    Config.minEdges
				                         );
		this.total      = new QuantityNodesEdges();
		this.duplicated = new QuantityNodesEdges();
		this.deleted    = new QuantityNodesEdges();
	}
	
	public Graph getStreamGraph() {
		return this.streamGraph;
	}
	
	
	// intern control of quantity of nodes and edges

	// total:
	public int getTotalNodes() {
		return this.total.getNumNodes();
	}
	public void incTotalNodes() {
		this.total.incNumNodes();
	}
	public void incTotalNodes(int value) {
		this.total.incNumNodes(value);
	}

	public int getTotalEdges() {
		return this.total.getNumEdges();
	}
	public void incTotalEdges() {
		this.total.incNumEdges();
	}
	public void incTotalEdges(int value) {
		this.total.incNumEdges(value);
	}

	// duplicated
	public int getTotalNodesDuplicate() {
		return this.duplicated.getNumNodes();
	}
	public void incTotalNodesDuplicate() {
		this.duplicated.incNumNodes();
	}
	public void incTotalNodesDuplicate(int value) {
		this.duplicated.incNumNodes(value);
	}

	public int getTotalEdgesDuplicate() {
		return this.duplicated.getNumEdges();
	}
	public void incTotalEdgesDuplicate() {
		this.duplicated.incNumEdges();
	}
	public void incTotalEdgesDuplicate(int value) {
		this.duplicated.incNumEdges(value);
	}

	// deleted
	public int getTotalNodesDeleted() {
		return this.deleted.getNumNodes();
	}
	public void incTotalNodesDeleted() {
		this.deleted.incNumNodes();
	}
	public void incTotalNodesDeleted(int value) {
		this.deleted.incNumNodes(value);
	}

	public int getTotalEdgesDeleted() {
		return this.deleted.getNumEdges();
	}
	public void incTotalEdgesDeleted() {
		this.deleted.incNumEdges();
	}
	public void incTotalEdgesDeleted(int value) {
		this.deleted.incNumEdges(value);
	}
	
	// actual and real quantity of nodes and edges
	public int getRealTotalNodes() {
		return this.streamGraph.getNodeCount();
	}
	public int getRealTotalEdges() {
		return this.streamGraph.getEdgeCount();
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
/*
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
*/
	// Apply K-core n on the Graph
	// return quantity of selected concepts deleted
	public int applyKcoreN() {
		int quantityDeletedSelectedConcepts = 0;
		for( Node node : this.streamGraph.getEachNode() ) {
			// if node has degree less than kcoreN ...
			if(node.getDegree() < Config.kCoreN) {
				String blankName = (String)node.getAttribute("shortblankname");
				// ...and it is not original node
				if(!WholeSystem.getConceptsRegister().isOriginalConcept(blankName)) {
					this.incTotalNodesDeleted();
					this.incTotalEdgesDeleted(node.getEdgeSet().size());
					this.incTotalNodes(-1);
					this.incTotalEdges(node.getEdgeSet().size());
					this.streamGraph.removeNode(node);
					// remove node of the concepts register, if is the case
					if(WholeSystem.getConceptsRegister().isConcept(blankName)) {
						WholeSystem.getConceptsRegister().removeConcept(blankName);
						quantityDeletedSelectedConcepts++;
					}
				}
			}	
		} 
		return quantityDeletedSelectedConcepts;
	}
	
	// Apply n-degree filter on the Graph
	// return quantity of selected concepts deleted
	public int applyNdegreeFilterTrigger() {
		LinkedList<Node> auxList = new LinkedList<Node>();
		// at first select the candidates nodes and put them in an auxiliary list
		for( Node node : this.streamGraph.getEachNode() ) {
			// if node has degree less than nDegreeFilter...
			if(node.getDegree() < Config.nDegreeFilter) {
				// ...and it is not original node, store this node
				if(!WholeSystem.getConceptsRegister().isOriginalConcept((String)node.getAttribute("shortblankname"))) {
					auxList.add(node);
				}
			}
		}
		// second: delete the selected nodes and their respectives edges
		int quantityDeletedSelectedConcepts = 0;
		for( Node node : auxList ) {
			this.incTotalNodesDeleted();
			this.incTotalEdgesDeleted(node.getEdgeSet().size());
			this.incTotalNodes(-1);
			this.incTotalEdges(node.getEdgeSet().size());
			// remove all edges linked with this node
			for( Edge edge : node.getEachEdge()) {
				this.streamGraph.removeEdge(edge);
			}
			// finally, remove the node of the Strem Graph
			this.streamGraph.removeNode(node);
			// remove node of the concepts register, if is the case
			if(WholeSystem.getConceptsRegister().isConcept((String)node.getAttribute("shortblankname"))) {
				WholeSystem.getConceptsRegister().removeConcept((String)node.getAttribute("shortblankname"));
				quantityDeletedSelectedConcepts++;
			}
		} 
		return quantityDeletedSelectedConcepts;
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
				"\nTotal nodes (real):     " + this.getRealTotalNodes() +
				"\nTotal edges (counted):  " + this.getTotalEdges() + 
				"\nTotal duplicated nodes: " + this.getTotalNodesDuplicate() +
				"\nTotal duplicated edges: " + this.getTotalEdgesDuplicate() +
				"\nTotal deleted nodes: "    + this.getTotalNodesDeleted() +
				"\nTotal deleted edges: "    + this.getTotalEdgesDeleted();
	}
	
	public String toString() {
		return  this.toStringGraph() +
				Config.singleLine +
				this.toStringShort();
	}
}
