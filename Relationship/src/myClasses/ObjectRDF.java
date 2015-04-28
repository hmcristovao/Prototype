package myClasses;

import com.hp.hpl.jena.rdf.model.RDFNode;

public class ObjectRDF {
	
	private String value;
	private RDFNode rdfNode;
	
	public ObjectRDF(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
	public RDFNode getRdfNode() {
		return this.rdfNode;
	}

	
	@Override
	public String toString() {
		return this.getValue();
	}
}
