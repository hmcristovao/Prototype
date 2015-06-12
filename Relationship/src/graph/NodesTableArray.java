package graph;

import java.util.Arrays;
import java.util.Comparator;

import main.*;

public class NodesTableArray {
	private NodeData table[]; 
	private int current;
	private int count;
	
	public NodesTableArray(NodeData table[]) {
		this.table   = table;
		this.count   = table.length;
		this.current = table.length;
	}
	
	public NodesTableArray(int maxQuantity) {
		this.table   = new NodeData[maxQuantity];
		this.current = 0;
		this.count   = maxQuantity;
	}

	public int getCount() {
		return this.count;
	}
	
	public void insert(NodeData nodeData) throws Exception {
		if(this.current == this.count)
			throw new Exception("Quantity of nodes is larger than capacity");
		table[current] = nodeData;
		this.current++;
	}
	public NodeData getNodeData(int position) throws Exception {
		if(position >= this.count)
			throw new Exception("Tried to read NodeData over the table");
		return this.table[position];
	}
	
	public int calculateOriginalQuantity() {
		int originalQuantity = 0;
		for(int i=0; i<this.count; i++) {
			if(this.table[i].getStatus() == Constants.Level.originalConcept)
			   originalQuantity++;
		}
		return originalQuantity;
	}
	
	
	public NodesTableArray sortBetwennness() {
		NodeData newTable[] = Arrays.copyOf(this.table, this.table.length);
		Arrays.sort(newTable, new SortBetweenness());
		return new NodesTableArray(newTable);
	}
	public NodesTableArray sortBetwennness(int first) {
		if(first > this.count)
			first = this.count;
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
		if(first > this.count)
			first = this.count;
		NodeData newTable[] = Arrays.copyOf(this.table, first);
		Arrays.sort(newTable, new SortCloseness());
		return new NodesTableArray(newTable);
	}
	public NodesTableArray sortEigenvector() {
		NodeData newTable[] = Arrays.copyOf(this.table, this.table.length);
		Arrays.sort(newTable, new SortEigenvector());
		return new NodesTableArray(newTable);
	}
	public NodesTableArray sortEingenvector(int first) {
		if(first > this.count)
			first = this.count;
		NodeData newTable[] = Arrays.copyOf(this.table, first);
		Arrays.sort(newTable, new SortEigenvector());
		return new NodesTableArray(newTable);
	}
	public NodesTableArray sortBetweennessCloseness(int first) {
		if(first > this.count)
			first = this.count;
		NodeData newTable[] = Arrays.copyOf(this.table, this.table.length);
		Arrays.sort(newTable, new SortBetweenness());
		NodeData newNewTable[] = Arrays.copyOf(newTable, first);
		return new NodesTableArray(newNewTable);
	}

	
	public String toString() {
		StringBuffer str = new StringBuffer();
		for(int i=0; i<this.current; i++) {
			str.append(this.table[i].toString());
			str.append("\n\n");
		}
		return  str.toString();
	}
}

class SortBetweenness implements Comparator<NodeData> {
	public int compare(NodeData nodeData1, NodeData nodeData2) {
		if(nodeData1.getBetweenness() < nodeData2.getBetweenness())
			return 1;
		else if(nodeData1.getBetweenness() > nodeData2.getBetweenness())
			return -1;
		else
			return 0;
	}
}
class SortCloseness implements Comparator<NodeData> {
	public int compare(NodeData nodeData1, NodeData nodeData2) {
		if(nodeData1.getCloseness() < nodeData2.getCloseness())
			return 1;
		else if(nodeData1.getCloseness() > nodeData2.getCloseness())
			return -1;
		else
			return 0;
	}
}
class SortEigenvector implements Comparator<NodeData> {
	public int compare(NodeData nodeData1, NodeData nodeData2) {
		if(nodeData1.getEigenvector() < nodeData2.getEigenvector())
			return 1;
		else if(nodeData1.getEigenvector() > nodeData2.getEigenvector())
			return -1;
		else
			return 0;
	}
}
