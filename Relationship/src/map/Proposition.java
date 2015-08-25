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

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Proposition))
			return false;
		Proposition other = (Proposition) obj;
		if (link == null) {
			if (other.link != null)
				return false;
		} else if (!link.equals(other.link))
			return false;
		if (sourceConcept == null) {
			if (other.sourceConcept != null)
				return false;
		} else if (!sourceConcept.equals(other.sourceConcept))
			return false;
		if (sourceNodeData == null) {
			if (other.sourceNodeData != null)
				return false;
		} else if (!sourceNodeData.equals(other.sourceNodeData))
			return false;
		if (targetConcept == null) {
			if (other.targetConcept != null)
				return false;
		} else if (!targetConcept.equals(other.targetConcept))
			return false;
		if (targetNodeData == null) {
			if (other.targetNodeData != null)
				return false;
		} else if (!targetNodeData.equals(other.targetNodeData))
			return false;
		return true;
	}

	public String toString() {
		return this.sourceConcept + " -> " + this.link + " -> " + this.targetConcept;
	}
}
