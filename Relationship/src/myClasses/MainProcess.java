package myClasses;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
//import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;

import basic.*;

public class MainProcess {

	public static void head(Wrapterms parser) {
		try {
			parser = new Wrapterms(new FileInputStream("txt_files\\terms.txt"));
			ListQuerySparql originalList = new ListQuerySparql();
			parser.start(originalList);
			MainProcess.showConsoleList(originalList);
			originalList.fillQuery();
			MainProcess.showConsoleList(originalList);
			MainProcess.fillRDFs(originalList);
			MainProcess.showConsoleList(originalList);
			

		}
		catch(FileNotFoundException e) {
			System.out.println("Error: file not found.");
		}
		catch (IOException e) {
			System.out.println("Error: problem with the persistent file: " + e.getMessage());
		}
		catch(TokenMgrError e) {
			System.out.println("Lexical error: " + e.getMessage());
		}
		catch(SemanticException e) {
			System.out.println("Semantic error: " + e.getMessage());
		}
		catch(ParseException e) {
			System.out.println("Sintax error: " + e.getMessage());
		}
		// get the rest
		catch(Exception e) {
			System.out.println("Other: " + e.getMessage());
		}
	}
	
	public static void showConsoleList(ListQuerySparql list) {
		System.out.println("\n=====================================================\n");
		System.out.println(list);
	}
	
	public static void fillRDFs(ListQuerySparql list) throws Exception {
		
		// query string de teste
		//String queryStr = "PREFIX : <http://dbpedia.org/resource/> CONSTRUCT { :Concept ?predicate ?object . } WHERE {  :Concept ?predicate ?object . FILTER regex(?object, \"http://dbpedia.org/resource/\") }";
		
		// pegando a primeira query string
		String queryStr = list.getList().get(0).getQueryString().getValueString();
		// imprime para testar:
		System.out.println("\n\nqueryStr=\n"+queryStr);
		
		Query query = QueryFactory.create(queryStr);
		QueryExecution queryExecution = QueryExecutionFactory.sparqlService(ConstListQuerySparql.serviceEndpoint,  query);
		ResultSet resultSet = queryExecution.execSelect();
		while (resultSet.hasNext()){
		    resultSet.nextSolution();
		    System.out.println(ResultSetFormatter.asText(resultSet));
		}
		queryExecution.close();
	}
	//
	// Como estava antes:
	/*
	while (resultSet.hasNext()){
	    QuerySolution querySolution = resultSet.nextSolution();
	    System.out.println(querySolution.getResource("?URI").toString());
	}
	*/
	

}
