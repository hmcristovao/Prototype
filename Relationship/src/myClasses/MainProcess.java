package myClasses;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
//import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.sparql.resultset.SPARQLResult;

import basic.*;

public class MainProcess {

	public static void head(Wrapterms parser) {
		try {
			parser = new Wrapterms(new FileInputStream("txt_files\\terms.txt"));
			ListQuerySparql originalList = new ListQuerySparql();
			parser.start(originalList);
			//MainProcess.showConsoleList(originalList);
			originalList.fillQuery();
			//MainProcess.showConsoleList(originalList);
			MainProcess.fillRDFs(originalList);
			//MainProcess.showConsoleList(originalList);
			

		}
		catch(FileNotFoundException e) {
			System.out.println("Error: file not found.");
			e.printStackTrace();
		}
		catch (IOException e) {
			System.out.println("Error: problem with the persistent file: " + e.getMessage());
			e.printStackTrace();
		}
		catch(TokenMgrError e) {
			System.out.println("Lexical error: " + e.getMessage());
			e.printStackTrace();
		}
		catch(SemanticException e) {
			System.out.println("Semantic error: " + e.getMessage());
			e.printStackTrace();
		}
		catch(ParseException e) {
			System.out.println("Sintax error: " + e.getMessage());
			e.printStackTrace();
		}
		// get the rest
		catch(Exception e) {
			System.out.println("Other: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void showConsoleList(ListQuerySparql list) {
		System.out.println("\n=====================================================\n");
		System.out.println(list);
	}
	
	public static void fillRDFs(ListQuerySparql list) throws Exception {
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
		for(int i=0; i < list.getList().size(); i++) {
			querySparql = list.getList().get(i);
			queryStr = querySparql.getQueryString().getValueString();
			query = QueryFactory.create(queryStr);
			queryExecution = QueryExecutionFactory.sparqlService(ConstListQuerySparql.serviceEndpoint,  query);
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
			}
		    queryExecution.close();
		}
	}	
}

