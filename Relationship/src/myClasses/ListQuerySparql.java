package myClasses;

import java.util.LinkedList;

public class ListQuerySparql {
	private LinkedList<QuerySparql> list;
	
	// private formatoGephi;
	// private dados para o gráfico ??
	// private metricas ...
	
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
		Query auxQuery = new Query();
		ListRDF auxListRDF = new ListRDF();
		QuerySparql querySparql = new QuerySparql(concept, auxQuery, auxListRDF);
		this.list.add(querySparql);  
	}
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
		return "ListQuerySparql [list=\n" + this.getList() + "]";
	}
}
