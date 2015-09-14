package rdf;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import main.Constants;
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
		String str = fileName.replace(';', ':').replace('_', ' ');
		StringBuffer newStr = new StringBuffer();
		for(int i=0; i < str.length() - 4; i++) {
			if(str.charAt(i) == '^') {
			   i++;
			   newStr.append(Character.toUpperCase((str.charAt(i))));
			}
			else
			{
				newStr.append(str.charAt(i));
			}
		}
		return newStr.toString();
	}
	public static String formatToFileName(String concept) {
		String str = concept.replace(':', ';').replace(' ', '_');
		StringBuffer newStr = new StringBuffer();
		for(int i=0; i < str.length(); i++) {
			if(str.charAt(i) >= 'A' && str.charAt(i) <= 'Z' ) {
			   newStr.append('^');
			   newStr.append(Character.toLowerCase((str.charAt(i))));
			}
			else
			{
				newStr.append(str.charAt(i));
			}
		}
		newStr.append(".dat");
		return newStr.toString();
	}
	
	public String toString() {
		StringBuffer out = new StringBuffer();
		Iterator<String> i = this.table.keySet().iterator(); 
		while(i.hasNext()) {
		   out.append((String)i.next()); 
		   out.append(", ");
		}
		return out.toString();
	}
	public String toStringAux() {
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
