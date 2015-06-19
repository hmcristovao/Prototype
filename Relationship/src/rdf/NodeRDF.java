package rdf;

import user.Concept;
import main.Config;
import main.WholeSystem;

public class NodeRDF extends ItemRDF {
	private Config.Status status;   
	
	public NodeRDF(String value, SetQuerySparql setQuerySparql) {
		super(value);
		String shortNameWithoutUnderline = Concept.underlineToBlank(this.getShortName());
		if(WholeSystem.isOriginalConcept(shortNameWithoutUnderline))
			this.status = Config.Status.originalConcept;
		// if is it not original, then verify if node RDF is current concept:
		else if(setQuerySparql.isCurrentConcept(shortNameWithoutUnderline))
			this.status =  Config.Status.selected; 
		else
			this.status = Config.Status.commonConcept;
	}
	public Config.Status getStatus() {
		return this.status;
	}
	
	public String toString() {
		return super.toString() + " - (" + Concept.statusToString(this.status) + ")";
	}

}
