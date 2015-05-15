package myClasses;

import com.hp.hpl.jena.rdf.model.RDFNode;

public class ObjectRDF extends NodeRDF {
	private RDFNode rdfNode;
	
	public ObjectRDF(String value, RDFNode rdfNode, int level) {
		super(value, level);
		this.rdfNode = rdfNode;
	}
	public RDFNode getRdfNode() {
		return this.rdfNode;
	}
}
