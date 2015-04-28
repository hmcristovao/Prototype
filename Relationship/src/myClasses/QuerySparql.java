    package myClasses;

import com.hp.hpl.jena.rdf.model.Model;

public class QuerySparql {
	
	private Concept concept;
	private QueryString queryString;
	private ListRDF listRDF;
	private Model model;
	
	public QuerySparql(Concept concept, QueryString queryString, ListRDF listRDF) {
		this.concept = concept;
		this.queryString = queryString;
		this.listRDF = listRDF;
	}
	
	public QuerySparql(Concept concept) {
		this.concept = concept;
		this.queryString = new QueryString(concept);
		this.listRDF = new ListRDF();
	}

	public Concept getConcept() {
		return this.concept;
	}
	public QueryString getQueryString() {
		return this.queryString;
	}
	public ListRDF getListRDF() {
		return this.listRDF;
	}
	public Model getModel() {
		return this.model;
	}

	public void setQuery(QueryString queryString) {
		this.queryString = queryString;
	}

	@Override
	public String toString() {
		return "QuerySparql [concept=" + 
				this.getConcept() + ", Query=" + 
				this.getQueryString() + ", listRDF=" + 
				this.getListRDF() + "]";
	}

	

}
