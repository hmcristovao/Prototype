package rdf;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import main.Config;
import main.WholeSystem;
import parse.Token;

public class RdfsFilesTable  implements Serializable   {
	Map<String, String> table; // concept, fileName (change : to ;)
	
	public RdfsFilesTable() {
		this.table = new HashMap<String,String>();
	}
	public void init(String directory) {
		File directoryFile = new File(directory);  
        for(File file : directoryFile.listFiles()) { 
           String concept = RdfsFilesTable.formatToConcept(file.getName());
           this.put(concept, file.toString());  
        }
	}
	public boolean containsKey(String fileName) {
		return this.table.containsKey(fileName);
	}
	public void put(String concept, String fileName) {
		this.table.put(concept, fileName);
	}
	public String get(String concept) {
		return this.table.get(concept);
	}
	public int size() {
		return this.table.size();
	}
	
	public static String formatToConcept(String fileName) {
		return fileName.replace(';', ':').replace('_', ' ');
	}
	public static String formatToFileName(String concept) {
		return concept.replace(':', ';').replace(' ', '_');
	}
	
	public String toString() {
		StringBuffer out = new StringBuffer();
		Iterator<String> i = this.table.keySet().iterator(); 
		while(i.hasNext()) {
		   String concept = (String)i.next(); 
		   String fileName = this.table.get(concept);
		   out.append(fileName);
		   out.append(", ");
		}
		return out.toString();
	}
}
