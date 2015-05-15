package myClasses;

import com.hp.hpl.jena.rdf.model.Property;

public class PredicateRDF extends ItemRDF {
	private Property property;

	public PredicateRDF(String value, Property property) {
		super(value);
		this.property = property;
	}
	public Property getProperty() {
		return this.property;
	}
}
