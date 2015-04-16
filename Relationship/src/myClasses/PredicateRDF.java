package myClasses;

public class PredicateRDF {
	
	private String value;
	
	public PredicateRDF(String value) {
		this.value = value;
	}
	public String getValue() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return this.getValue();
	}

}
