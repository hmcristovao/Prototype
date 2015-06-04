package myClasses;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.statistics.plugin.GraphDistance;

public class NodesTableHash {

	private Map<String,NodeData> table; 
	
	public NodesTableHash() {
		this.table = new HashMap<String, NodeData>();
	}
	
	public NodeData get(String nodeId) {
		return this.get(nodeId);
	}
	public void put(String nodeId, NodeData nodeData) {
		this.table.put(nodeId, nodeData);
	}

	public void putBetweenness(String nodeId, double valueBetweenness) {
		this.table.get(nodeId).setBetweenness(valueBetweenness);
	}
	public void putCloseness(String nodeId, double valueCloseness) {
		this.table.get(nodeId).setCloseness(valueCloseness);
	}
	public void putEingenvector(String nodeId, double valueEingenvector) {
		this.table.get(nodeId).setEingenvector(valueEingenvector);
	}
	
	public void buildNodesTableHash(GephiGraphData gephiGraphData) {
		Double valueBetweenness, valueCloseness;
		String nodeId;
		// copy betweenness and closeness to table
		AttributeColumn attributeColumnBetweenness = gephiGraphData.getAttributeTable().getColumn(GraphDistance.BETWEENNESS);
		AttributeColumn attributeColumnCloseness   = gephiGraphData.getAttributeTable().getColumn(GraphDistance.CLOSENESS);
		for(Node gephiNode: gephiGraphData.getGephiGraph().getNodes()) {
			nodeId = gephiNode.getNodeData().getId();
			valueBetweenness = (Double)gephiNode.getNodeData().getAttributes().getValue(attributeColumnBetweenness.getIndex());
			this.putBetweenness(nodeId, valueBetweenness.doubleValue());
			valueCloseness   = (Double)gephiNode.getNodeData().getAttributes().getValue(attributeColumnCloseness.getIndex());
			this.putCloseness(nodeId, valueCloseness.doubleValue());
		}	
	}
	
	public String toString() {
		return  this.table.toString();
	}
}


