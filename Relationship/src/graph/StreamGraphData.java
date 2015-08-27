package graph;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import main.Config;
import main.Log;
import main.WholeSystem;

import org.graphstream.algorithm.BetweennessCentrality;
import org.graphstream.algorithm.measure.ClosenessCentrality;
import org.graphstream.algorithm.measure.EigenvectorCentrality;
import org.graphstream.graph.Edge;
import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.IdAlreadyInUseException;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;

import rdf.ItemRDF;
import rdf.ListRDF;
import rdf.NodeRDF;
import rdf.OneRDF;
import rdf.QuerySparql;
import rdf.SetQuerySparql;
import user.Concept;
import user.ConceptsGroup;

enum Deleted { yes, no, yesConcept}; 

public class StreamGraphData {
	private Graph streamGraph;
	private QuantityNodesEdges total;
    private QuantityNodesEdges duplicated;
    private QuantityNodesEdges deleted;
		

	public StreamGraphData() {
		this.streamGraph = new SingleGraph(
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
		
	// get RDFs and convert them to StreamGraph, but this fucntion is call: 
	//    in the firt iteration build StreamGraphData and EdgeTable from RDFs
	//    in the second iteration so foth, just add new data (RDFs) into StreamGraphData and EdgeTable
    // (build stream graph from rdfs in set query Sparql)
	public QuantityNodesEdges buildStreamGraphData_buildEdgeTable_fromRdfs(SetQuerySparql setQuerySparql) {
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
				
				// map RDF to Graph:
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
	// only used by buildStreamGraphData_buildEdgeTable
	// work with only one triple RDF each time
	private void insertRDF(OneRDF oneRDF, QuantityNodesEdges quantityNodesEdges) { 
		// split elements of RDF:
		ItemRDF subjectRDF   = oneRDF.getSubject() ;
		ItemRDF predicateRDF = oneRDF.getPredicate();
		ItemRDF objectRDF    = oneRDF.getObject();
		Node node = null;
		Edge edge = null;
		// work with the subject RDF
		try {
			// case 01
			node = this.streamGraph.addNode(subjectRDF.getShortBlankName());
			quantityNodesEdges.incNumNodes();
			node.addAttribute("fullname",           subjectRDF.getFullName());
			node.addAttribute("shortunderlinename", subjectRDF.getShortUnderlineName());
			node.addAttribute("shortblankname",     subjectRDF.getShortBlankName());  // repeated with node id, but it is important...
			if(Config.nodeLabelStreamGephi)
				node.addAttribute("label", subjectRDF.getShortBlankName());
			// if original concept then put label
			if(WholeSystem.getConceptsRegister().isOriginalConcept(subjectRDF.getShortBlankName())) { 
				node.addAttribute("label", subjectRDF.getShortBlankName());
			}		
		}
		// case 02
		catch(IdAlreadyInUseException e) {
			// repeated node, do nothing, only get it to continue the process
			node = this.streamGraph.getNode(subjectRDF.getShortBlankName());
		}

		// case 12
		// if predicate is known, transform it in attributes into node
		if(predicateRDF.getFullName().equals(Config.addressBasic + "homepage"))
			node.addAttribute("homepage", objectRDF.getShortBlankName());
		else if(predicateRDF.getFullName().equals(Config.addressBasic + "comment"))
			node.addAttribute("comment", objectRDF.getShortBlankName());
		else if(predicateRDF.getFullName().equals(Config.addressBasic + "abstract"))
			node.addAttribute("abstract", objectRDF.getShortBlankName());
		else if(predicateRDF.getFullName().equals(Config.addressBasic + "image"))
			node.addAttribute("image", objectRDF.getShortBlankName());
        
		// continue to common predicate (unknown)
		// work with the object RDF
		else {
			try {
				// case 01
				node = this.streamGraph.addNode(objectRDF.getShortBlankName());
				quantityNodesEdges.incNumNodes();
				node.addAttribute("fullname",           objectRDF.getFullName());
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
				// case 03
				// repeated node, do nothing, only get it to continue the process
				node = this.streamGraph.getNode(objectRDF.getShortBlankName());
			}
			
			// work with predicate RDF
			try {
				// case 01
				edge = this.streamGraph.addEdge(predicateRDF.getShortBlankName(), subjectRDF.getShortBlankName(), objectRDF.getShortBlankName(),true);
				edge.addAttribute("fullname",           predicateRDF.getFullName());
				edge.addAttribute("shortunderlinename", predicateRDF.getShortUnderlineName());
				edge.addAttribute("shortblankname",     predicateRDF.getShortBlankName());
				edge.addAttribute("repeatedtimes",      "0");  // quantity of repeated edges to same pair of nodes (default: 1) 
				// put new edge in hash table
				// if existent predicate (I don't know why!!! because the "catch" should get...) do nothing
				WholeSystem.getEdgesTable().put(predicateRDF.getShortBlankName());
				quantityNodesEdges.incNumEdges();
				if(Config.edgeLabelStreamGephi)
					edge.addAttribute("label", predicateRDF.getShortBlankName());
			}
			// case 4(scratch): same predicate (different subject and object)
			// case 5(scratch): same subject and predicate (different object)
			// case 6(scratch): same predicate and object (different subject)
			// case 10(scratch): subject and object commuted, moreover same predicate
			catch(IdAlreadyInUseException e) {
				try {
					// insert a count element in the id edge and try again
                    String newNameEdge = WholeSystem.getEdgesTable().getNewName(predicateRDF.getShortBlankName());  // add "#n" in the edge name
					edge = this.streamGraph.addEdge(newNameEdge, subjectRDF.getShortBlankName(), objectRDF.getShortBlankName(),true);
					quantityNodesEdges.incNumEdges();
					if(Config.edgeLabelStreamGephi)
						// add modifier to differentiate each link into the graph
						edge.addAttribute("label", newNameEdge);
				}
				// case 7(scratch): same subject, predicate and object    
				catch(EdgeRejectedException e2) {
					Edge repeatedEdge = this.streamGraph.getEdge(predicateRDF.getShortBlankName()); 
					// store +1 in edge "repeatedTimes" attribute. Do not create other edge
					int currentTimes = Integer.parseInt((String)repeatedEdge.getAttribute("repeatedtimes"));
					currentTimes++;
					repeatedEdge.addAttribute("repeatedtimes", String.valueOf(currentTimes));
					// update the edge table
					WholeSystem.getEdgesTable().incEdgeTimes(predicateRDF.getShortBlankName());
				}
			}
			// case 8(scratch): same subject and object (different predicate) 
			catch(EdgeRejectedException e) {
				// search the existent edge with subject and object
				Node sourceNode = this.streamGraph.getNode(subjectRDF.getShortBlankName()); 
				Node targetNode = this.streamGraph.getNode(objectRDF.getShortBlankName()); 
				Edge existentEdge = sourceNode.getEdgeBetween(targetNode);
				// create a extension to the edge attribute. Do not create other edge
				// seach next free attribute...
				int numberNextFreeAttribute = 0;
				for( ; ; numberNextFreeAttribute++) {
					if(existentEdge.getAttribute("nextedge"+numberNextFreeAttribute) == null) 
						break;
				}
				existentEdge.addAttribute("nextedge"+numberNextFreeAttribute, predicateRDF.getShortBlankName());
			}
		}
	}
	
	// don't used (instead of, it was used Gephi Tool Kit)
	public void computeBetweennessCentrality() {
		BetweennessCentrality betweenness = new BetweennessCentrality();
		betweenness.init(this.getStreamGraph());
		//betweenness.setUnweighted();
		betweenness.setCentralityAttributeName("betweenness");
		betweenness.compute();
	}
	// don't used (instead of, it was used Gephi Tool Kit)
	public void computeClosenessCentrality() {
		ClosenessCentrality closeness = new ClosenessCentrality();
		closeness.init(this.getStreamGraph());
		closeness.setCentralityAttribute("closeness");
		closeness.compute();
	}
	// don't used (instead of, it was used Gephi Tool Kit)
	public void computeEigenvectorCentrality() {
		EigenvectorCentrality eingenvector = new EigenvectorCentrality();
		eingenvector.init(this.getStreamGraph());
		eingenvector.setCentralityAttribute("eingenvector");
		eingenvector.compute();	
	}

	/*
	// do not working...
	public void addNewConceptsLabel(ConceptsGroup newConcepts) {
		Node node;
		for(Concept concept : newConcepts.getList()) {
			node = this.streamGraph.getNode(concept.getBlankName());
			// some nodes will not be found because they are from "Category"
			if(node != null) {
			   node.addAttribute("label", concept.getBlankName());  // the process enters here, but this act do not updade the graph visualization!!!
			   //node.changeAttribute("label", concept.getBlankName());  // it is also not working
			}
		}
	}
    */
	
	// Apply K-core on the Graph
	// return quantity of selected concepts that was deleted
	public int applyKCoreFilterTrigger(int k) {
		int totalQuantityDeletedSelectedConcepts = 0;
		int quantityDeletedSelectedConcepts = 0;
		do {
			quantityDeletedSelectedConcepts = this.applyNdegreeFilterTrigger(k);
			totalQuantityDeletedSelectedConcepts += quantityDeletedSelectedConcepts;
		}while(quantityDeletedSelectedConcepts != 0);
		return totalQuantityDeletedSelectedConcepts;
	}
	
	// Apply n-degree filter on the Graph
	// return quantity of selected concepts that was deleted
	public int applyNdegreeFilterTrigger(int n) {
		LinkedList<Node> auxList = new LinkedList<Node>();
		// at first select the candidates nodes and put them in an auxiliary list
		for( Node node : this.streamGraph.getEachNode() ) {
			// if node has degree less than nDegreeFilter...
			if(node.getDegree() < n) {
				// ...and it is not original node, store this node
				if(!WholeSystem.getConceptsRegister().isOriginalConcept((String)node.getAttribute("shortblankname"))) {
					auxList.add(node);
				}
			}
		}
		// second: delete the selected nodes and their respectives edges
		int quantityDeletedSelectedConcepts = 0;
		for( Node node : auxList ) {
			if(this.deleteNode(node) == Deleted.yesConcept)
				quantityDeletedSelectedConcepts++;
		} 
		return quantityDeletedSelectedConcepts;
	}
	
	// delete a node and all edges linked it
	// return true if node is a concept
	// NEVER it delete an original concept
	public Deleted deleteNode(Node node) {
		// if original concept, do not delete
		if(WholeSystem.getConceptsRegister().isOriginalConcept((String)node.getAttribute("shortblankname"))) 
		   return Deleted.no;
		this.incTotalNodesDeleted();
		this.incTotalEdgesDeleted(node.getEdgeSet().size());
		this.incTotalNodes(-1);
		this.incTotalEdges(-1*node.getEdgeSet().size());
		// remove all edges linked with this node
		for( Edge edge : node.getEachEdge()) {
			this.streamGraph.removeEdge(edge);
		}
		// finally, remove the node of the Stream Graph
		this.streamGraph.removeNode(node);
		// remove node of the concepts register, if it is the case
		if(WholeSystem.getConceptsRegister().isConcept((String)node.getAttribute("shortblankname"))) {
			WholeSystem.getConceptsRegister().removeConcept((String)node.getAttribute("shortblankname"));
			return Deleted.yesConcept;
		}
		return Deleted.yes;
	}
	
	// delete a node and all edges linked it
	// return true if node is a concept
	public void insert(Node node, List<Edge> edges) {
		this.incTotalNodesDeleted(-1);
		this.incTotalEdgesDeleted(-1*edges.size());
		this.incTotalNodes();
		this.incTotalEdges(edges.size());
		
		// insert node
		Node newNode = this.streamGraph.addNode(node.getId());
		newNode.addAttribute("shortblankname", node.getAttribute("shortblankname"));
		newNode.addAttribute("fullname", node.getAttribute("fullname"));
		if(node.getAttribute("homepage") != null) 
			newNode.addAttribute("homepage", node.getAttribute("homepage"));
		if(node.getAttribute("abstract") != null) 
			newNode.addAttribute("abstract", node.getAttribute("abstract"));
		if(node.getAttribute("comment") != null) 
			newNode.addAttribute("comment", node.getAttribute("comment"));
		if(node.getAttribute("image") != null) 
			newNode.addAttribute("image", node.getAttribute("image"));
		
		// insert all edges
		for( Edge edge : edges ) {
			Node nodeSource = edge.getSourceNode();
			Node nodeTarget = edge.getTargetNode();
			this.streamGraph.addEdge(edge.getId(), nodeSource, nodeTarget, true);
		}
	}

	public void deleteCommonNodes_remainOriginalAndSelectedConcepts() {
		ArrayList<Node> lista = new ArrayList<Node>();
		for(Node node : this.streamGraph.getNodeSet()) {
			// if it is not original or selected concepts, separate it to delete it and all its edges
			if(!WholeSystem.getConceptsRegister().isConcept(node.getId())) 
				lista.add(node);  // it's ok because it is not possible remove directly of streamGraph (there is a bug in this operation)
		}
		for(Node node: lista)
			this.deleteNode(node);
	}
	
	public static String nodeToString(Node node) {
		StringBuffer str = new StringBuffer();
		str.append("\nID: ");
		str.append(node.toString());
		str.append(" - Full name: ");
		str.append(node.getAttribute("fullname"));
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
		str.append("\nEdges:\n");
		for( Edge edge : node.getEachEdge()) {
			str.append("      ");
			str.append(edge.toString());
			str.append(" (times: ");
			str.append(edge.getAttribute("repeatedTimes"));
			str.append(")\n");
			for(int numberExtraEdge = 0; ; numberExtraEdge++) {
				if(edge.getAttribute("nextedge"+numberExtraEdge) == null) 
					break;
				else {
					str.append("         extra edge ");
					str.append(numberExtraEdge);
					str.append(": ");
					str.append(edge.getAttribute("nextedge"+numberExtraEdge));
					str.append("\n");
				}
			}
		}
		return str.toString();
	}

	
	public String toStringGraph() {
		StringBuffer str = new StringBuffer();
		Graph graph = this.getStreamGraph();
		str.append("\n\nGraph stream:\n");
		for( Node node : graph.getEachNode() ) {
			str.append(StreamGraphData.nodeToString(node));
		}
		return str.toString();
	}
	
	public String toStringShort() {
		return  "\n\nGraph stream (resume):\n" +
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
