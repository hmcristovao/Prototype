package myClasses;

public class ObjectRDF {
	
	private String value;
	
	public ObjectRDF(String value) {
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
