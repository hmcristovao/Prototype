package myClasses;

import java.util.LinkedList;
import java.util.TreeMap;

public class RankNodes {
	private TreeMap<IdNode, Double> nodes;
	
	public RankNodes() {
		this.nodes = new TreeMap<IdNode, Double>();
	}
	
	public TreeMap<IdNode, Double> getSetNode() {
		return this.nodes;
	}
	
	public void add(IdNode idNode, double value) {
		Double valueDouble = new Double(value);
		this.nodes.put(idNode, valueDouble);
	}

	public void add(String strIdNode, double value, StreamGraphData streamGraphData, GephiGraphData gephiGraphData) {
		IdNode idNode = new IdNode(strIdNode, streamGraphData, gephiGraphData);
		this.add(idNode,  value);
	}
	
	public String toString() {
		return this.nodes.toString();
	}
}
