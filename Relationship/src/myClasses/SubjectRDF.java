package myClasses;

public class SubjectRDF {
	
	private String value;
	
	public SubjectRDF(String value) {
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
