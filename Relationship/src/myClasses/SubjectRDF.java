package myClasses;

import com.hp.hpl.jena.rdf.model.Resource;

public class SubjectRDF {
	
	private String value;
	private Resource resource;
	
	public SubjectRDF(String value) {
		this.value = value;
	}
	public String getValue() {
		return this.value;
	}
	public Resource getResource() {
		return this.resource;
	}
	
	@Override
	public String toString() {
		return this.getValue();
	}

}
