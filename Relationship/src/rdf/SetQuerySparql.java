package rdf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import org.apache.jena.riot.WebContent;

import main.Config;
import main.Log;
import main.WholeSystem;
import user.Concept;
import user.GroupConcept;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;

public class SetQuerySparql {
	private LinkedList<QuerySparql> listQuerySparql;
	private int totalRDF;

	public SetQuerySparql() {
		this.listQuerySparql = new LinkedList<QuerySparql>();
		this.totalRDF  = 0;
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
	public void incTotalRDFs() {
		this.totalRDF++;
	}
	public void incTotalRDFs(int value) {
		this.totalRDF += value;
	}
	public void insertQuerySparql(QuerySparql querySparql) {
		this.listQuerySparql.add(querySparql);  
	}
	
	// get total RDFs of an especifc concept
	public int getTotalRdfsEspecificConcept(String blankName) {
		for(QuerySparql querySparql : this.listQuerySparql) {
			if(querySparql.getConcept().getBlankName().equals(blankName))
				return querySparql.sizeListRDF();
		}
		// it did not find concept
		return 0;
	}
	
	// create a QuerySparql object, fill it with concept, and insert it into listQuerySparql
	public void insertQuerySparql(Concept concept) {
		QueryString auxQuery = new QueryString();
		ListRDF auxListRDF = new ListRDF();
		QuerySparql querySparql = new QuerySparql(concept, auxQuery, auxListRDF);
		this.listQuerySparql.add(querySparql);  
	}

	// fill listQuerySparql with a concept list
	public void insertListConcept(GroupConcept groupConcept) {
		QuerySparql querySparql;
		for(Concept concept : groupConcept.getList()) { 
			querySparql = new QuerySparql(concept, new QueryString(), new ListRDF());
			this.listQuerySparql.add(querySparql); 
		}
	}

	// add all objects from old listQuerySparql
	// return quantity of the element copied 
	public int insertListQuerySparql(LinkedList<QuerySparql> listQuerySparql) {
		int count = 0;
		for(QuerySparql querySparql : listQuerySparql) { 
			this.listQuerySparql.add(querySparql);
			count++;
		}
		return count;
	}
		
	// exceptional function, direct access to current concepts listQuerySparql
	// return one concept short name per line
	public String getCurrentConcepts() {
		StringBuffer out = new StringBuffer();
		for(QuerySparql x: this.listQuerySparql) {
			out.append(x.getConcept().toString());
			out.append("\n");
		}
		return out.toString();
	}
	// exceptional function, direct access to original concepts listQuerySparql
	// return one object LinkedList containing all current concepts short name 
	public GroupConcept getListCurrentConcepts() {
		GroupConcept list = new GroupConcept();
		for(QuerySparql x: this.listQuerySparql) 		
			list.add(x.getConcept());
		return list;
	}

	private StringBuffer readFileQueryDefault() throws IOException {
		BufferedReader fileQueryDefault = new BufferedReader(new FileReader(Config.nameQueryDefaultFile));
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
		while( (start = newQueryDefault.indexOf(Config.markQueryReplacement, start)) != -1)
		   newQueryDefault.replace(start, start+Config.markQueryReplacement.length(), concept);
		return newQueryDefault;
	}
	
	public int assemblyQueries() throws IOException {	
	    StringBuffer queryDefault = this.readFileQueryDefault();
	    StringBuffer newQueryDefault = null;
	    String newConcept = null;
	    QueryString queryString = null;
	    int n = 0;
		for(QuerySparql x: this.getListQuerySparql()) {
			newConcept = x.getConcept().getUnderlineConcept();
			newQueryDefault = this.replaceQueryDefault(queryDefault, newConcept);
			queryString = new QueryString(newQueryDefault);
			x.setQuery(queryString);
			n++;
		}
		return n;
	}
	
	//  read all RDF triples belongs to each query concept 
	public int collectRDFs() throws Exception {
		int total = 0, subTotal;
		for(int i=0; i < this.getListQuerySparql().size(); i++) {
			QuerySparql querySparql = this.getListQuerySparql().get(i);
			String queryStr = querySparql.getQueryString().getQueryStrString();

			Query query = QueryFactory.create(queryStr);
			QueryEngineHTTP queryEngineHTTP = (QueryEngineHTTP)QueryExecutionFactory.sparqlService(Config.selectedServiceSnorqlEndPoint,  query);
			queryEngineHTTP.setModelContentType(WebContent.contentTypeJSONLD);
			Model model = queryEngineHTTP.execConstruct();
			
			querySparql.setModel(model);
			ListRDF listRDF = querySparql.getListRDF();
			StmtIterator stmtIterator = model.listStatements();

			subTotal = 0;
			while (stmtIterator.hasNext()) {
				// read elements of the model
				Statement statement = stmtIterator.nextStatement();  
				Resource  subject   = statement.getSubject();     
				Property  predicate = statement.getPredicate();   
				RDFNode   object    = statement.getObject();
				
				// create complete registerRDF 
				ItemRDF subjectRDF   = new SubjectRDF(subject.toString(), subject);
				ItemRDF predicateRDF = new PredicateRDF(predicate.toString(), predicate);
				ItemRDF objectRDF    = new ObjectRDF(object.toString(), object);
				OneRDF  oneRDF       = new OneRDF(statement, subjectRDF, predicateRDF, objectRDF);
				
				// insert complete item into listQuerySparql of the RDFs
				listRDF.getList().add(oneRDF);
				total++;
				subTotal++;
				
				this.incTotalRDFs();
			}
			queryEngineHTTP.close();
			// update rdfs quantity in concept
			querySparql.getConcept().setQuantityRdfs(subTotal);
			// if exist concept into WholeSystem then update its quantity
			WholeSystem.getConceptsRegister().getConcept(querySparql.getConcept().getBlankName()).setQuantityRdfs(subTotal);
		}
		return total;
	}
	
	// consider basicConcept without underline character
	public boolean isCurrentConcept(String basicConcept) {
		for(QuerySparql x : this.getListQuerySparql()) {
			if(x.getConcept().equals(basicConcept))
				return true;
		}
		return false;
	}
	
	public String toStringShort() {
		StringBuffer out = new StringBuffer();
		int n = 1;
		for(QuerySparql x: this.listQuerySparql) {
			out.append("\nConcept number ");
			out.append(n++);
			out.append(": ");
			out.append(x.toStringShort());
			out.append("\n");
		}
		return out.toString();
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
