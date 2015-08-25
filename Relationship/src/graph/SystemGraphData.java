package graph;

import main.*;
import map.Proposition;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Node;
import org.gephi.statistics.plugin.ConnectedComponents;
import org.graphstream.graph.Graph;

import user.Concept;
import user.GroupConcept;

public class SystemGraphData {
	private GephiGraphData gephiGraphData;
	private NodesTableHash  nodesTableHash;  // work like nodes index
	private NodesTableArray nodesTableArray;
	private int connectedComponentsCount;  
	private Ranks ranks;
	private NodesTableArray betweennessSortTable;
	private NodesTableArray closenessSortTable;
	private NodesTableArray eigenvectorSortTable;
	
	public SystemGraphData() {
		this(new GephiGraphData());
	}

	public SystemGraphData(GephiGraphData gephiGraphData) {
		this.gephiGraphData           		= gephiGraphData;
		this.nodesTableHash           		= new NodesTableHash();
		this.nodesTableArray          		= null;  // will be to fill after nodeTableHash filling
		this.connectedComponentsCount 		= 0;
		this.ranks                    		= null;
		this.betweennessSortTable     		= null;
		this.closenessSortTable       		= null;
		this.eigenvectorSortTable     		= null;
	}

	public GephiGraphData getGephiGraphData() {
		return this.gephiGraphData;
	}
	public NodesTableArray getNodesTableArray() {
		return this.nodesTableArray;
	}
	public void setGephiGraphData(GephiGraphData gephiGraphData) {
		this.gephiGraphData = gephiGraphData;
	}
	// seach nodeData in TableHash
	public NodeData getNodeData(String nodeId) {
		return this.nodesTableHash.get(nodeId);
	}
	public int getConnectedComponentsCount() {
		return this.connectedComponentsCount;
	}
	public void setConnectedComponentsCount(int count) {
		this.connectedComponentsCount = count;
	}
	public Ranks getRanks() {
		return this.ranks;
	}
	public void setRanks(Ranks ranks) {
		this.ranks = ranks;
	}
	public NodesTableArray getBetweennessSortTable() {
		return this.betweennessSortTable;
	}
	public void setBetweennessSortTable(NodesTableArray betweennessSortTable) {
		this.betweennessSortTable = betweennessSortTable;
	}
	public NodesTableArray getClosenessSortTable() {
		return this.closenessSortTable;
	}
	public void setClosenessSortTable(NodesTableArray closenessSortTable) {
		this.closenessSortTable = closenessSortTable;
	}
	public NodesTableArray getEigenvectorSortTable() {
		return this.eigenvectorSortTable;
	}
	public void setEigenvectorSortTable(NodesTableArray eigenvectorSortTable) {
		this.eigenvectorSortTable = eigenvectorSortTable;
	}
	
	// It copies all graph from StreamGraph format to GephiGraph format.
	// It also builds nodesTableHash and nodesTableArray
	public QuantityNodesEdges buildGephiGraphData_NodesTableHash_NodesTableArray_fromStreamGraph() throws Exception {
		org.graphstream.graph.Graph streamGraph = WholeSystem.getStreamGraphData().getStreamGraph();
		this.nodesTableArray = new NodesTableArray(WholeSystem.getStreamGraphData().getRealTotalNodes());
		NodeData newNodeData = null;
		QuantityNodesEdges quantityNodesEdges = new QuantityNodesEdges();
		Config.Status status;
		// get "label" attribute
		AttributeColumn labelAttributeColumn = this.getGephiGraphData().getAttributeModel().getNodeTable().getColumn("Label");
		// first: each node, add in GephiGraph, nodesTableArray and nodesTableHash
		for(org.graphstream.graph.Node streamNode : streamGraph.getEachNode() ) {
			String idNode         = streamNode.toString();
			String shortBlankName = streamNode.getAttribute("shortblankname");
			String fullName       = streamNode.getAttribute("fullname");
			// create a new gephiNode
			Node gephiNode = this.gephiGraphData.getGraphModel().factory().newNode(idNode);
			// add attribute "label" if the concept is selected or original
			if(WholeSystem.getConceptsRegister().isConcept(shortBlankName)) 
				gephiNode.getNodeData().getAttributes().setValue(labelAttributeColumn.getIndex(), shortBlankName);
			else if(Config.nodeLabelFileGephi)  // or add to all, if configured to this
				gephiNode.getNodeData().getAttributes().setValue(labelAttributeColumn.getIndex(), shortBlankName);				
				
			// add node in gephiGraphData
			this.gephiGraphData.getGephiGraph().addNode(gephiNode);
			
			status = WholeSystem.getConceptsRegister().getStatus(shortBlankName);
			// create a new NodeData object
			newNodeData = new NodeData(idNode, shortBlankName, fullName, streamNode, gephiNode, status);
			// add attributes to new nodeData
			if(streamNode.getAttribute("homepage") != null) 
				newNodeData.setHomepageAttribute(streamNode.getAttribute("homepage").toString());
			if(streamNode.getAttribute("abstract") != null) 
				newNodeData.setAbstractAttribute(streamNode.getAttribute("abstract").toString());
			if(streamNode.getAttribute("comment") != null) 
				newNodeData.setCommentAttribute(streamNode.getAttribute("comment").toString());
			if(streamNode.getAttribute("image") != null) 
				newNodeData.setImageAttribute(streamNode.getAttribute("image").toString());
			
			// add node in nodesTableArray
			this.nodesTableArray.insert(newNodeData);
			// add node (the same) in nodesTableHash
			this.nodesTableHash.put(shortBlankName,  newNodeData);
			quantityNodesEdges.incNumNodes();
		}
		// second: each edge, add in gephiGraphData
		for( org.graphstream.graph.Edge streamEdge : streamGraph.getEachEdge() ) {
			String idNodeSource  = streamEdge.getSourceNode().toString();
			String idNodeTarget  = streamEdge.getTargetNode().toString();
			Node gephiNodeSource = this.gephiGraphData.getGephiGraph().getNode(idNodeSource);
			Node gephiNodeTarget = this.gephiGraphData.getGephiGraph().getNode(idNodeTarget);
			Edge gephiEdge  = this.gephiGraphData.getGraphModel().factory().newEdge(streamEdge.toString(), gephiNodeSource, gephiNodeTarget, 1, true);
			// put "label" if configured to this
			if(Config.edgeLabelFileGephi)  // or add to all, if configured to this
				gephiEdge.getEdgeData().getAttributes().setValue(labelAttributeColumn.getIndex(), streamEdge.toString());				

			// if exist, also put the attribute "repeatedTimes" in the edge
			if(streamEdge.getAttribute("repeatedTimes") != null)
				gephiEdge.getEdgeData().getAttributes().setValue("repeatedTimes", streamEdge.getAttribute("repeatedTimes"));				

			// if exist, also put the attributes "nextedge#" in the edge
			for(int numberExtraEdge = 0; ; numberExtraEdge++) {
				if(streamEdge.getAttribute("nextedge"+numberExtraEdge) == null) 
					break;
				else 
					gephiEdge.getEdgeData().getAttributes().setValue("nextedge"+numberExtraEdge, streamEdge.getAttribute("nextedge"+numberExtraEdge));				
			}	
			
			// insert the edge in the gephi graph
			this.gephiGraphData.getGephiGraph().addEdge(gephiEdge);
			quantityNodesEdges.getNumEdges();
		}		
		return quantityNodesEdges;
	}
	
	// store results in NodesData set
	// BEFORE: must calculate calculateGephiGraphDistanceMeasures() from GephiGraphData class
	public void storeDistanceMeasuresWholeNetwork() {
		Double betweennessValue, closenessValue;
		NodeData nodeData;		
		// copy Betweenness and Closeness values to NodesTableArray 
		for(Node gephiNode: this.gephiGraphData.getGephiGraph().getNodes()) {
			String nodeId = gephiNode.getNodeData().getId();
			betweennessValue  = (Double)gephiNode.getNodeData().getAttributes().getValue(this.gephiGraphData.getBetweennessColumn().getIndex());
			closenessValue    = (Double)gephiNode.getNodeData().getAttributes().getValue(this.gephiGraphData.getClosenessColumn().getIndex());
			// put the values in NodeData by NodeTableHash
			nodeData = this.nodesTableHash.get(nodeId);
			nodeData.setBetweenness(betweennessValue);
			nodeData.setCloseness(closenessValue);
		}	
	}
	// store results in NodesData set
	// BEFORE: must calculate calculateGephiGraphEigenvectorMeasure() from GephiGraphData class
	public void storeEigenvectorMeasuresWholeNetwork() {
		Double eigenvectorValue;
		NodeData nodeData;		
		// copy Eigenvector values to NodesTableArray 
		for(Node gephiNode: this.gephiGraphData.getGephiGraph().getNodes()) {
			String nodeId = gephiNode.getNodeData().getId();
			eigenvectorValue  = (Double)gephiNode.getNodeData().getAttributes().getValue(this.gephiGraphData.getEigenvectorColumn().getIndex());
			// put the values in NodeData by NodeTableHash
			nodeData = this.nodesTableHash.get(nodeId);
			nodeData.setEigenvector(eigenvectorValue);
		}	
	}
	
	public void sortBetweennessWholeNetwork() {
		this.betweennessSortTable = this.nodesTableArray.sortBetweenness();
	}
	public void sortClosenessWholeNetwork() {
		this.closenessSortTable   = this.nodesTableArray.sortCloseness();
	}
	public void sortEigenvectorWholeNetwork() {
		this.eigenvectorSortTable = this.nodesTableArray.sortEigenvector(); 
	}
	
	// create rank of MeasuresRanks objects
	public void buildSubGraphRanks() throws Exception {
		// create an array of the MeasureRank (Ranks object), size = total number of the connect components
		this.ranks = new Ranks(this.connectedComponentsCount);
		NodeData nodeData;
		int connectedComponentNumber;
			
		for(Node gephiNode: this.gephiGraphData.getGephiGraph().getNodes()) {
			// get the number of connected component from Gephi
			String nodeId = gephiNode.getNodeData().getId();
			connectedComponentNumber = (Integer)gephiNode.getNodeData().getAttributes().getValue(this.gephiGraphData.getConnectedComponentColumn().getIndex());
			
			// put the number of the connected component in NodeData
			nodeData = this.nodesTableHash.get(nodeId);
			nodeData.setConnectedComponent(connectedComponentNumber);
			
			// create a GephiNode object (from current Gephi node) and put it into the GephiGraphData object (belongs to MeasureRank object), 
			// by connected component number
			org.gephi.graph.api.Graph currentGephiGraph;
			Node newGephiNode = this.gephiGraphData.getGraphModel().factory().newNode(nodeId);
			currentGephiGraph = ranks.getMeasuresRankTable(connectedComponentNumber).getGephiGraphData().getGephiGraph();
			currentGephiGraph.addNode(newGephiNode);
			
			// for each edge belonged to gephiNode, copy it to currentGephiGraph
			Edge edgesGephiNode[] = this.gephiGraphData.getGephiGraph().getEdges(gephiNode).toArray();
			for(int i=0; i < edgesGephiNode.length; i++) {
				// add edge into currentGephiGraph
				currentGephiGraph.addEdge(edgesGephiNode[i]);
			}
		}
	}

	// build the basic table to each connected component
	// and build the group of the original concepts
	public void buildSubGraphsTablesInConnectedComponents() throws Exception {
		org.gephi.graph.api.Graph currentGephiGraph;
		int connectedComponentNodesQuantity;
		NodesTableArray newBasicTable;
		NodeData nodeData;
		Concept concept;
		// for each group of the connected components, build the NodesTableArray:
		for(int i=0; i < this.ranks.getCount(); i++) {
			currentGephiGraph = this.ranks.getMeasuresRankTable(i).getGephiGraphData().getGephiGraph();
			connectedComponentNodesQuantity = currentGephiGraph.getNodeCount();
			// create the Basic Table Array
			newBasicTable = new NodesTableArray(connectedComponentNodesQuantity);
			// create the Group Original Concepts
			for(Node gephiNode: currentGephiGraph.getNodes()) {
				// fill the Basic Table Array
				nodeData = this.nodesTableHash.get(gephiNode.toString());
				newBasicTable.insert(nodeData);
				// fill the Group Original Concepts
				// concept = WholeSystem.getConceptsRegister().getConcept(nodeData.getShortName());
				concept = new Concept(nodeData.getFullName(),nodeData.getShortName(), nodeData.getStatus(), 0, Config.Category.no, 0, Config.withoutConnectedComponent);
				if(concept.getStatus() == Config.Status.originalConcept)
					this.ranks.getMeasuresRankTable(i).getOriginalGroupConcepts().add(concept);
			}
			this.ranks.getMeasuresRankTable(i).setBasicTable(newBasicTable);
		}
		// I gave up calculate measures to each sub-graph, but to use the measures of the whole graph (sorted) 
	}
	
	public void sortConnectecComponentRanks() throws Exception {
		NodesTableArray currentNodesTableArray, sortedNodesTableArray;
		// for each group of the connected components, build the sorted NodesTableArray:
		for(int i=0; i < this.ranks.getCount(); i++) {
			
			// create each new sort table with measures from BasicTableArray
			currentNodesTableArray = this.ranks.getMeasuresRankTable(i).getBasicTable();
			
			sortedNodesTableArray  = currentNodesTableArray.sortBetweenness();
			this.ranks.getMeasuresRankTable(i).setBetweenness(sortedNodesTableArray);
			
			sortedNodesTableArray  = currentNodesTableArray.sortCloseness();
			this.ranks.getMeasuresRankTable(i).setCloseness(sortedNodesTableArray);
			
			sortedNodesTableArray  = currentNodesTableArray.sortEigenvector();
			this.ranks.getMeasuresRankTable(i).setEigenvector(sortedNodesTableArray);
			
			int filterQuantity = (int)(WholeSystem.getQuantityOriginalConcepts() * Config.proporcionBetweenness);
			sortedNodesTableArray  = currentNodesTableArray.sortBetweennessCloseness(filterQuantity);
			this.ranks.getMeasuresRankTable(i).setBetweennessCloseness(sortedNodesTableArray);	
		}
	}
		
	// select the firt largest maxBetweennessCloseness nodes of each connected component
	public int selectLargestNodesBetweennessCloseness(int iteration) throws Exception {
		// quantity total of nodes to change the status (all connected components)
		int countTotalSelectNodes = (int)(WholeSystem.getQuantityOriginalConcepts() * Config.proporcionBetweennessCloseness);
		int countConnectedComponentSelectNodes;
		NodeData currentNodeData;
		int count = 0;
		for(int i=0; i < this.connectedComponentsCount; i++) {
			// calculate proportionate the quantity to each connected component group  
			countConnectedComponentSelectNodes = 
			(int)( ( (double)countTotalSelectNodes / (double)WholeSystem.getQuantityOriginalConcepts() ) * 
					this.ranks.getMeasuresRankTable(i).getOriginalGroupConcepts().size() + 
			        Config.precisionBetweennessCloseness
			      );
			// mark the level of the firt nodes to new status, except original nodes
			for(int j=0, k=0; k < countConnectedComponentSelectNodes &&
					          j < this.ranks.getMeasuresRankTable(i).getBetweennessCloseness().getCount() &&
					          k < Config.maxBetweennessCloseness; 
				j++) {
				currentNodeData = this.ranks.getMeasuresRankTable(i).getBetweennessCloseness().getNodeData(j);
				// changes status only of nodes still not selected or not original concept
				if(!WholeSystem.getConceptsRegister().isConcept(currentNodeData.getShortName())) {
					this.ranks.getMeasuresRankTable(i).getBetweennessCloseness().getNodeData(j).setStatus(Config.Status.selectedBetweennessClosenessConcept);
					k++;
					// add this node in the general register concepts
					Concept concept = new Concept(currentNodeData.getFullName(),currentNodeData.getShortName(), 
							                      Config.Status.selectedBetweennessClosenessConcept, iteration, Config.Category.no, 0, i);
					WholeSystem.getConceptsRegister().add(concept);
					count++;
				}
			}	
		}
		return count;
	}
	
	// select the firt largest maxEigenvector nodes of each connected component
	public int selectLargestNodesEigenvector(int iteration) throws Exception {
		// quantity total of nodes to change the status (all connected components)
		int countTotalSelectNodes = (int)(WholeSystem.getQuantityOriginalConcepts() * Config.proporcionEigenvector);
		int countConnectedComponentSelectNodes;
		NodeData currentNodeData;
		int count = 0;
		for(int i=0; i < this.connectedComponentsCount; i++) {
			// calculate proportionate the quantity to each connected component group  
			countConnectedComponentSelectNodes = 
			(int)( ( (double)countTotalSelectNodes / (double)WholeSystem.getQuantityOriginalConcepts()) * 
					this.ranks.getMeasuresRankTable(i).getOriginalGroupConcepts().size() + 
			        Config.precisionEigenvector
			      );
			// mark the level of the firt nodes to new status, except original nodes
			for(int j=0, k=0; k < countConnectedComponentSelectNodes && 
					          j < this.ranks.getMeasuresRankTable(i).getEigenvector().getCount() &&
					          k < Config.maxEigenvector;  
				j++) {
				currentNodeData = this.ranks.getMeasuresRankTable(i).getEigenvector().getNodeData(j);
				// changes status only of nodes still not selected or not original concept
				if(!WholeSystem.getConceptsRegister().isConcept(currentNodeData.getShortName())) {
					this.ranks.getMeasuresRankTable(i).getEigenvector().getNodeData(j).setStatus(Config.Status.selectedEigenvectorConcept);
					k++;
					// add this node in the general register concepts
					Concept concept = new Concept(currentNodeData.getFullName(),currentNodeData.getShortName(), 
							                      Config.Status.selectedEigenvectorConcept, iteration, Config.Category.no, 0, i);
					WholeSystem.getConceptsRegister().add(concept);
					count++;
				}
			}	
		}
		return count;
	}

	// Gephi already does this...
	/*
	public void fillGephiGraphAttributes() throws Exception {
		NodeData nodeData;
		org.gephi.graph.api.Node node;
		// to each node in Table Array, update the attributes in Gephi Graph
		for(int i=0; i<this.nodesTableArray.getCount(); i++) {
			nodeData  = this.nodesTableArray.getNodeData(i);
			node      = this.getGephiGraphData().getGephiGraph().getNode(nodeData.getStrIdNode());
			node.getNodeData().getAttributes().setValue( this.getGephiGraphData().getBetweennessColumn().getIndex(),        nodeData.getBetweenness() );
			node.getNodeData().getAttributes().setValue( this.getGephiGraphData().getClosenessColumn().getIndex(),          nodeData.getCloseness() );
			node.getNodeData().getAttributes().setValue( this.getGephiGraphData().getEigenvectorColumn().getIndex(),        nodeData.getEigenvector() );
			node.getNodeData().getAttributes().setValue( this.getGephiGraphData().getConnectedComponentColumn().getIndex(), nodeData.getConnectedComponent() );
		}
	}
	*/
	
	public String reportSelectedNodes(int iteration) throws Exception {
		StringBuffer str = new StringBuffer();
		for(int i=0; i < this.connectedComponentsCount; i++) {
			str.append("Connected component number: ");
			str.append(this.ranks.getMeasuresRankTable(i).getConnectedComponentNumber());
			str.append("   (iteration ");
			str.append(iteration);
			str.append(")\n\nOriginal concepts:\n");
			str.append(this.ranks.getMeasuresRankTable(i).getOriginalGroupConcepts().toString());
			str.append("\nNew concepts added from betweenness + closeness rank:\n");
			str.append(WholeSystem.getConceptsRegister().getSelectedBetweennessClosenessConcepts(iteration, i).toString());
			str.append("\nNew concepts added from eigenvector rank:\n");
			str.append(WholeSystem.getConceptsRegister().getSelectedEigenvectorConcepts(iteration, i).toString());			
			str.append(Config.doubleLine);
		}
		str.append("Whole network   (iteration ");
		str.append(iteration);
		if(iteration == 0)
			str.append(")\n\nCurrent concepts:\n");
		else
			str.append(")\n\nCurrent concepts (original and selected in the previous iterations) :\n");
		str.append(WholeSystem.getConceptsRegister().getCurrentConcepts(iteration).toString());  // get original concepts and selected concepts of the previous iteration
		str.append("\nNew concepts:\n");
		str.append(WholeSystem.getConceptsRegister().getSelectedConcepts(iteration).toString());
		str.append("\n");
		return str.toString();
	}
	
	// copy selected concepts to a NodesTableArray
	// sort this table e store it in WholeSystem.sortAverageSelectedConcepts
	// (do not enter: original concepts, concepts that already were category or concepts with zero rdfs)
	// return quantity of concepts that already were category or concepts with zero rdfs
	public int createSortAverageOnlySelectedConcepts() throws Exception {
		GroupConcept selectedConcepts = WholeSystem.getConceptsRegister().getSelectedConcepts();
		NodesTableArray nodesTableArray = new NodesTableArray(selectedConcepts.size());
		int quantity = 0, excluded = 0;
		for(int i=0; i<selectedConcepts.size(); i++) {
			Concept concept = selectedConcepts.getConcept(i);
			if(concept.getCategory() != Config.Category.was && concept.getQuantityRdfs() != 0) {	
				NodeData nodeData = this.getNodeData(concept.getBlankName());
				nodesTableArray.insert(nodeData);
				quantity++;
			}
			else
				excluded++;
		}
Log.consoleln("\n\nquantity="+quantity+"\n\n");
Log.consoleln(nodesTableArray.toString());
		//nodesTableArray.sortCrescentAverage(quantity);
		nodesTableArray.sortBetweenness(quantity);
Log.consoleln("\n\nsorted 1\n\n");
Log.consoleln(nodesTableArray.toString());
		WholeSystem.setSortAverageSelectedConcepts(nodesTableArray);
Log.consoleln("\n\nsorted 2\n\n");
Log.consoleln(WholeSystem.getSortAverageSelectedConcepts().toString());
		return excluded;
	}

		
	// create a raw map concept from Stream Graph
	// return quantity of repeated propositions (not inserted)
	public int buildRawConceptMap() {
		int quantityRepeatedPropositions = 0;
		for( org.graphstream.graph.Node node : WholeSystem.getStreamGraphData().getStreamGraph().getEachNode() ) {
			for( org.graphstream.graph.Edge edge : node.getEachEdge()) {
				NodeData sourceConcept = this.getNodeData(edge.getSourceNode().getId());
				NodeData targetConcept = this.getNodeData(edge.getTargetNode().getId());
				// if it can not intert, plus 1
				if(!WholeSystem.getConceptMap().insert(sourceConcept, edge.getId(), targetConcept)) {
					quantityRepeatedPropositions++;
					continue;
				}
				
				// if exist extra edges then get each one
				for(int numberExtraEdge = 0; ; numberExtraEdge++) {
					if(edge.getAttribute("nextedge"+numberExtraEdge) == null) 
						break;
					else {
						// if it can not intert, plus 1
						if(!WholeSystem.getConceptMap().insert(sourceConcept, (String)edge.getAttribute("nextedge"+numberExtraEdge), targetConcept)) {
							quantityRepeatedPropositions++;
						}
					}
				}
			}
		}
		return quantityRepeatedPropositions;
	}
	
		
	public String toString() {
		return  "\nQuantity connected component: " + this.connectedComponentsCount +
		        "\n"+Config.doubleLine+"Table array: "+Config.singleLine + 
				this.nodesTableArray.toString() +
		        "\n"+Config.doubleLine+"Table array - Betweenness sorted:"+Config.singleLine + 
				this.betweennessSortTable.toString() +
		        "\n"+Config.doubleLine+"Table array - Closeness sorted (closeness sorted): "+Config.singleLine + 
				this.closenessSortTable.toString() +
		        "\n"+Config.doubleLine+"Table array - Eingenvector sorted: "+Config.singleLine + 
				this.eigenvectorSortTable.toString() +		
				this.getRanks().toString();
	}
	
	public String toStringShort(int quantityNodes) {
		return  "\nQuantity connected component: " + this.connectedComponentsCount +
		        "\n"+Config.doubleLine+"Table array - Betweenness sorted - (only the first "+quantityNodes+" nodes)"+
				Config.singleLine + 
				this.betweennessSortTable.toStringShort(quantityNodes) +
		        "\n"+Config.doubleLine+"Table array - Closeness sorted - (only the first "+quantityNodes+" nodes)"+
				Config.singleLine + 
				this.closenessSortTable.toStringShort(quantityNodes) +
		        "\n"+Config.doubleLine+"Table array - Eingenvector sorted - (only the first "+quantityNodes+" nodes)"+
				Config.singleLine + 
				this.eigenvectorSortTable.toStringShort(quantityNodes) +		
				this.getRanks().toStringShort(quantityNodes);
	}
	
	
	
}
