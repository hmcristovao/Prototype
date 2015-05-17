package myClasses;

import com.hp.hpl.jena.rdf.model.Resource;

public class SubjectRDF extends NodeRDF {
	private Resource resource;
	
	public SubjectRDF(String value, Resource resource, SetQuerySparql setQuerySparql) {
		super(value, setQuerySparql);
		this.resource = resource;
	}
	public Resource getResource() {
		return this.resource;
	}
}
