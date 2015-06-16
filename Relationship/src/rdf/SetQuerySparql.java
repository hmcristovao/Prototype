package rdf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import main.Config;
import user.Concept;

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

public class SetQuerySparql {
	private LinkedList<QuerySparql> listQuerySparql;
	private int totalRDF;
	private LinkedList<Concept> listNewConcepts;  // will be fill after

	public SetQuerySparql() {
		this.listQuerySparql = new LinkedList<QuerySparql>();
		this.totalRDF  = 0;
		this.listNewConcepts = new LinkedList<Concept>();
	}
	
	public LinkedList<QuerySparql> getListQuerySparql() {
		return this.listQuerySparql;
	}
	public int getTotalConcepts() {
		return this.listQuerySparql.size();
	}
	public int getTotalRDFs() {
		return this.totalRDF;
	}
	public void insertNewConcept(Concept concept) {
		this.listNewConcepts.add(concept);
	}
	public void insertNewConcept(String stringNewConcept) {
		Concept objectNewConcept = new Concept(stringNewConcept);
		this.listNewConcepts.add(objectNewConcept);
	}
	public LinkedList<Concept> getListNewConcepts() {
		return this.listNewConcepts;
	}
	public void incTotalRDFs() {
		this.totalRDF++;
	}
	public void incTotalRDFs(int value) {
		this.totalRDF += value;
	}
	public void insertQuerySparql(QuerySparql querySparql) {
		this.listQuerySparql.add(querySparql);  
	}
	// create a QuerySparql object, fill it with concept, and insert it into listQuerySparql
	public void insertQuerySparql(Concept concept) {
		QueryString auxQuery = new QueryString();
		ListRDF auxListRDF = new ListRDF();
		QuerySparql querySparql = new QuerySparql(concept, auxQuery, auxListRDF);
		this.listQuerySparql.add(querySparql);  
	}
	// fill listQuerySparql with a concept list
	public void insertListConcept(LinkedList<Concept> listConcept) {
		QueryString auxQuery;
		ListRDF auxListRDF;
		QuerySparql querySparql;
		for(Concept concept : listConcept) { 
			auxQuery = new QueryString();
			auxListRDF = new ListRDF();
			querySparql = new QuerySparql(concept, auxQuery, auxListRDF);
			this.listQuerySparql.add(querySparql); 
		}
	}
	// add all objects from old listQuerySparql
	public void insertListQuerySparql(LinkedList<QuerySparql> listQuerySparql) {
		for(QuerySparql querySparql : listQuerySparql) { 
			this.listQuerySparql.add(querySparql); 
		}
	}
		
	// exceptional function, direct access to original concepts listQuerySparql
	// return one concept short name per line
	public String getOriginalConcepts() {
		StringBuffer out = new StringBuffer();
		for(QuerySparql x: this.listQuerySparql) {
			out.append(x.getConcept().toString());
			out.append("\n");
		}
		return out.toString();
	}
	// exceptional function, direct access to original concepts listQuerySparql
	// return one object LinkedList containing all original concepts short name 
	public LinkedList<Concept> getLinkedListOriginalConcepts() {
		LinkedList<Concept> list = new LinkedList<Concept>();
		for(QuerySparql x: this.listQuerySparql) 		
			list.add(x.getConcept());
		return list;
	}

	private StringBuffer readFileQueryDefault() throws IOException {
		BufferedReader fileQueryDefault = new BufferedReader(new FileReader(Config.nameFileQueryDefault));
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
		for(QuerySparql x: this.getListQuerySparql()) {
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
		for(int i=0; i < this.getListQuerySparql().size(); i++) {
			querySparql = this.getListQuerySparql().get(i);
			queryStr = querySparql.getQueryString().getQueryStrString();
			query = QueryFactory.create(queryStr);
			queryExecution = QueryExecutionFactory.sparqlService(Config.serviceEndpoint,  query);
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
				
				// insert complete item into listQuerySparql of the RDFs
				listRDF.getList().add(oneRDF);
				
				this.incTotalRDFs();
			}
		    queryExecution.close();
		}
	}
	
	@Override
	public String toString() {
		StringBuffer out = new StringBuffer();
		int n = 1;
		for(QuerySparql x: this.listQuerySparql) {
			out.append("\n***** Concept number ");
			out.append(n++);
			out.append(" *****\n");
			out.append(x.toString());
			out.append("\n");
		}
		return out.toString();
	}
}
