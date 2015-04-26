package myClasses;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class ListQuerySparql implements ConstListQuerySparql {
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
		QueryString auxQuery = new QueryString();
		ListRDF auxListRDF = new ListRDF();
		QuerySparql querySparql = new QuerySparql(concept, auxQuery, auxListRDF);
		this.list.add(querySparql);  
	}
	
	// exceptional function - for test
	public String getListConcept() {
		StringBuffer out = new StringBuffer();
		for(QuerySparql x: this.list) {
			out.append(x.getConcept().toString());
			out.append("\n");
		}
		return out.toString();
	}

	private StringBuffer readFileQueryDefault() throws IOException {
		BufferedReader fileQueryDefault = new BufferedReader(new FileReader(ListQuerySparql.nameFileQueryDefault));
		StringBuffer queryDefault = new StringBuffer();
		String linhaAux = null;
	    while (true) {
	       linhaAux = fileQueryDefault.readLine();
	       if(linhaAux == null)
	    	   break;
	       queryDefault.append(linhaAux);
	       queryDefault.append("\n");
	    }
        fileQueryDefault.close();
        return queryDefault;
	}
	
	// replace and make a copy of the query
	private StringBuffer replaceQueryDefault(StringBuffer queryDefault, String concept) {
		StringBuffer newQueryDefault = new StringBuffer(queryDefault);
		int start = 0;
		while( (start = newQueryDefault.indexOf(":Concept", start)) != -1)
		   newQueryDefault.replace(start, start+8, concept);
		return newQueryDefault;
	}
	
	public void fillQuery() throws IOException {	
	    StringBuffer queryDefault = this.readFileQueryDefault();
	    StringBuffer newQueryDefault = null;
	    String newConcept = null;
	    QueryString queryString = null;
		for(QuerySparql x: this.list) {
			newConcept = x.getConcept().getFormatedConcept();
			newQueryDefault = this.replaceQueryDefault(queryDefault, newConcept);
			queryString = new QueryString(newQueryDefault);
			x.setQuery(queryString);
		}
	}
	
	
	@Override
	public String toString() {
		return "ListQuerySparql [list=\n" + this.getList() + "]";
	}
}
