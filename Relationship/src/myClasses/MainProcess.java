package myClasses;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import basic.*;

public class MainProcess {

	public static void head(Wrapterms parser) {

	      try {
	         parser = new Wrapterms(new FileInputStream("txt_files\\terms.txt"));
	         ListQuerySparql originalList = new ListQuerySparql();
	         parser.start(originalList);
			 System.out.println("originalList=\n");
	         System.out.println(originalList);
	         System.out.println("\n"+originalList.getListConcept());
	         originalList.fillQuery();
			 System.out.println("originalList (after fill query)=\n");
	         System.out.println(originalList);
	         
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

}
