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
    private int totalNodes;
    private int totalEdges;
    private int totalNodesDuplicate;
	private int totalEdgesDuplicate;
	
	public Dataset() {
		this.listQuerySparql = new ListQuerySparql();
		this.graphData = new GraphData();
		this.totalRDF            = 0;
		this.totalNodes          = 0;
		this.totalEdges          = 0;
		this.totalNodesDuplicate = 0;
		this.totalEdgesDuplicate = 0;
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
		return this.totalNodes;
	}
	public void incTotalNodes() {
		this.totalNodes++;
	}
	public void incTotalNodes(int value) {
		this.totalNodes += value;
	}

	public int getTotalEdges() {
		return this.totalEdges;
	}
	public void incTotalEdges() {
		this.totalEdges++;
	}
	public void incTotalEdges(int value) {
		this.totalEdges += value;
	}

	public int getTotalNodesDuplicate() {
		return this.totalNodesDuplicate;
	}
	public void incTotalNodesDuplicate() {
		this.totalNodesDuplicate++;
	}
	public void incTotalNodesDuplicate(int value) {
		this.totalNodesDuplicate += value;
	}

	public int getTotalEdgesDuplicate() {
		return this.totalEdgesDuplicate;
	}
	public void incTotalEdgesDuplicate() {
		this.totalEdgesDuplicate++;
	}
	public void incTotalEdgesDuplicate(int value) {
		this.totalEdgesDuplicate += value;
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
	
	public void buildGraph() {
		QuerySparql querySparql;
		ListRDF listRDF;
		ItemRDF itemRDF;
		SubjectRDF subjectRDF;
		PredicateRDF predicateRDF;
		ObjectRDF objectRDF;
		for(int i=0; i < this.getListQuerySparql().getList().size(); i++) {
			querySparql = this.getListQuerySparql().getList().get(i);
			listRDF = querySparql.getListRDF();
			for(int j=0; j < listRDF.size(); j++) {
				// get RDF elements
				itemRDF = listRDF.getList().get(j);
				subjectRDF = itemRDF.getSubject();
				predicateRDF = itemRDF.getPredicate();
				objectRDF = itemRDF.getObject();
				// insert into of graph
				if(graphData.insertNode(subjectRDF.getValue())) {
					this.incTotalNodes();
					Debug.DEBUG("inserted node:", subjectRDF.getValue());
				}
				else {
					this.incTotalNodesDuplicate();
					Debug.DEBUG("not inserted node:", subjectRDF.getValue());
				}
				if(graphData.insertNode(objectRDF.getValue())) {
					this.incTotalNodes();
					Debug.DEBUG("inserted node:", objectRDF.getValue());
				}
				else {
					this.incTotalNodesDuplicate();
					Debug.DEBUG("not inserted node:", subjectRDF.getValue());
				}
				if(graphData.insertEdge(predicateRDF.getValue(), subjectRDF.getValue(), objectRDF.getValue())) {
					this.incTotalEdges();
					Debug.DEBUG("inserted edge:", predicateRDF.getValue()+" - "+subjectRDF.getValue()+" - "+objectRDF.getValue());
				}
				else {
					this.incTotalEdgesDuplicate();
					Debug.DEBUG("not inserted edge:", predicateRDF.getValue()+" - "+subjectRDF.getValue()+" - "+objectRDF.getValue());
				}
			}
		}
	}

	public String toString() {
		return  "\nlistQuerySparql = \n" + this.getListQuerySparql().toString() + 
				"\ngraph = " + this.getGraph().toString() +
				"\ntotalRDF = " + this.getTotalRDFs() +
				"\ntotalNodes = " + this.getTotalNodes() +
				"\ntotalEdges = " + this.getTotalEdges() + 
				"\ntotalNodesDuplicate = " + this.getTotalNodesDuplicate() +
				"\ntotalEdgesDuplicate = " + this.getTotalEdgesDuplicate();
	}	
}
