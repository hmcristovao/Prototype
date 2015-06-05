package myClasses;

import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Node;

public class SystemGraphData {

	private StreamGraphData streamGraphData;
	private GephiGraphData gephiGraphData;
	// private - file GEXF ...
	private NodesTableHash  nodesTableHash;
	private NodesTableArray nodesTableArray;
	private Ranks ranks;
	
	public SystemGraphData() {
		this(new StreamGraphData(), new GephiGraphData());
	}

	public SystemGraphData(StreamGraphData streamGraphData, GephiGraphData gephiGraphData) {
		this.streamGraphData = streamGraphData; 
		this.gephiGraphData  = gephiGraphData;
		this.nodesTableHash  = new NodesTableHash();
		this.nodesTableArray = null;  // will be to fill after nodeTableHash filling
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
	public Ranks getRanks() {
		return this.ranks;
	}
	public void setRanks(Ranks ranks) {
		this.ranks = ranks;
	}

	// copy all graph from Stream to Gephi format
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
			// add node in nodesTableArray
			this.nodesTableArray.insert(newNodeData);
			// add node in nodesTableHash
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
	
	public void buildSystemGraphData(SetQuerySparql setQuerySparql) throws Exception {
		Debug.err("Building Stream Graph Data...");
		this.streamGraphData.buildStreamGraphData(setQuerySparql);
		Debug.err("Building Gephi Graph Data...");
		this.buildGephiGraphData_NodesTableHash_NodesTableArray();
		Debug.err("Building Gephi Graph Table...");
		this.gephiGraphData.buildGephiGraphTable();
		Debug.err("Building Nodes Table graph...");
		this.nodesTableArray.buildNodesTable(this.gephiGraphData);
	}
	
	public void buildRanks() {
		
	}
	
	public void analyseGraphData() {
		
	}
	
	public String toString() {
		return  this.getStreamGraphData().toString() +
				this.getGephiGraphData().toString() +
				this.getRanks().toString();
	}


}
