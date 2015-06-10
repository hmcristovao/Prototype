package graph;

import main.*;
import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Node;
import org.gephi.statistics.plugin.ConnectedComponents;
import org.gephi.statistics.plugin.EigenvectorCentrality;
import org.gephi.statistics.plugin.GraphDistance;

import rdf.SetQuerySparql;

public class SystemGraphData {
	private int originalQuantity;
	private StreamGraphData streamGraphData;
	private GephiGraphData gephiGraphData;
	// private - file GEXF ...
	private NodesTableHash  nodesTableHash;  // work like nodes index
	private NodesTableArray nodesTableArray;
	private int connectedComponentsCount;
	private Ranks ranks;
	private NodesTableArray betweennessSortTable;
	private NodesTableArray closenessSortTable;
	private NodesTableArray eigenvectorSortTable;
	
	public SystemGraphData(int originalQuantity) {
		this(new StreamGraphData(), new GephiGraphData(), originalQuantity);
	}

	public SystemGraphData(StreamGraphData streamGraphData, GephiGraphData gephiGraphData, int originalQuantity) {
		this.originalQuantity         = originalQuantity;
		this.streamGraphData          = streamGraphData; 
		this.gephiGraphData           = gephiGraphData;
		this.nodesTableHash           = new NodesTableHash();
		this.nodesTableArray          = null;  // will be to fill after nodeTableHash filling
		this.connectedComponentsCount = 0;
		this.ranks                    = null;
		this.betweennessSortTable     = null;
		this.closenessSortTable       = null;
		this.eigenvectorSortTable     = null;
	}

	public int getOriginalQuantity() {
		return this.originalQuantity;
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
	public int getConnectedComponentsCount() {
		return this.connectedComponentsCount;
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
	
	// copy all graph from StreamGraph format to GephiGraph format
	// also build nodesTableHash and nodesTableArray
	public void buildGephiGraphData_NodesTableHash_NodesTableArray() throws Exception {
		org.graphstream.graph.Graph streamGraph = streamGraphData.getStreamGraph();
		this.nodesTableArray = new NodesTableArray(this.getStreamGraphData().getTotalNodes());
		NodeData newNodeData = null;
		// each node: add in graphData, nodesTableArray and nodesTableHash
		for( org.graphstream.graph.Node streamNode : streamGraph.getEachNode() ) {
			// add node in gephiGraphData
			String idNode = streamNode.toString();
			Node gephiNode = this.gephiGraphData.getGraphModel().factory().newNode(idNode);
			this.gephiGraphData.getGephiGraph().addNode(gephiNode);
			// create a new NodeData object
			newNodeData = new NodeData(idNode, 
					                   streamNode.getAttribute("shortname").toString(),
					                   streamNode,
					                   gephiNode,
					                   (streamNode.getAttribute("original").toString().compareTo("true")==0 ? true : false)
					                  );
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
			this.nodesTableHash.put(idNode,  newNodeData);
		}
		// each edge: add in graphData
		for( org.graphstream.graph.Edge streamEdge : streamGraph.getEachEdge() ) {
			String idNode0 = streamEdge.getNode0().toString();
			String idNode1 = streamEdge.getNode1().toString();
			Node gephiNode0 = this.gephiGraphData.getGephiGraph().getNode(idNode0);
			Node gephiNode1 = this.gephiGraphData.getGephiGraph().getNode(idNode1);
			Edge gephiEdge = this.gephiGraphData.getGraphModel().factory().newEdge(streamEdge.toString(), gephiNode0, gephiNode1, 1, true);
			this.gephiGraphData.getGephiGraph().addEdge(gephiEdge);
		}		
	}
	
	// put results in NodesData set
	public void calculateMeasuresWholeNetwork() {
		// at firt, calculate the measures: betweenness, closeness and eigenvector
		this.gephiGraphData.buildGephiGraphTable();
		Double betweennessValue, closenessValue, eigenvectorValue;
		String nodeId;
		NodeData nodeData;		
		// copy Betweenness, Closeness and Eigenvector values to NodesTableArray 
		AttributeColumn attributeColumnBetweenness = gephiGraphData.getAttributeTable().getColumn(GraphDistance.BETWEENNESS);
		AttributeColumn attributeColumnCloseness   = gephiGraphData.getAttributeTable().getColumn(GraphDistance.CLOSENESS);
		AttributeColumn attributeColumnEigenvector = gephiGraphData.getAttributeTable().getColumn(EigenvectorCentrality.EIGENVECTOR);
		for(Node gephiNode: gephiGraphData.getGephiGraph().getNodes()) {
			nodeId = gephiNode.getNodeData().getId();
			betweennessValue  = (Double)gephiNode.getNodeData().getAttributes().getValue(attributeColumnBetweenness.getIndex());
			closenessValue    = (Double)gephiNode.getNodeData().getAttributes().getValue(attributeColumnCloseness.getIndex());
			eigenvectorValue  = (Double)gephiNode.getNodeData().getAttributes().getValue(attributeColumnEigenvector.getIndex());
			// put the values in NodeData by NodeTableHash
			nodeData = this.nodesTableHash.get(nodeId);
			nodeData.setBetweenness(betweennessValue);
			nodeData.setCloseness(closenessValue);
			nodeData.setEigenvector(eigenvectorValue);
		}	
	}
	
	public void sortMeasuresWholeNetwork() {
		this.betweennessSortTable = this.nodesTableArray.sortBetwennness();
		this.closenessSortTable   = this.nodesTableArray.sortCloseness();
		this.eigenvectorSortTable = this.nodesTableArray.sortEigenvector(); 
	}
	
	// clasify connected components and create rank of MeasuresRanks objects
	public void classifyConnectedComponent_BuildSubGraph() throws Exception {
		ConnectedComponents connectedComponents = new ConnectedComponents();
		connectedComponents.execute(this.gephiGraphData.getGraphModel(), this.gephiGraphData.getAttributeModel());
		this.connectedComponentsCount = connectedComponents.getConnectedComponentsCount();
		AttributeColumn attributeColumn = gephiGraphData.getAttributeModel().getNodeTable().getColumn(ConnectedComponents.WEAKLY);

		// create a array of the MeasureRank (Ranks object), size = total number of the connect components
		this.ranks = new Ranks(this.connectedComponentsCount);

		String nodeId;
		NodeData nodeData;
		int connectedComponentNumber;
			
		for(Node gephiNode: this.gephiGraphData.getGephiGraph().getNodes()) {
			// get the number of connected component from Gephi
			nodeId = gephiNode.getNodeData().getId();
			connectedComponentNumber = (Integer)gephiNode.getNodeData().getAttributes().getValue(attributeColumn.getIndex());
			
			// put the number of connected component in NodeData
			nodeData = this.nodesTableHash.get(nodeId);
			nodeData.setConnectedComponent(connectedComponentNumber);
			
			// create a GephiNode object (from current Gephi node) and put it into the GephiGraphData object (belongs to MeasureRank object), by connected component number
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

	public void buildBasicTableSubGraph() throws Exception {
		org.gephi.graph.api.Graph currentGephiGraph;
		int connectedComponentNodesQuantity;
		NodesTableArray newBasicTable;
		NodeData nodeData;
		// for each group of the connected components, build the NodesTableArray:
		for(int i=0; i < this.ranks.getCount(); i++) {
			currentGephiGraph = this.ranks.getMeasuresRankTable(i).getGephiGraphData().getGephiGraph();
			connectedComponentNodesQuantity = currentGephiGraph.getNodeCount();
			// create the Basic Table Array
			newBasicTable = new NodesTableArray(connectedComponentNodesQuantity);
			for(Node gephiNode: currentGephiGraph.getNodes()) {
				nodeData = this.nodesTableHash.get(gephiNode.toString());
				newBasicTable.insert(nodeData);
			}
			this.ranks.getMeasuresRankTable(i).setBasicTable(newBasicTable);
		}
		// I gave up calculate measures to each sub-graph, but to use the measures of the whole graph (sorted) 
	}
	
	public void sortConnectecComponentRanks() throws Exception {
		NodesTableArray currentNodesTableArray, sortedNodesTableArray;
		int originalQuantity;
		// for each group of the connected components, build the sorted NodesTableArray:
		for(int i=0; i < this.ranks.getCount(); i++) {
			// calculate quantity of original nodes and store the result
			originalQuantity = this.ranks.getMeasuresRankTable(i).getBasicTable().calculateOriginalQuantity();
			this.ranks.getMeasuresRankTable(i).setOriginalQuantity(originalQuantity);
			
			// create each new sort table with measures from BasicTableArray
			currentNodesTableArray = this.ranks.getMeasuresRankTable(i).getBasicTable();
			
			sortedNodesTableArray  = currentNodesTableArray.sortBetwennness();
			this.ranks.getMeasuresRankTable(i).setBetweenness(sortedNodesTableArray);
			
			sortedNodesTableArray  = currentNodesTableArray.sortCloseness();
			this.ranks.getMeasuresRankTable(i).setCloseness(sortedNodesTableArray);
			
			sortedNodesTableArray  = currentNodesTableArray.sortEigenvector();
			this.ranks.getMeasuresRankTable(i).setEigenvector(sortedNodesTableArray);
			
			int filterQuantity = (int)(this.getOriginalQuantity() * Constants.proporcionBetweenness);
			sortedNodesTableArray  = currentNodesTableArray.sortBetweennessCloseness(filterQuantity);
			this.ranks.getMeasuresRankTable(i).setBetweennessCloseness(sortedNodesTableArray);	
		}
	}
	
	public void analyseGraphData() {
		
	}
	
	public String toString() {
		return  "Stream Graph Data: \n" + this.getStreamGraphData().toString() +
				//this.getGephiGraphData().toString() +
				//this.getRanks().toString() +
				"\nQuantity connected component: " + this.connectedComponentsCount +
		        "\n\n=================================\nTable array: \n---------------------------------\n" + 
				this.nodesTableArray.toString() +
		        "\n\n=================================\nTable array (betweenness sorted):\n---------------------------------\n" + 
				this.betweennessSortTable.toString() +
		        "\n\n=================================\nTable array (closeness sorted): \n---------------------------------\n" + 
				this.closenessSortTable.toString() +
		        "\n\n=================================\nTable array (eingenvector sorted): \n---------------------------------\n" + 
				this.eigenvectorSortTable.toString();		
	}
}