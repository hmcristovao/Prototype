package myClasses;

import org.gephi.data.attributes.api.AttributeTable;
import org.graphstream.graph.Edge;
import org.graphstream.graph.IdAlreadyInUseException;
import org.graphstream.graph.Node;

public class SystemGraphData {

	private StreamGraphData streamGraphData;
	private GephiGraphData gephiGraphData;
	private AttributeTable attributeTable;
	private Ranks ranks;
	private NodesSet nodesSet;
	
	private static int modifier = 0;
	private QuantityNodesEdges added;
    private QuantityNodesEdges duplicate;
		
	public SystemGraphData() {
		this(new StreamGraphData(), new GephiGraphData());
	}

	public SystemGraphData(StreamGraphData streamGraphData, GephiGraphData gephiGraphData) {
		this.streamGraphData = streamGraphData; 
		this.gephiGraphData  = gephiGraphData;
		this.attributeTable  = null;
		this.ranks           = new Ranks();
		this.nodesSet        = new NodesSet(); 
	}

	public StreamGraphData getStreamGraphData() {
		return this.streamGraphData;
	}
	public void setStreamGraphData(StreamGraphData streamGraphData) {
		this.streamGraphData = streamGraphData;
	}
	public GephiGraphData getGephiGraphData() {
		return this.gephiGraphData;
	}
	public void setGephiGraphData(GephiGraphData gephiGraphData) {
		this.gephiGraphData = gephiGraphData;
	}
	public AttributeTable getAttributeTable() {
		return this.attributeTable;
	}
	public void setAttributeTable(AttributeTable attributeTable) {
		this.attributeTable = attributeTable;
	}
	public Ranks getRanks() {
		return this.ranks;
	}
	public void setRanks(Ranks ranks) {
		this.ranks = ranks;
	}
	public NodesSet getNodesSet() {
		return this.nodesSet;
	}
	public void setSetNodes(NodesSet setNodes) {
		this.nodesSet = setNodes;
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
		return String.valueOf(SystemGraphData.modifier); 
	}
	
	public static void incModifier() {
		SystemGraphData.modifier++;
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

	private void insertRDF(OneRDF oneRDF, QuantityNodesEdges quantityNodesEdges) { 
		// split elements of RDF:
		ItemRDF subjectRDF   = oneRDF.getSubject() ;
		ItemRDF predicateRDF = oneRDF.getPredicate();
		ItemRDF objectRDF    = oneRDF.getObject();
		Node node = null;
		Edge edge = null;
		try {
			node = this.streamGraph.addNode(subjectRDF.getValue());
			quantityNodesEdges.incNumNodes();
			node.addAttribute("shortname", Concept.underlineToBlank(subjectRDF.getShortName()));
			if(Constants.nodeLabel)
				node.addAttribute("label", subjectRDF.getShortName());
			// if main node, put label
			if(((NodeRDF)subjectRDF).getLevel() == Constants.Level.originalConcept) { 
				node.addAttribute("label", Concept.underlineToBlank(subjectRDF.getShortName()));
			}		
		}
		catch(IdAlreadyInUseException e) {
			// repeated node, do nothing
			node = this.streamGraph.getNode(subjectRDF.getValue());
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
				node = this.streamGraph.addNode(objectRDF.getValue());
				quantityNodesEdges.incNumNodes();
				node.addAttribute("shortname", Concept.underlineToBlank(objectRDF.getShortName()));
				if(Constants.nodeLabel)
					node.addAttribute("label", objectRDF.getShortName());
				// if main node, put label 
				if(((NodeRDF)objectRDF).getLevel() == Constants.Level.originalConcept) { 
					node.addAttribute("label", Concept.underlineToBlank(objectRDF.getShortName()));
				}
			}
			catch(IdAlreadyInUseException e) {
				// repeated node, do nothing
				node = this.streamGraph.getNode(objectRDF.getValue());
			}
			try {
				edge = this.streamGraph.addEdge(predicateRDF.getValue(), subjectRDF.getValue(), objectRDF.getValue(),true);
				quantityNodesEdges.incNumEdges();
				if(Constants.edgeLabel)
					edge.addAttribute("label", predicateRDF.getShortName());
			}
			catch(IdAlreadyInUseException e) {
				// repeated edge, insert a different element in the identifying string and try again
				StreamGraphData.incModifier();
				edge = this.streamGraph.addEdge(predicateRDF.getValue()+" - "+StreamGraphData.getStrModifier(), subjectRDF.getValue(), objectRDF.getValue(),true);
				quantityNodesEdges.incNumEdges();
				if(Constants.edgeLabel)
					// add modifier to differentiate each link into the graph
					edge.addAttribute("label", predicateRDF.getShortName()+" - "+StreamGraphData.getStrModifier());
			}
		}
	}
	
	
	public String toString() {
		return  this.getStreamGraphData().toString() +
				this.getGephiGraphData().toString() +
				this.getAttributeTable().toString() +
				this.getRanks().toString() +
				this.getNodesSet().toString() +
				"\ntotalNodes (count)= " 	+ this.getTotalNodes() +
				"\ntotalNodes (real) = " 	+ this.getStreamGraphData().getStreamGraph().getNodeCount() +
				"\ntotalEdges (count)= " 	+ this.getTotalEdges() + 
				"\ntotalNodesDuplicate = " 	+ this.getTotalNodesDuplicate() +
				"\ntotalEdgesDuplicate = " 	+ this.getTotalEdgesDuplicate();
	}


}
