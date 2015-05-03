package myClasses;

public class QueryString {
	
	private StringBuffer value;
	
	public QueryString() {
		this.value = new StringBuffer();
	}
	public QueryString(StringBuffer value) {
		this.value = value;
	}
	public QueryString(Concept concept) {
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
