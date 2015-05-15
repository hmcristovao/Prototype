package myClasses;

import com.hp.hpl.jena.rdf.model.Resource;

public class SubjectRDF extends NodeRDF {
	private Resource resource;
	
	public SubjectRDF(String value, Resource resource, int level) {
		super(value, level);
		this.resource = resource;
	}
	public Resource getResource() {
		return this.resource;
	}
}
