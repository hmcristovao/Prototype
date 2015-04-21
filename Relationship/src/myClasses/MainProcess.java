package myClasses;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

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
	}
	
	public static void showConsoleList(ListQuerySparql list) {
		System.out.println("\n=====================================================\n");
		System.out.println(list);
	}
	
	public static void fillRDFs(ListQuerySparql list) {
		String queryStr = " ";
		QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql",  queryStr);
		ResultSet resultSet = queryExecution.execSelect();
		while (resultSet.hasNext()){
		    QuerySolution querySolution = resultSet.nextSolution();
		    System.out.println(querySolution.getResource("?URI").toString());
		}
	}
	// ver este código, depois de resolver o problema ...
	// http://stackoverflow.com/questions/24116853/query-sparql-to-dbpedia-using-java-code
	

}
