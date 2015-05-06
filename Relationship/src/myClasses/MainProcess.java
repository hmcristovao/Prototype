package myClasses;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import basic.*;

public class MainProcess implements Constants {

	public static void head(Wrapterms parser) {
		try {
			parser = new Wrapterms(new FileInputStream("txt_files\\"+Constants.nameFileInput));
			Dataset originalDataset  = new Dataset();
			parser.start(originalDataset);
			Debug.DEBUG(originalDataset.toString());
			originalDataset.fillQuery();
			Debug.DEBUG(originalDataset.toString());
			originalDataset.fillRDFs();
			Debug.DEBUG(originalDataset.toString());
			originalDataset.buildGraph();
			Debug.DEBUG(originalDataset.toString());
			originalDataset.getGraph().getGraph().display(true);
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
	
}

