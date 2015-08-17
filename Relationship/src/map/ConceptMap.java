package map;

import graph.NodeData;

import java.util.ArrayList;
import java.util.List;

import main.WholeSystem;

public class ConceptMap {
	private List<Proposition> propositions;

	public ConceptMap() {
		this.propositions = new ArrayList<Proposition>();		
	}
	
	public List<Proposition> getPropositions() {
		return this.propositions;
	}

	public int size() {
		return this.propositions.size();
	}
	
	public void insert(NodeData sourceConcept, String rawLink, NodeData targetConcept) {
		String formatedLink = WholeSystem.getVocabularyTable().get(rawLink);
		Proposition proposition = new Proposition( sourceConcept, formatedLink, targetConcept);
		this.propositions.add(proposition);
	}
	
	public String toString() {
		StringBuffer out = new StringBuffer();
		for(Proposition p : this.propositions) {
			out.append(p.toString());
			out.append("\n");
		}
		return out.toString();
	}
}
