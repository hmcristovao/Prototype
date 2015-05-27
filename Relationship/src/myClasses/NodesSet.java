package myClasses;

import java.util.LinkedList;

public class NodesSet {
	private LinkedList<IdNode> nodes;
	
	public NodesSet() {
		this.nodes = new LinkedList<IdNode>();
	}
	
	public LinkedList<IdNode> getSetNode() {
		return this.nodes;
	}
	
	public void add(IdNode idNode) {
		this.nodes.add(idNode);
	}

	public void add(String strIdNode, double value, StreamGraphData streamGraphData, GephiGraphData gephiGraphData) {
		IdNode idNode = new IdNode(strIdNode, streamGraphData, gephiGraphData);
		this.add(idNode);
	}
	
	public String toString() {
		return this.nodes.toString();
	}
}
