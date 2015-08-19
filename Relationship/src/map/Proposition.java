package map;

import graph.NodeData;

public class Proposition {
	private String   sourceConcept;
	private String   targetConcept;
	private NodeData sourceNodeData;
	private NodeData targetNodeData;
	private String   link;
	
	public Proposition(NodeData sourceConcept, String link, NodeData targetConcept) {
		this.sourceConcept  = sourceConcept.getShortName();
		this.targetConcept  = targetConcept.getShortName();
		this.sourceNodeData = sourceConcept;
		this.targetNodeData = targetConcept;
		this.link           = link;
	}
	
	public String getLink() {
		return this.link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getSourceConcept() {
		return this.sourceConcept;
	}
	public void setSourceConcept(String str) {
		this.sourceConcept = str;
	}
	public String getTargetConcept() {
		return this.targetConcept;
	}
	public void setTargetConcept(String str) {
		this.targetConcept = str;
	}
	public NodeData getSourceNodeData() {
		return this.sourceNodeData;
	}
	public NodeData getTargetNodeData() {
		return this.targetNodeData;
	}
	
	public String toString() {
		return this.sourceConcept + " -> " + this.link + " -> " + this.targetConcept;
	}
}
