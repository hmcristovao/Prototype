package rdf;

import main.Constants;

public class NodeRDF extends ItemRDF {
	private Constants.Level status;   
	
	public NodeRDF(String value, SetQuerySparql setQuerySparql) {
		super(value);
		this.status = this.qualifyStatus(setQuerySparql);
	}
	public Constants.Level getStatus() {
		return this.status;
	}
	
	// verify if the node belongs to original concepts list
	private Constants.Level qualifyStatus(SetQuerySparql setQuerySparql) {
		for(QuerySparql x: setQuerySparql.getList()) {
			if(Constants.ignoreCaseConcept) {
				if(this.getShortName().equalsIgnoreCase(x.getConcept().getUnderlineConcept())) 
					return Constants.Level.originalConcept;
			}
			else {
				if(this.getShortName().equals(x.getConcept().getUnderlineConcept())) 
					return Constants.Level.originalConcept;
			}
		}
		return Constants.Level.commonConcept;
	}
	
	public String toString() {
		StringBuffer out = new StringBuffer();
		out.append(super.toString());
		if(this.status == Constants.Level.originalConcept) 
			out.append(" - \"original concept\"");
		return out.toString();
	}

}
