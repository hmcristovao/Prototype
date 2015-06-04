package myClasses;

import java.util.Arrays;
import java.util.Comparator;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.statistics.plugin.GraphDistance;

public class NodesTableArray {
	private NodeData table[]; 
	private int current;
	private int maxQuantity;
	
	/*public NodesTableArray(NodeData table[]) {
		this.table = table;
		this.maxQuantity = table.length;
		this.current = table.length;
	}
	*/
	
	public NodesTableArray(int maxQuantity) {
		this.table = new NodeData[maxQuantity];
		this.current = 0;
		this.maxQuantity = maxQuantity;
	}

	//private static void buildBasicTable(NodesTableHash nodesTableHash) {
		
	//}
	
	public void buildNodesTable(GephiGraphData gephiGraphData) {

		Double valueBetweenness, valueCloseness;
		String nodeId;
		// copy Betweenness to NodesTableArray
		AttributeColumn attributeColumnBetweenness = gephiGraphData.getAttributeTable().getColumn(GraphDistance.BETWEENNESS);
		AttributeColumn attributeColumnCloseness   = gephiGraphData.getAttributeTable().getColumn(GraphDistance.CLOSENESS);
		for(Node gephiNode: gephiGraphData.getGephiGraph().getNodes()) {
			nodeId = gephiNode.getNodeData().getId();
			valueBetweenness = (Double)gephiNode.getNodeData().getAttributes().getValue(attributeColumnBetweenness.getIndex());
			valueCloseness   = (Double)gephiNode.getNodeData().getAttributes().getValue(attributeColumnCloseness.getIndex());
			this.addBetweennessCloseness(nodeId, valueBetweenness, valueCloseness);
		}	
	}
	
	public void insert(NodeData nodeData) throws Exception {
		if(this.current == this.maxQuantity)
			throw new Exception("Quantity of nodes is larger than capacity");
		table[current] = nodeData;
		this.current++;
	}
	public NodeData getNodeData(int position) throws Exception {
		if(position >= this.maxQuantity)
			throw new Exception("Tried to read NodeData over the table");
		return this.table[position];
	}
	
	public NodesTableArray sortBetwennness() {
		NodeData newTable[] = Arrays.copyOf(this.table, this.table.length);
		Arrays.sort(newTable, new SortBetweenness());
		return new NodesTableArray(newTable);
	}
	public NodesTableArray sortBetwennness(int first) {
		if(first > this.maxQuantity)
			first = this.maxQuantity;
		NodeData newTable[] = Arrays.copyOf(this.table, first);
		Arrays.sort(newTable, new SortBetweenness());
		return new NodesTableArray(newTable);
	}
	public NodesTableArray sortCloseness() {
		NodeData newTable[] = Arrays.copyOf(this.table, this.table.length);
		Arrays.sort(newTable, new SortCloseness());
		return new NodesTableArray(newTable);
	}
	public NodesTableArray sortCloseness(int first) {
		if(first > this.maxQuantity)
			first = this.maxQuantity;
		NodeData newTable[] = Arrays.copyOf(this.table, first);
		Arrays.sort(newTable, new SortCloseness());
		return new NodesTableArray(newTable);
	}
	public NodesTableArray sortEingenvector() {
		NodeData newTable[] = Arrays.copyOf(this.table, this.table.length);
		Arrays.sort(newTable, new SortEingenvector());
		return new NodesTableArray(newTable);
	}
	public NodesTableArray sortEingenvector(int first) {
		if(first > this.maxQuantity)
			first = this.maxQuantity;
		NodeData newTable[] = Arrays.copyOf(this.table, first);
		Arrays.sort(newTable, new SortEingenvector());
		return new NodesTableArray(newTable);
	}
	
	public String toString() {
		return  Arrays.toString(this.table);
	}
}

class SortBetweenness implements Comparator<NodeData> {
	public int compare(NodeData nodeData1, NodeData nodeData2) {
		return (nodeData1.getBetweenness() > nodeData2.getBetweenness() ? -1 : 1);
	}
}
class SortCloseness implements Comparator<NodeData> {
	public int compare(NodeData nodeData1, NodeData nodeData2) {
		return (nodeData1.getCloseness() > nodeData2.getCloseness() ? -1 : 1);
	}
}
class SortEingenvector implements Comparator<NodeData> {
	public int compare(NodeData nodeData1, NodeData nodeData2) {
		return (nodeData1.getEingenvector() > nodeData2.getEingenvector() ? -1 : 1);
	}
}
