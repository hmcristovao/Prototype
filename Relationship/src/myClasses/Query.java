package myClasses;

public class Query implements ConstSparql {
	
	private StringBuffer value;
	
	public Query() {
		this.value = new StringBuffer();
	}
	public Query(StringBuffer value) {
		this.value = value;
	}
	public Query(Concept concept) {
		this.value = this.buildQuery(concept);
	}
	
	public StringBuffer buildQuery(Concept concept) {
		StringBuffer aux = new StringBuffer();
		
		return aux;
	}
	
	
	public StringBuffer getValue() {
		return this.value;
	}
	public String getValueString() {
		return this.value.toString();
	}
	public void append(String str) {
		this.value.append(str);
		return;
	}
	public void appendLine(String str) {
		this.value.append("\n");
		this.value.append(str);
		return;
	}
	@Override
	public String toString() {
		return this.getValue().toString();
	}
}
