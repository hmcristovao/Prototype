package myClasses;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

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

public class Dataset implements Constants {
	private ListQuerySparql listQuerySparql;
	private Graph graph;
	private int totalRDF;
	
	public Dataset() {
		this.listQuerySparql = new ListQuerySparql();
		this.graph = new SingleGraph("Graph");
	}
	
	public ListQuerySparql getListQuerySparql() {
		return this.listQuerySparql;
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
		while( (start = newQueryDefault.indexOf(":Concept", start)) != -1)
		   newQueryDefault.replace(start, start+8, concept);
		return newQueryDefault;
	}
	
	public void fillQuery() throws IOException {	
	    StringBuffer queryDefault = this.readFileQueryDefault();
	    StringBuffer newQueryDefault = null;
	    String newConcept = null;
	    QueryString queryString = null;
		for(QuerySparql x: this.getListQuerySparql().getList()) {
			newConcept = x.getConcept().getFormatedConcept();
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
		ItemRDF itemRDF;
		SubjectRDF subjectRDF;
		PredicateRDF predicateRDF;
		ObjectRDF objectRDF;
		for(int i=0; i < this.getListQuerySparql().getList().size(); i++) {
			querySparql = this.getListQuerySparql().getList().get(i);
			queryStr = querySparql.getQueryString().getValueString();
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
				
				// create complete item 
				subjectRDF   = new SubjectRDF(subject.toString());
				predicateRDF = new PredicateRDF(predicate.toString());
				objectRDF    = new ObjectRDF(object.toString());
				itemRDF      = new ItemRDF(statement, subjectRDF, predicateRDF, objectRDF);
				
				// insert complete item into list of the RDFs
				listRDF.getList().add(itemRDF);
				
				this.incTotalRDFs();
			}
		    queryExecution.close();
		}
	}	

}
