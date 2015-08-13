package graph;

import java.util.Arrays;
import java.util.Comparator;

import user.Concept;
import user.GroupConcept;
import main.*;

public class NodesTableArray {
	private NodeData table[]; 
	private int count;
	private int max;
	
	public NodesTableArray(NodeData table[]) {
		this.table   = table;
		this.max = this.count = table.length;
	}
	public NodesTableArray(int maxQuantity) {
		this.table   = new NodeData[maxQuantity];
		this.count = 0;
		this.max   = maxQuantity;
	}
	
	public int getCount() {
			return this.max;
	}
	
	public void insert(NodeData nodeData) throws Exception {
		if(this.count == this.max)
			throw new Exception("Quantity of nodes is larger than capacity");
		table[count] = nodeData;
		this.count++;
	}
	public NodeData getNodeData(int position) throws Exception {
		if(position >= this.max)
			throw new Exception("Tried to read NodeData over the table");
		return this.table[position];
	}
	
	public int calculateOriginalConceptQuantity() {
		int originalQuantity = 0;
		for(int i=0; i<this.max; i++) {
			if(this.table[i].getStatus() == Config.Status.originalConcept)
			   originalQuantity++;
		}
		return originalQuantity;
	}
	public GroupConcept getGroupOriginalConcepts() {
		GroupConcept groupConcept = new GroupConcept();
		Concept concept;
		for(int i=0; i<this.max; i++) {
			if(this.table[i].getStatus() == Config.Status.originalConcept) {
				concept = new Concept(this.table[i].getStrIdNode(), this.table[i].getShortName(), this.table[i].getStatus(), 0, Config.withoutConnectedComponent);  
				groupConcept.add(concept);
			}
		}
		return groupConcept;
	}
	
	
	
	public NodesTableArray sortBetwennness() {
		NodeData newTable[] = Arrays.copyOf(this.table, this.table.length);
		Arrays.sort(newTable, new SortBetweenness());
		return new NodesTableArray(newTable);
	}
	public NodesTableArray sortBetwennness(int first) {
		if(first > this.max)
			first = this.max;
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
		if(first > this.max)
			first = this.max;
		NodeData newTable[] = Arrays.copyOf(this.table, first);
		Arrays.sort(newTable, new SortCloseness());
		return new NodesTableArray(newTable);
	}
	public NodesTableArray sortEigenvector() {
		NodeData newTable[] = Arrays.copyOf(this.table, this.table.length);
		Arrays.sort(newTable, new SortEigenvector());
		return new NodesTableArray(newTable);
	}
	public NodesTableArray sortEigenvector(int first) {
		if(first > this.max)
			first = this.max;
		NodeData newTable[] = Arrays.copyOf(this.table, first);
		Arrays.sort(newTable, new SortEigenvector());
		return new NodesTableArray(newTable);
	}
	public NodesTableArray sortCrescentEigenvector() {
		NodeData newTable[] = Arrays.copyOf(this.table, this.table.length);
		Arrays.sort(newTable, new SortCrescentEigenvector());
		return new NodesTableArray(newTable);
	}
	public NodesTableArray sortBetweennessCloseness(int first) {
		if(first > this.max)
			first = this.max;
		NodeData newTable[] = Arrays.copyOf(this.table, this.table.length);
		Arrays.sort(newTable, new SortBetweenness());
		NodeData newNewTable[] = Arrays.copyOf(newTable, first);
		return new NodesTableArray(newNewTable);
	}

	public String toStringShort(int quantityNodes) {
		StringBuffer str = new StringBuffer();
		for(int i=0; i<quantityNodes && i<this.count; i++) {
			str.append(this.table[i].toString());
			str.append("\n\n");
		}
		return  str.toString();
	}
	
	public String toString() {
		StringBuffer str = new StringBuffer();
		for(int i=0; i<this.count; i++) {
			str.append(this.table[i].toString());
			str.append("\n\n");
		}
		return str.toString();
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
class SortCrescentEigenvector implements Comparator<NodeData> {
	public int compare(NodeData nodeData1, NodeData nodeData2) {
		if(nodeData1.getEigenvector() > nodeData2.getEigenvector())
			return 1;
		else if(nodeData1.getEigenvector() < nodeData2.getEigenvector())
			return -1;
		else
			return 0;
	}
}
