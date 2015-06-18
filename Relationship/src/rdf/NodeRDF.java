package rdf;

import main.Config;
import main.WholeSystem;

public class NodeRDF extends ItemRDF {
	private Config.Level status;   
	
	public NodeRDF(String value) {
		super(value);
		this.status = WholeSystem.isOriginalConcept(this.getShortName());
	}
	public Config.Level getStatus() {
		return this.status;
	}
	
	// verify if the node belongs to original concepts list
	private Config.Level qualifyStatus(SetQuerySparql setQuerySparql) {
		for(QuerySparql x: setQuerySparql.getListQuerySparql()) {
			if(Config.ignoreCaseConcept) {
				if(this.getShortName().equalsIgnoreCase(x.getConcept().getUnderlineConcept())) 
					return Config.Level.originalConcept;
			}
			else {
				if(this.getShortName().equals(x.getConcept().getUnderlineConcept())) 
					return Config.Level.originalConcept;
			}
		}
		return Config.Level.commonConcept;
	}
	
	public String toString() {
		StringBuffer out = new StringBuffer();
		out.append(super.toString());
		if(this.status == Config.Level.originalConcept) 
			out.append(" - \"original concept\"");
		return out.toString();
	}

}
