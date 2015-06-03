package myClasses;

import java.util.Arrays;
import java.util.Comparator;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.statistics.plugin.GraphDistance;

public class NodesTable {

	private NodeData table[]; 
	private int current;
	private int maxQuantity;
	
	public NodesTable(NodeData table[]) {
		this.table = table;
		this.maxQuantity = table.length;
		this.current = table.length;
	}
	public NodesTable(int maxQuantity) {
		this.table = new NodeData[maxQuantity];
		this.current = 0;
		this.maxQuantity = maxQuantity;
	}

	public void buildNodesTable(GraphModel graphModel, AttributeModel attributeModel) {
		GraphDistance graphDistance = new GraphDistance();
		graphDistance.setDirected(false);
		graphDistance.setNormalized(true);
		graphDistance.execute(graphModel, attributeModel);
		
		AttributeColumn attributeColumn = this.attributeModel.getNodeTable().getColumn(GraphDistance.CLOSENESS);
		
		// coloca o atributo no Stream ...
		for(Node gephiNode: this.gephiGraph.getNodes()) {
			Double closeness = (Double)gephiNode.getNodeData().getAttributes().getValue(attributeColumn.getIndex());
			System.out.println(closeness);
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
	
	public NodesTable sortBetwennness() {
		NodeData newTable[] = Arrays.copyOf(this.table, this.table.length);
		Arrays.sort(newTable, new SortBetweenness());
		return new NodesTable(newTable);
	}
	public NodesTable sortBetwennness(int first) {
		if(first > this.maxQuantity)
			first = this.maxQuantity;
		NodeData newTable[] = Arrays.copyOf(this.table, first);
		Arrays.sort(newTable, new SortBetweenness());
		return new NodesTable(newTable);
	}
	public NodesTable sortCloseness() {
		NodeData newTable[] = Arrays.copyOf(this.table, this.table.length);
		Arrays.sort(newTable, new SortCloseness());
		return new NodesTable(newTable);
	}
	public NodesTable sortCloseness(int first) {
		if(first > this.maxQuantity)
			first = this.maxQuantity;
		NodeData newTable[] = Arrays.copyOf(this.table, first);
		Arrays.sort(newTable, new SortCloseness());
		return new NodesTable(newTable);
	}
	public NodesTable sortEingenvector() {
		NodeData newTable[] = Arrays.copyOf(this.table, this.table.length);
		Arrays.sort(newTable, new SortEingenvector());
		return new NodesTable(newTable);
	}
	public NodesTable sortEingenvector(int first) {
		if(first > this.maxQuantity)
			first = this.maxQuantity;
		NodeData newTable[] = Arrays.copyOf(this.table, first);
		Arrays.sort(newTable, new SortEingenvector());
		return new NodesTable(newTable);
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
