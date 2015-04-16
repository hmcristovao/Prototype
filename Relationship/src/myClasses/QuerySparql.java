package myClasses;

public class QuerySparql {
	
	private Concept concept;
	private Query query;
	private ListRDF listRDF;
	
	public QuerySparql(Concept concept, Query query, ListRDF listRDF) {
		this.concept = concept;
		this.query = query;
		this.listRDF = listRDF;
	}
	
	public Concept getConcept() {
		return this.concept;
	}

	public Query getQuery() {
		return this.query;
	}

	public ListRDF getListRDF() {
		return this.listRDF;
	}

	@Override
	public String toString() {
		return "QuerySparql [concept=" + 
				this.getConcept() + ", Query=" + 
				this.getQuery() + ", listRDF=" + 
				this.getListRDF() + "]";
	}

	

}
