package myClasses;

import org.gephi.data.attributes.api.AttributeTable;

public class SystemGraphData {

	private StreamGraphData streamGraphData;
	private GephiGraphData gephiGraphData;
	private AttributeTable attributeTable;
	private Ranks ranks;
	private NodesSet nodesSet;
		
	public SystemGraphData() {
		this.streamGraphData = new StreamGraphData(); 
		this.gephiGraphData  = new GephiGraphData();
		this.attributeTable  = null;
		this.ranks           = new Ranks();
		this.nodesSet        = new NodesSet(); 
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
	
	public String toString() {
		return  this.getStreamGraphData().toStringGraph() +
				this.getGephiGraphData().toString() +
				this.getAttributeTable().toString() +
				this.getRanks().toString() +
				this.getNodesSet().toString();
	}
}
