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
	// cria um objeto QuerySparql preenchendo-o com concept e o insere na lista
	public void insert(Concept concept) {
		QueryString auxQuery = new QueryString();
		ListRDF auxListRDF = new ListRDF();
		QuerySparql querySparql = new QuerySparql(concept, auxQuery, auxListRDF);
		this.list.add(querySparql);  
	}
	
	// exceptional function
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
		return "\nlist = " + this.getList().toString();
	}
}
