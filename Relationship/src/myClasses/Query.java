package myClasses;

public class Query {
	
	private StringBuffer value;
	
	public Query() {
		this.value = new StringBuffer();
	}
	public Query(StringBuffer value) {
		this.value = value;
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
