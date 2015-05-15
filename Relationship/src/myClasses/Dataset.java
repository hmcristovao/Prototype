package myClasses;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class Dataset {
	private ListQuerySparql listQuerySparql;
	private GraphData graphData;
	private int totalRDF;
    private QuantityNodesEdges added;
    private QuantityNodesEdges duplicate;
	
	public Dataset() {
		this.listQuerySparql = new ListQuerySparql();
		this.graphData = new GraphData();
		this.totalRDF  = 0;
		this.added = new QuantityNodesEdges();
		this.duplicate = new QuantityNodesEdges();
	}
	
	public ListQuerySparql getListQuerySparql() {
		return this.listQuerySparql;
	}
	public GraphData getGraph() {
		return this.graphData;
	}
	
	public int getTotalConcepts() {
		return this.listQuerySparql.getList().size();
	}
	
	public int getTotalRDFs() {
		return this.totalRDF;
	}
	public void incTotalRDFs() {
		this.totalRDF++;
	}
	public void incTotalRDFs(int value) {
		this.totalRDF += value;
	}
	public int getTotalNodes() {
		return this.added.getNumNodes();
	}
	public void incTotalNodes() {
		this.added.incNumNodes();
	}
	public void incTotalNodes(int value) {
		this.added.incNumNodes(value);
	}

	public int getTotalEdges() {
		return this.added.getNumEdges();
	}
	public void incTotalEdges() {
		this.added.incNumEdges();
	}
	public void incTotalEdges(int value) {
		this.added.incNumEdges(value);
	}

	public int getTotalNodesDuplicate() {
		return this.duplicate.getNumNodes();
	}
	public void incTotalNodesDuplicate() {
		this.duplicate.incNumNodes();
	}
	public void incTotalNodesDuplicate(int value) {
		this.duplicate.incNumNodes(value);
	}

	public int getTotalEdgesDuplicate() {
		return this.duplicate.getNumEdges();
	}
	public void incTotalEdgesDuplicate() {
		this.duplicate.incNumEdges();
	}
	public void incTotalEdgesDuplicate(int value) {
		this.duplicate.incNumEdges(value);
	}

	private StringBuffer readFileQueryDefault() throws IOException {
		BufferedReader fileQueryDefault = new BufferedReader(new FileReader(Constants.nameFileQueryDefault));
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
		while( (start = newQueryDefault.indexOf(":#######", start)) != -1)
		   // add ":" before concept
		   newQueryDefault.replace(start, start+8, ":"+concept);
		return newQueryDefault;
	}
	
	public void fillQuery() throws IOException {	
	    StringBuffer queryDefault = this.readFileQueryDefault();
	    StringBuffer newQueryDefault = null;
	    String newConcept = null;
	    QueryString queryString = null;
		for(QuerySparql x: this.getListQuerySparql().getList()) {
			newConcept = x.getConcept().getUnderlineConcept();
			newQueryDefault = this.replaceQueryDefault(queryDefault, newConcept);
			queryString = new QueryString(newQueryDefault);
			x.setQuery(queryString);
		}
	}
	
	public void fillRDFs() throws Exception {
		QuerySparql querySparql;
		String queryStr;
		Query query;
		QueryExecution queryExecution;
		Model model;
		ListRDF listRDF;
		StmtIterator stmtIterator;
		Statement statement;
		Resource  subject;
		Property  predicate;
		RDFNode   object;
		OneRDF oneRDF;
		ItemRDF subjectRDF;
		ItemRDF predicateRDF;
		ItemRDF objectRDF;
		for(int i=0; i < this.getListQuerySparql().getList().size(); i++) {
			querySparql = this.getListQuerySparql().getList().get(i);
			queryStr = querySparql.getQueryString().getQueryStrString();
			query = QueryFactory.create(queryStr);
			queryExecution = QueryExecutionFactory.sparqlService(Constants.serviceEndpoint,  query);
			model = queryExecution.execConstruct();
			querySparql.setModel(model);
			listRDF = querySparql.getListRDF();
			
			stmtIterator = model.listStatements();

			while (stmtIterator.hasNext()) {
				// read elements of the model
				statement = stmtIterator.nextStatement();  
				subject   = statement.getSubject();     
				predicate = statement.getPredicate();   
				object    = statement.getObject();
				
				// create complete registerRDF 
				subjectRDF   = new SubjectRDF(subject.toString(), subject, this);
				predicateRDF = new PredicateRDF(predicate.toString(), predicate);
				objectRDF    = new ObjectRDF(object.toString(), object, this);
				oneRDF       = new OneRDF(statement, subjectRDF, predicateRDF, objectRDF);
				
				// insert complete item into list of the RDFs
				listRDF.getList().add(oneRDF);
				
				this.incTotalRDFs();
			}
		    queryExecution.close();
		}
	}
	
	public void buildGraph() {
		QuerySparql querySparql;
		ListRDF listRDF;
		OneRDF oneRDF;
		QuantityNodesEdges quantityNodesEdges = new QuantityNodesEdges();
		for(int i=0; i < this.getListQuerySparql().getList().size(); i++) {
			querySparql = this.getListQuerySparql().getList().get(i);
			listRDF = querySparql.getListRDF();
			for(int j=0; j < listRDF.size(); j++) {
				// get RDF elements
				oneRDF = listRDF.getList().get(j);
				// insert into of graph

				quantityNodesEdges.reset();
				this.graphData.insertRDF(oneRDF, quantityNodesEdges);
				this.incTotalNodes(quantityNodesEdges.getNumNodes());
				this.incTotalEdges(quantityNodesEdges.getNumEdges());
				this.incTotalNodesDuplicate(2 - quantityNodesEdges.getNumNodes());
				this.incTotalEdgesDuplicate(1 - quantityNodesEdges.getNumEdges());
			}
		}
	}

	public String toString() {
		return  "\nlistQuerySparql = "		+ this.getListQuerySparql().toString() + 
				"\n\ngraph = " 				+ this.getGraph().toString() +
				"\ntotalRDF = " 			+ this.getTotalRDFs() +
				"\ntotalNodes = " 			+ this.getTotalNodes() +
				"\ntotalEdges = " 			+ this.getTotalEdges() + 
				"\ntotalNodesDuplicate = " 	+ this.getTotalNodesDuplicate() +
				"\ntotalEdgesDuplicate = " 	+ this.getTotalEdgesDuplicate();
	}	
}
