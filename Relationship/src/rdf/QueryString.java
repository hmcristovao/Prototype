package rdf;

import user.Concept;

public class QueryString {
	
	private StringBuffer queryStr;
	
	public QueryString() {
		this.queryStr = new StringBuffer();
	}
	public QueryString(StringBuffer queryStr) {
		this.queryStr = queryStr;
	}
	public QueryString(Concept concept) {
		this.queryStr = this.buildQuery(concept);
	}
	public StringBuffer buildQuery(Concept concept) {
		StringBuffer aux = new StringBuffer();
		return aux;
	}
	public StringBuffer getQueryStr() {
		return this.queryStr;
	}
	public String getQueryStrString() {
		return this.queryStr.toString();
	}
	public void append(String str) {
		this.queryStr.append(str);
		return;
	}
	public void appendLine(String str) {
		this.queryStr.append("\n");
		this.queryStr.append(str);
		return;
	}
	@Override
	public String toString() {
		return this.getQueryStr().toString();
	}
}
