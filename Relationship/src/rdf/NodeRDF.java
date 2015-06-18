package rdf;

import user.Concept;
import main.Config;
import main.WholeSystem;

public class NodeRDF extends ItemRDF {
	private Config.Status status;   
	
	public NodeRDF(String value) {
		super(value);
		if(WholeSystem.isOriginalConcept(Concept.underlineToBlank(this.getShortName())))
			this.status = Config.Status.originalConcept;
		else
			this.status = Config.Status.commonConcept;
	}
	public Config.Status getStatus() {
		return this.status;
	}
	
	public String toString() {
		StringBuffer out = new StringBuffer();
		out.append(super.toString());
		if(this.status == Config.Status.originalConcept) 
			out.append(" - \"original concept\"");
		return out.toString();
	}

}
