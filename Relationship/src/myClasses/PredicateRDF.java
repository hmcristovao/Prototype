package myClasses;

import com.hp.hpl.jena.rdf.model.Property;

public class PredicateRDF {
	
	private String value;
	private Property property;
	

	public PredicateRDF(String value) {
		this.value = value;
	}
	public String getValue() {
		return this.value;
	}
	public Property getProperty() {
		return this.property;
	}
	
	@Override
	public String toString() {
		return this.getValue();
	}

}
