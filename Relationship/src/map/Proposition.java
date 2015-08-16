package map;

import graph.NodeData;

public class Proposition {
	NodeData sourceConcept;
	NodeData targetConcept;
	String   link;
	
	public Proposition(NodeData sourceConcept, String link, NodeData targetConcept) {
		this.sourceConcept  = sourceConcept;
		this.targetConcept = targetConcept;
		this.link          = link;
	}
	
	public String toString() {
		return this.sourceConcept.getShortName() + " -> " + this.link + " -> " + this.targetConcept.getShortName();
	}
}
