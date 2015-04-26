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
import com.hp.hpl.jena.rdf.model.Model;

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
		
		// query string of test
		//String queryStr = "PREFIX : <http://dbpedia.org/resource/> CONSTRUCT { :Concept ?predicate ?object . } WHERE {  :Concept ?predicate ?object . FILTER regex(?object, \"http://dbpedia.org/resource/\") }";
		String queryStr = "PREFIX foaf: <http://xmlns.com/foaf/0.1/> CONSTRUCT { ?subj foaf:name ?name } WHERE { ?subj foaf:name ?name  FILTER regex(?subj, \"Henrique\") } LIMIT 10";
		
		// to test, to get the first query string
		//String queryStr = list.getList().get(0).getQueryString().getValueString();
		// imprime para testar:
		//Debug.DEBUG("queryStr",queryStr);
		
		// it's ok!
		Query query = QueryFactory.create(queryStr);
		
		Debug.DEBUG(query.toString());
		
		// it's ok!
		QueryExecution queryExecution = QueryExecutionFactory.sparqlService(ConstListQuerySparql.serviceEndpoint,  query);
		Debug.DEBUG("1");
		
		
		Debug.DEBUG(queryExecution.toString());
		Debug.DEBUG("2");
		
		Model data = queryExecution.execConstruct();
		Debug.DEBUG("3");
		
		// data.write(System.out,"TURTLE");
		Debug.DEBUG(data.toString());
		
        //Model model = queryExecution.execConstruct();
		//Debug.DEBUG("2");
		/*
		while (resultSet.hasNext()){
		
			Debug.DEBUG("3");

		    resultSet.nextSolution();
			Debug.DEBUG("4");

		    System.out.println(ResultSetFormatter.asText(resultSet));
		}
		queryExecution.close();
		*/
		Debug.DEBUG("FIM!");
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
