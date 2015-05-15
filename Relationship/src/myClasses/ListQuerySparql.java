package myClasses;

import java.util.LinkedList;

public class ListQuerySparql {
	private LinkedList<QuerySparql> list;

	public ListQuerySparql() {
		this.list = new LinkedList<QuerySparql>();
	}
	
	public LinkedList<QuerySparql> getList() {
		return this.list;
	}
	public void insert(QuerySparql querySparql) {
		this.list.add(querySparql);  
	}
	// create a object QuerySparql, fill it with concept, and insert it into list
	public void insert(Concept concept) {
		QueryString auxQuery = new QueryString();
		ListRDF auxListRDF = new ListRDF();
		QuerySparql querySparql = new QuerySparql(concept, auxQuery, auxListRDF);
		this.list.add(querySparql);  
	}
	
	// exceptional function - must be deleted
	public String getListConcept() {
		StringBuffer out = new StringBuffer();
		for(QuerySparql x: this.list) {
			out.append(x.getConcept().toString());
			out.append("\n");
		}
		return out.toString();
	}
	
	@Override
	public String toString() {
		StringBuffer out = new StringBuffer();
		int n = 1;
		for(QuerySparql x: this.list) {
			out.append("\n***** Concept number ");
			out.append(n++);
			out.append(" *****\n");
			out.append(x.toString());
			out.append("\n");
		}
		return out.toString();
	}
}
